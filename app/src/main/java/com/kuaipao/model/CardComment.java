package com.kuaipao.model;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.kuaipao.base.CardConfig;
import com.kuaipao.utils.LangUtils;
import com.kuaipao.utils.WebUtils;

public class CardComment implements Serializable {
    private static final long serialVersionUID = -3710259299760138273L;

    private static final String KEY_COMMENT_ID = "cid"; // id of business district
    private static final String KEY_MERCHANT_ID = "gid";// id of merchant
    private static final String KEY_CONTENT = "content";
    private static final String KEY_RATING = "rating";
    private static final String KEY_LIKES_NUM = "likes";
    private static final String KEY_EDIT_TIME = "edit_time";
    private static final String KEY_IS_LIKE = "is_like";
    private static final String KEY_COURSES = "courses";
    private static final String KEY_IMGS = "imgs";
    private static final String KEY_USER = "user";
    private static final String KEY_USER_NAME = "uname";
    private static final String KEY_USER_AVATAR = "avatar";
    private static final String KEY_USER_ID = "uid";
    private static final String KEY_IS_AUTHOR = "is_author";

    private int commentID = -1;
    private int merchantID = -1;
    private String content;
    private int rating = 0;
    private int likeNum;
    private String editTime;//format like: '2015-09-11T16:46:36'
    private boolean isLike;//1 like
    private ArrayList<Integer> courses;
    private ArrayList<CommentImage> imgs;
    private String userName;
    private String userLogo;
    private int userID;
    private boolean isAuthor;

    private ArrayList<CommentImage> rmImgs;
//  private ArrayList<Integer> newImgs;

    public CardComment(int merchantID) {
        this.merchantID = merchantID;
    }

    public static CardComment fromJson(JSONObject j) {
        if (j == null || j.size() == 0) {
            return null;
        }

        int commentID = WebUtils.getJsonInt(j, KEY_COMMENT_ID, -1);
        int merchantID = WebUtils.getJsonInt(j, KEY_MERCHANT_ID, -1);
        String content = WebUtils.getJsonString(j, KEY_CONTENT);
        int rating = WebUtils.getJsonInt(j, KEY_RATING, 0);
        int likeNum = WebUtils.getJsonInt(j, KEY_LIKES_NUM, 0);
        String editTime = WebUtils.getJsonString(j, KEY_EDIT_TIME);
        boolean isLike = WebUtils.getJsonInt(j, KEY_IS_LIKE, 0) == 1 ? true : false;
        boolean isAuthor = WebUtils.getJsonInt(j, KEY_IS_AUTHOR, 0) == 1 ? true : false;


//    <imgs>: a list: [[img_id, img_url], ...], img_id, img_url: str

        CardComment cc = new CardComment(merchantID);
        cc.setCommentID(commentID);
        cc.setContent(content);
        cc.setRating(rating);
        cc.setLikeNum(likeNum);
        cc.setEditTime(editTime);
        cc.setLike(isLike);
        cc.setAuthor(isAuthor);

        JSONArray coursesArray = WebUtils.getJsonArray(j, KEY_COURSES);
        if (coursesArray != null) {
            ArrayList<Integer> courses = (ArrayList<Integer>) WebUtils.jsonToArrayString(coursesArray);
            cc.setCourses(courses);
        }
        JSONArray imgsArray = WebUtils.getJsonArray(j, KEY_IMGS);
        if (imgsArray != null) {
            ArrayList<CommentImage> imgs = new ArrayList<CardComment.CommentImage>(imgsArray.size());
            for (int i = 0; i < imgsArray.size(); i++) {
                JSONArray imgArray = WebUtils.getJsonArray(imgsArray, i);
                if (imgArray != null) {
                    imgs.add(CommentImage.fromJson(imgArray));
                }
            }
            cc.setImgs(imgs);
        }

        JSONObject userJson = WebUtils.getJsonObject(j, KEY_USER);
        if (userJson != null) {
            String userName = WebUtils.getJsonString(userJson, KEY_USER_NAME);
            String userLogo = WebUtils.getJsonString(userJson, KEY_USER_AVATAR);
            int userID = WebUtils.getJsonInt(userJson, KEY_USER_ID);
            cc.setUserName(userName);
            cc.setUserLogo(userLogo);
            cc.setUserID(userID);
        }

        return cc;
    }

