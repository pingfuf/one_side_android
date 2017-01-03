package com.kuaipao.manager;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import com.alibaba.fastjson.JSONObject;
import com.kuaipao.model.CardComment;
import com.kuaipao.model.CardComment.CommentImage;
import com.kuaipao.utils.Constant;
import com.kuaipao.utils.IOUtils;
import com.kuaipao.utils.LangUtils;
import com.kuaipao.utils.LogUtils;
import com.kuaipao.utils.UploadPicFileUtils;
import com.kuaipao.utils.WebUtils;
import com.kuaipao.base.net.UrlRequest;

public class CardCommentPostManager {

    private static CardCommentPostManager _instance;
    private static ExecutorService COMMENT_POSTER_EXECUTOR = Executors.newFixedThreadPool(1);
    private static ExecutorService IMAGE_POSTER_EXECUTOR = Executors.newFixedThreadPool(2);
    private static volatile boolean bCancel = false;
    private static final String CHARSET = "utf-8";
    private static ArrayList<OnPostStateChangedListener> mOnPostStateChangedListeners;

    private CardCommentPostManager() {
    }

    public synchronized static CardCommentPostManager getInstance() {
        if (_instance == null) {
            _instance = new CardCommentPostManager();
        }
        return _instance;
    }

    public static interface OnPostStateChangedListener {
        public void onPostState(CardComment cardComment, boolean bSuccess);
    }

    public synchronized void registerOnPostStateChangedListener(OnPostStateChangedListener l) {
        if (l == null)
            return;
        if (mOnPostStateChangedListeners == null)
            mOnPostStateChangedListeners =
                    new ArrayList<CardCommentPostManager.OnPostStateChangedListener>();
        mOnPostStateChangedListeners.add(l);
    }

    public synchronized void unregisterOnPostStateChangedListener(OnPostStateChangedListener l) {
        if (l == null || mOnPostStateChangedListeners == null)
            return;
        Iterator<OnPostStateChangedListener> iterator = mOnPostStateChangedListeners.iterator();
        while (iterator.hasNext()) {
            if (iterator.next() == l)
                iterator.remove();
        }
    }

    public synchronized void clearOnPostStateChangedListener() {
        if (mOnPostStateChangedListeners == null)
            return;
        mOnPostStateChangedListeners.clear();
        mOnPostStateChangedListeners = null;
    }