    public JSONObject toJson() {
        JSONObject j = new JSONObject();
        try {
            j.put(KEY_COMMENT_ID, commentID);
            j.put(KEY_MERCHANT_ID, merchantID);
            j.put(KEY_CONTENT, content);
            j.put(KEY_RATING, rating);
            j.put(KEY_COURSES, WebUtils.java2jsonValue(courses));
            j.put(KEY_EDIT_TIME, editTime);
            j.put(KEY_LIKES_NUM, likeNum);
            j.put(KEY_IS_AUTHOR, isAuthor ? 1 : 0);
            j.put(KEY_IS_LIKE, isLike ? 1 : 0);

            if (LangUtils.isNotEmpty(imgs)) {
                JSONArray imgsArray = new JSONArray();
                for (CommentImage ci : imgs) {
                    imgsArray.add(ci.toJson());
                }
                j.put(KEY_IMGS, imgsArray);
            }

            JSONObject userJson = new JSONObject();
            userJson.put(KEY_USER_NAME, userName);
            userJson.put(KEY_USER_AVATAR, userLogo);
            userJson.put(KEY_USER_ID, userID);
            j.put(KEY_USER, userJson);

            return j;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String toString() {
        JSONObject j = toJson();
        return j == null ? super.toString() : j.toString();
    }

    public static class CommentImage implements Serializable {
        private static final long serialVersionUID = -267754181988232211L;

        private String imgId;
        private String imgUrl;
        private String imgLocalPath;

        public static CommentImage fromJson(JSONArray j) {
            if (j == null || j.size() < 2) {//
                return null;
            }

            CommentImage ci = new CommentImage();


            String imgId = WebUtils.getJsonString(j, 0, "");
            String imgUrl = WebUtils.getJsonString(j, 1, "");
            String imgLocalPath = WebUtils.getJsonString(j, 2, "");

            ci.setImgId(imgId);
            if (LangUtils.isNotEmpty(imgUrl) && !imgUrl.startsWith("http")) {
                imgUrl = CardConfig.DEFAULT_URL + imgUrl;
            }
            ci.setImgUrl(imgUrl);
            ci.setImgLocalPath(imgLocalPath);

            return ci;
        }

        public JSONArray toJson() {
            try {
                JSONArray j = new JSONArray();
                if (imgId == null)
                    j.add(0, "");
                else
                    j.add(0, imgId);

                if (imgUrl == null)
                    j.add(1, "");
                else
                    j.add(1, imgUrl);

                if (imgLocalPath == null)
                    j.add(2, "");
                else
                    j.add(2, imgLocalPath);

                return j;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public String toString() {
            JSONArray j = toJson();
            return j == null ? super.toString() : j.toString();
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getImgLocalPath() {
            return imgLocalPath;
        }

        public void setImgLocalPath(String imgLocalPath) {
            this.imgLocalPath = imgLocalPath;
        }

        public String getImgId() {
            return imgId;
        }

        public void setImgId(String imgId) {
            this.imgId = imgId;
        }
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }

    public String getEditTime() {
        return editTime;
    }

    public void setEditTime(String editTime) {
        this.editTime = editTime;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean isLike) {
        this.isLike = isLike;
    }

    public ArrayList<Integer> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<Integer> courses) {
        this.courses = courses;
    }

    public ArrayList<String> getImgIDs() {
        if (imgs == null)
            return null;
        ArrayList<String> imgIDs = new ArrayList<String>();
        for (CommentImage ci : imgs) {
            if (LangUtils.isNotEmpty(ci.getImgId()))
                imgIDs.add(ci.getImgId());
        }
        return imgIDs;
    }

    public ArrayList<CommentImage> getImgs() {
        return imgs;
    }

    public void setImgs(ArrayList<CommentImage> imgs) {
        this.imgs = imgs;
    }

    public ArrayList<String> parseImgsPathes() {
        if (imgs == null) {
            return null;
        }
        ArrayList<String> pics = new ArrayList<String>();
        for (CommentImage ci : imgs) {
            if (LangUtils.isNotEmpty(ci.imgLocalPath) &&
                    new File(ci.imgLocalPath).exists()) {
                pics.add(ci.imgLocalPath);
            } else {
                if (LangUtils.isNotEmpty(ci.imgUrl)) {
                    pics.add(ci.imgUrl);
                }
            }
        }
//    for(String path : pics){
//      LogUtils.d("#### getImgsPathes path= %s", path);
//    }
        return pics;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserLogo() {
        return userLogo;
    }

    public void setUserLogo(String userLogo) {
        this.userLogo = userLogo;
    }

    public ArrayList<CommentImage> getRmImgs() {
        return rmImgs;
    }

    public void setRmImgs(ArrayList<CommentImage> rmImgs) {
        this.rmImgs = rmImgs;
    }

    public int getCommentID() {
        return commentID;
    }

    public void setCommentID(int commentID) {
        this.commentID = commentID;
    }

    public int getMerchantID() {
        return merchantID;
    }

    public void setMerchantID(int merchantID) {
        this.merchantID = merchantID;
    }

    public boolean isAuthor() {
        return isAuthor;
    }

    public void setAuthor(boolean isAuthor) {
        this.isAuthor = isAuthor;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