    public synchronized void autoPostComment() {
        String[] marks = getCommentMarks();
        if (marks != null && marks.length > 0) {
            for (String mark : marks) {
                if (LangUtils.isNotEmpty(mark.trim())) {
                    int merchantID = LangUtils.parseInt(mark.trim());
                    if (merchantID > 0) {
                        String strComment =
                                IOUtils.getPreferenceValue(Constant.PREFERENCE_KEY_COMMENT_PRE + mark.trim());
                        if (LangUtils.isNotEmpty(strComment.trim())) {
                            JSONObject j = WebUtils.parseJsonObject(strComment);
                            if (j != null) {
                                try {
                                    CardComment cc = CardComment.fromJson(j);
                                    if (cc != null) {
                                        postComment(cc);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void postComment(CardComment cardComment) {

        LogUtils.d("#### postComment cardComment: %s", cardComment);
        if (cardComment == null) {
            return;
        }

        COMMENT_POSTER_EXECUTOR.execute(new CommentPoster(cardComment));
    }

    public synchronized static void saveCommentMark(CardComment cardComment) {
        StringBuilder existedLocalComments = new StringBuilder();
        String[] marks = getCommentMarks();
        String currentMerchantID = LangUtils.parseString(cardComment.getMerchantID());
        if (marks != null && marks.length > 0) {
            for (String mark : marks) {
                if (!mark.equals(currentMerchantID) && LangUtils.isNotEmpty(mark.trim())) {
                    existedLocalComments.append(mark);
                    existedLocalComments.append(",");
                }
            }
        }
        existedLocalComments.append(currentMerchantID);
        // if(LangUtils.isEmpty(existedLocalComments.toString()))
        // IOUtils.removePreferenceValue(Constant.PUBLISH_COMMENT_LOCAL_MERCHANT_IDS);
        // else
        IOUtils.savePreferenceValue(Constant.PUBLISH_COMMENT_LOCAL_MERCHANT_IDS,
                existedLocalComments.toString());
        LogUtils.d("#### saveCommentMark : %s", existedLocalComments.toString());

        IOUtils.savePreferenceValue(Constant.PREFERENCE_KEY_COMMENT_PRE + currentMerchantID,
                cardComment.toString());
        LogUtils.d("#### saveCommentMark key=%s, content=%s", Constant.PREFERENCE_KEY_COMMENT_PRE
                + currentMerchantID, cardComment.toString());
    }

    private synchronized static String[] getCommentMarks() {
        String existedLocalComments = IOUtils.getPreferenceValue(Constant.PUBLISH_COMMENT_LOCAL_MERCHANT_IDS);
        if (LangUtils.isNotEmpty(existedLocalComments)) {
            return existedLocalComments.split(",");
        }

        return null;
    }

    public synchronized static void removeCommentMark(CardComment cardComment) {
        StringBuilder existedLocalComments = new StringBuilder();
        String[] marks = getCommentMarks();
        String currentMerchantID = LangUtils.parseString(cardComment.getMerchantID());
        if (marks != null && marks.length > 0) {
            for (String mark : marks) {
                if (!mark.equals(currentMerchantID) && LangUtils.isNotEmpty(mark.trim())) {
                    existedLocalComments.append(mark);
                    existedLocalComments.append(",");
                }
            }
        }
        if (LangUtils.isEmpty(existedLocalComments.toString())) {
            IOUtils.removePreferenceValue(Constant.PUBLISH_COMMENT_LOCAL_MERCHANT_IDS);
        } else {
            IOUtils.savePreferenceValue(Constant.PUBLISH_COMMENT_LOCAL_MERCHANT_IDS,
                    existedLocalComments.toString());
        }
        IOUtils.removePreferenceValue(Constant.PREFERENCE_KEY_COMMENT_PRE + currentMerchantID);
    }

    public void cancelPost() {
        bCancel = true;
        if (COMMENT_POSTER_EXECUTOR != null) {
            COMMENT_POSTER_EXECUTOR.shutdown();
        }

        if (IMAGE_POSTER_EXECUTOR != null) {
            IMAGE_POSTER_EXECUTOR.shutdown();
        }
    }

    public static class CommentPoster implements Runnable {
        private CardComment mCardComment;

        public CommentPoster(CardComment cardComment) {
            super();
            mCardComment = cardComment;
        }

        @Override
        public void run() {
            boolean bCommentPostSuccess = false;
            if (CardSessionManager.getInstance().isOnLine()
                    && CardSessionManager.getInstance().isLogin()) {
                boolean bImagePostSuccess = false;
                if (LangUtils.isNotEmpty(mCardComment.getImgs())) {
                    List<Integer> imgIndexes = new ArrayList<Integer>();
                    for (int i = 0; i < mCardComment.getImgs().size(); i++) {
                        CommentImage ci = mCardComment.getImgs().get(i);
                        if (LangUtils.isEmpty(ci.getImgId()) && LangUtils.isEmpty(ci.getImgUrl())
                                && LangUtils.isNotEmpty(ci.getImgLocalPath()) && new File(ci.getImgLocalPath()).exists())
                            imgIndexes.add(i);
                    }

                    if (LangUtils.isNotEmpty(imgIndexes)) {
                        CountDownLatch end = new CountDownLatch(imgIndexes.size());
                        AtomicInteger atomicInteger = new AtomicInteger(imgIndexes.size());
                        for (int index : imgIndexes) {
                            IMAGE_POSTER_EXECUTOR.execute(new ImagePoster(mCardComment.getImgs(), index, end,
                                    atomicInteger));
                        }
                        try {
                            end.await();
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        if (atomicInteger.get() > 0) {// some imgs upload failed!
                            bImagePostSuccess = false;
                        } else {
                            bImagePostSuccess = true;
                        }
                    } else
                        bImagePostSuccess = true;

                } else {
                    bImagePostSuccess = true;
                }
                if (bImagePostSuccess) {
                    bCommentPostSuccess = publishCommentWeb(mCardComment);
                } else {
                    bCommentPostSuccess = false;
                }
            }
            if (bCommentPostSuccess) {
                removeCommentMark(mCardComment);
            }
            notifyPostStateChanged(mCardComment, bCommentPostSuccess);
        }


        public synchronized void notifyPostStateChanged(CardComment cardComment, boolean bSuccess) {
            if (mOnPostStateChangedListeners == null)
                return;

            Iterator<OnPostStateChangedListener> iterator = mOnPostStateChangedListeners.iterator();
            while (iterator.hasNext()) {
                iterator.next().onPostState(cardComment, bSuccess);
            }
        }

        private boolean publishCommentWeb(final CardComment mCommentByMe) {
            boolean bRes = false;
            UrlRequest r = new UrlRequest("comment/post");
            HttpURLConnection conn = null;
            try {
                URL url = new URL(r.getUrl());
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestMethod("POST");
                conn.setUseCaches(false);
                String COOKIE_KEY = "Cookie";
                String SESSION_TOKEN = "remember_token";
                String sessionId = IOUtils.getPreferenceValue(SESSION_TOKEN);
                if (sessionId.length() > 0) {
                    StringBuilder builder = new StringBuilder();
                    builder.append(SESSION_TOKEN);
                    builder.append("=");
                    builder.append(sessionId);
                    conn.setRequestProperty(COOKIE_KEY, builder.toString());
                }

                conn.setRequestProperty("Charset", CHARSET);
                conn.setRequestProperty("connection", "keep-alive");
                conn.connect();
                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

                String strCourses = "[]";
                if (LangUtils.isNotEmpty(mCommentByMe.getCourses())) {
                    StringBuilder sbAttendCourses = new StringBuilder("[");
                    for (int classType : mCommentByMe.getCourses()) {
                        sbAttendCourses.append(LangUtils.parseString(classType));
                        sbAttendCourses.append(",");
                    }
                    strCourses = sbAttendCourses.toString();
                    if (strCourses.endsWith(",")) {
                        strCourses = strCourses.substring(0, strCourses.length() - 1);
                    }
                    strCourses += "]";
                }

                StringBuilder sbPostContent = new StringBuilder();
                sbPostContent.append(URLEncoder.encode("gid", CHARSET)).append("=")
                        .append(URLEncoder.encode(LangUtils.parseString(mCommentByMe.getMerchantID()), CHARSET))
                        .append("&").append(URLEncoder.encode("content", CHARSET)).append("=")
                        .append(URLEncoder.encode(mCommentByMe.getContent(), CHARSET)).append("&")
                        .append(URLEncoder.encode("rating", CHARSET)).append("=")
                        .append(URLEncoder.encode(LangUtils.parseString(mCommentByMe.getRating()), CHARSET)).append("&")
                        .append(URLEncoder.encode("courses", CHARSET)).append("=")
                        .append(URLEncoder.encode(strCourses, CHARSET));

                if (LangUtils.isNotEmpty(mCommentByMe.getImgIDs())) {
                    String strImgIDs = WebUtils.java2jsonValue(mCommentByMe.getImgIDs()).toString();
                    if (LangUtils.isNotEmpty(strImgIDs)) {
                        sbPostContent.append("&")
                                .append(URLEncoder.encode("imgs", CHARSET)).append("=")
                                .append(URLEncoder.encode(strImgIDs, CHARSET));
                    }
                }

                String postContent = sbPostContent.toString();

                dos.write(postContent.getBytes());
                dos.flush();
                dos.close();

                int resCode = conn.getResponseCode();
                LogUtils.d("#### publishComment resCode=%s", resCode);
                if (resCode == HttpURLConnection.HTTP_OK) {
                    InputStream is = null;
                    is = conn.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    String line = null;
                    StringBuffer sb = new StringBuffer();
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                    if (is != null)
                        is.close();

                    String content = sb.toString();
                    if (LangUtils.isNotEmpty(content)) {
                        JSONObject j = WebUtils.parseJsonObject(content);
                        if (j != null) {
                            int code = WebUtils.getJsonInt(j, "code", -1);
                            if (code == 0) {
                                int commentID = WebUtils.getJsonInt(j, "data");
                                if (commentID >= 0) {
                                    mCommentByMe.setCommentID(commentID);
                                    bRes = true;
                                }
                            }
                        }
                    }
                } else {
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (conn != null)
                    conn.disconnect();
            }

            return bRes;
        }
    }

    public static final UrlRequest imgPosterUrl = new UrlRequest("comment/upload_img");

    public static class ImagePoster implements Runnable {

        private ArrayList<CommentImage> imgs;
        private int imgIndex;
        private CountDownLatch end;
        private AtomicInteger atomicInteger;

        public ImagePoster(ArrayList<CommentImage> imgs, int imgIndex, CountDownLatch end,
                           AtomicInteger atomicInteger) {
            super();
            this.imgIndex = imgIndex;
            this.imgs = imgs;
            this.end = end;
            this.atomicInteger = atomicInteger;
        }

        @Override
        public void run() {
            try {
                if (imgs != null && imgs.size() > imgIndex) {

                    File imgFile = new File(imgs.get(imgIndex).getImgLocalPath());
                    if (imgFile.exists()) {
                        UploadPicFileUtils.uploadImage(imgFile, "img", imgPosterUrl.getUrl(), null,
                                new UploadPicFileUtils.OnUploadProcessListener() {

                                    @Override
                                    public void onUploadDone(int responseCode, String content) {
                                        if (responseCode == UploadPicFileUtils.UPLOAD_SUCCESS_CODE
                                                && LangUtils.isNotEmpty(content)) {
                                            JSONObject j = WebUtils.parseJsonObject(content);
                                            if (j == null)
                                                return;
                                            LogUtils.d("#### ImagePoster j=%s", j);
                                            int code = WebUtils.getJsonInt(j, "code", -1);
                                            if (code == 0) {
                                                atomicInteger.getAndDecrement();
                                                String img_id = WebUtils.getJsonString(j, "data");
                                                if (LangUtils.isNotEmpty(img_id)) {
                                                    synchronized (ImagePoster.class) {
                                                        imgs.get(imgIndex).setImgId(img_id);
                                                    }
                                                }
                                            }
                                        } else {
                                            LogUtils.d("#### onUploadDone responseCode:%s; content = %s",
                                                    responseCode, content);
                                        }
                                    }

                                    @Override
                                    public void onUploadProcess(int uploadSize) {
                                    }

                                    @Override
                                    public void initUpload(int fileSize) {
                                    }
                                });
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                end.countDown();
            }
        }
    }

}
