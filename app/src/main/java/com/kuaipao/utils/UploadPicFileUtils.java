package com.kuaipao.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.alibaba.fastjson.JSONObject;
import com.kuaipao.base.net.RequestDelegate;
import com.kuaipao.base.net.UrlRequest;
import com.kuaipao.model.data.BaseDataSource;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class UploadPicFileUtils {
    private static UploadPicFileUtils uploadUtil;
    private static final String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
    private static final String PREFIX = "--";
    private static final String LINE_END = "\r\n";
    private static final String CONTENT_TYPE = "multipart/form-data"; // 内容类型

    private UploadPicFileUtils() {

    }

    /**
     * 单例模式获取上传工具类
     *
     * @return
     */
    public static UploadPicFileUtils getInstance() {
        if (null == uploadUtil) {
            uploadUtil = new UploadPicFileUtils();
        }
        return uploadUtil;
    }

    private static final String TAG = "UploadUtil";
    private static int readTimeOut = 60 * 1000; // 读取超时
    private static int connectTimeout = 60 * 1000; // 超时时间
    /***
     * 请求使用多长时间
     */
    private static int requestTime = 0;

    private static final String CHARSET = "utf-8"; // 设置编码

    /***
     * 上传成功
     */
    public static final int UPLOAD_SUCCESS_CODE = 1;
    /**
     * 文件不存在
     */
    public static final int UPLOAD_FILE_NOT_EXISTS_CODE = 2;
    /**
     * 服务器出错
     */
    public static final int UPLOAD_SERVER_ERROR_CODE = 3;
    protected static final int WHAT_TO_UPLOAD = 1;
    protected static final int WHAT_UPLOAD_DONE = 2;

    /**
     * android上传文件到服务器
     *
     * @param filePath   需要上传的文件的路径
     * @param fileKey    在网页上<input type=file name=xxx/> xxx就是这里的fileKey
     * @param RequestURL 请求的URL
     */
    public static void uploadFile(String filePath, String fileKey, String RequestURL,
                           Map<String, String> param, OnUploadProcessListener onUploadProcessListener) {
        if (filePath == null) {
            if (onUploadProcessListener != null)
                onUploadProcessListener.onUploadDone(UPLOAD_FILE_NOT_EXISTS_CODE, "文件不存在");
            return;
        }
        try {
            File file = new File(filePath);
            uploadFile(file, fileKey, RequestURL, param, onUploadProcessListener);
        } catch (Exception e) {
            if (onUploadProcessListener != null)
                onUploadProcessListener.onUploadDone(UPLOAD_FILE_NOT_EXISTS_CODE, "文件不存在");
            e.printStackTrace();
            return;
        }
    }

    /**
     * android上传文件到服务器
     *
     * @param file       需要上传的文件
     * @param fileKey    在网页上<input type=file name=xxx/> xxx就是这里的fileKey
     * @param RequestURL 请求的URL
     */
    public static void uploadFile(final File file, final String fileKey, final String RequestURL,
                           final Map<String, String> param, final OnUploadProcessListener onUploadProcessListener) {
        if (file == null || (!file.exists())) {
            if (onUploadProcessListener != null)
                onUploadProcessListener.onUploadDone(UPLOAD_FILE_NOT_EXISTS_CODE, "文件不存在");
            return;
        }

        new Thread(new Runnable() { // 开启线程上传文件
            @Override
            public void run() {
                toUploadFile(file, fileKey, RequestURL, param, onUploadProcessListener);
            }
        }).start();
    }

    public void uploadUserBkgImg(final File file, final String fileKey, final String RequestURL,
                                 final Map<String, String> param, final OnUploadProcessListener onUploadProcessListener) {
        if (file == null || (!file.exists())) {
            if (onUploadProcessListener != null)
                onUploadProcessListener.onUploadDone(UPLOAD_FILE_NOT_EXISTS_CODE, "文件不存在");
            return;
        }

        new Thread(new Runnable() { // 开启线程上传文件
            @Override
            public void run() {
                toUploadImgFile(file, fileKey, RequestURL, param, onUploadProcessListener);
            }
        }).start();
    }

    public void uploadMerchantCardImg(final File file, final String fileKey, final String RequestURL,
                                      final Map<String, String> param, final OnUploadProcessListener onUploadProcessListener) {
        if (file == null || (!file.exists())) {
            if (onUploadProcessListener != null)
                onUploadProcessListener.onUploadDone(UPLOAD_FILE_NOT_EXISTS_CODE, "文件不存在");
            return;
        }

        // Log.i(TAG, "请求的URL=" + RequestURL);
        // Log.i(TAG, "请求的fileName=" + file.getName());
        // Log.i(TAG, "请求的fileKey=" + fileKey);
        new Thread(new Runnable() { // 开启线程上传文件
            @Override
            public void run() {
                toUploadImgFile(file, fileKey, RequestURL, param, onUploadProcessListener);
//            Map<String, String> param = new HashMap<String, String>();
//            param.put(fileKey, ViewUtils.compressBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()),
//                CompressFormat.PNG, 80).toString());
//            toUploadFile2(RequestURL, param, onUploadProcessListener);
            }
        }).start();

    }

    /*
     * Function  :   封装请求体信息
     * Param     :   params请求体内容，encode编码格式
     * Author    :   博客园-依旧淡然
     */
    public static StringBuffer getRequestData(Map<String, String> params, String encode) {
        StringBuffer stringBuffer = new StringBuffer();        //存储封装好的请求体信息
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                stringBuffer.append(entry.getKey())
                        .append("=")
                        .append(URLEncoder.encode(entry.getValue(), encode))
                        .append("&");
            }
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);    //删除最后的一个"&"
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuffer;
    }


    public static void toUploadImgFile(File file, String fileKey, String RequestURL,
                                Map<String, String> param, OnUploadProcessListener onUploadProcessListener) {
        File currentFile = createAndSaveScaledBitmap(file, 720, 1080);//1080, 1920);
        LogUtils.d("2424 currentFile_name=%s", currentFile.getName());

        toUploadFile(currentFile, fileKey, RequestURL, param, onUploadProcessListener);

        if (!currentFile.getName().equals(file.getName()) && currentFile.exists()) {
            currentFile.delete();
        }
    }


    public static void toUploadFile(File file, String fileKey, String RequestURL,
                             Map<String, String> param, OnUploadProcessListener onUploadProcessListener) {
        LogUtils.d("#### toUploadFile url=%s", RequestURL);

        String result = null;
        requestTime = 0;

//        long requestTime = System.currentTimeMillis();
//        long responseTime = 0;

        HttpURLConnection conn = null;
        try {
            URL url = WebUtils.createURL(RequestURL);//new URL(RequestURL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(readTimeOut);
            conn.setConnectTimeout(connectTimeout);
            conn.setDoInput(true); // 允许输入流
            conn.setDoOutput(true); // 允许输出流
            conn.setUseCaches(false); // 不允许使用缓存
            conn.setRequestMethod("POST"); // 请求方式
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

            conn.setRequestProperty("Charset", CHARSET); // 设置编码
            conn.setRequestProperty("connection", "keep-alive");
//      conn.setRequestProperty("user-agent",
//       "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
//       conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            /**
             * 当文件不为空，把文件包装并且上传
             */
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            StringBuffer sb = null;
            String params = "";

            if (param != null && param.size() > 0) {
                Iterator<String> it = param.keySet().iterator();
                while (it.hasNext()) {
                    sb = null;
                    sb = new StringBuffer();
                    String key = it.next();
                    String value = param.get(key);
                    sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
                    sb.append("Content-Disposition: form-data; name=\"").append(key).append("\"")
                            .append(LINE_END).append(LINE_END);
                    sb.append(value).append(LINE_END);
                    params = sb.toString();
                    dos.write(params.getBytes());
                    // dos.flush();
                }
            }

            sb = null;
            params = null;
            sb = new StringBuffer();

            String imgType = ImageTypeUtil.getFileType(file.getPath());
            LogUtils.d("v3232 imgType = %s lis %s", imgType, onUploadProcessListener);

            sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
            sb.append("Content-Disposition:form-data; name=\"" + fileKey + "\"; filename=\""
                    + file.getName() + "\"" + LINE_END);
//            sb.append("Content-Type:image/png" + LINE_END); // 这里配置的Content-type很重要的 ，用于服务器端辨别文件的类型的
            sb.append("Content-Type:image/" + imgType + LINE_END); // 这里配置的Content-type很重要的 ，用于服务器端辨别文件的类型的
            sb.append(LINE_END);
            params = sb.toString();
            sb = null;

            dos.write(params.getBytes());

            InputStream is = new FileInputStream(file);
            if (onUploadProcessListener != null)
                onUploadProcessListener.initUpload((int) file.length());
            byte[] bytes = new byte[1024];
            int len = 0;
            int curLen = 0;
            while ((len = is.read(bytes)) != -1) {
                curLen += len;
                dos.write(bytes, 0, len);
                if (onUploadProcessListener != null)
                    onUploadProcessListener.onUploadProcess(curLen);
            }
            is.close();

            dos.write(LINE_END.getBytes());
            byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
            dos.write(end_data);
            dos.flush();
            dos.close();

            int res = conn.getResponseCode();
//            responseTime = System.currentTimeMillis();
//            this.requestTime = (int) ((responseTime - requestTime) / 1000);
            if (res == HttpURLConnection.HTTP_OK) {
                // Log.e(TAG, "request success");
                InputStream is1 = conn.getInputStream();
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len1 = -1;
                while ((len1 = is1.read(buffer)) != -1) {
                    os.write(buffer, 0, len1);
                    //Log.d(">>>", "####" + buffer.toString());
                }

                is1.close();
                result = os.toString();// 把流中的数据转换成字符串,采用的编码是utf-8(模拟器默认编码)
                LogUtils.d("2424 response code:%s; result = %s; result.length = %s; os.size=%s",
                        res, result, result.length(), os.size());
                os.close();
                if (onUploadProcessListener != null)
                    onUploadProcessListener.onUploadDone(UPLOAD_SUCCESS_CODE, result);
                return;
            } else {
                // Log.e(TAG, "request error");
                if (onUploadProcessListener != null)
                    onUploadProcessListener.onUploadDone(UPLOAD_SERVER_ERROR_CODE, "failed: code=" + res);
                return;
            }
        } catch (MalformedURLException e) {
            if (onUploadProcessListener != null)
                onUploadProcessListener.onUploadDone(UPLOAD_SERVER_ERROR_CODE,
                        "failed MalformedURLException：error=" + e.getMessage());
            e.printStackTrace();
            return;
        } catch (IOException e) {
            if (onUploadProcessListener != null)
                onUploadProcessListener.onUploadDone(UPLOAD_SERVER_ERROR_CODE,
                        "上传失败IOException：error=" + e.getMessage());
            e.printStackTrace();
            return;
        } finally {
            if (conn != null)
                conn.disconnect();
        }
    }


    /**
     * @author shimingzheng
     */
    public static interface OnUploadProcessListener {
        /**
         * 上传响应
         *
         * @param responseCode
         * @param message
         */
        void onUploadDone(int responseCode, String message);

        /**
         * 上传中
         *
         * @param uploadSize
         */
        void onUploadProcess(int uploadSize);

        /**
         * 准备上传
         *
         * @param fileSize
         */
        void initUpload(int fileSize);
    }

    // private OnUploadProcessListener onUploadProcessListener;


    // public void setOnUploadProcessListener(
    // OnUploadProcessListener onUploadProcessListener) {
    // this.onUploadProcessListener = onUploadProcessListener;
    // }

    public int getReadTimeOut() {
        return readTimeOut;
    }

    public void setReadTimeOut(int readTimeOut) {
        this.readTimeOut = readTimeOut;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    /**
     * 获取上传使用的时间
     *
     * @return
     */
    public static int getRequestTime() {
        return requestTime;
    }

    public static interface uploadProcessListener {

    }

    public static File createAndSaveScaledBitmap(File originFile, int maxWidth,
                                                 int maxHeight) {
        if (originFile == null || !originFile.exists() || maxWidth <= 0 || maxHeight <= 0)
            return originFile;

        Bitmap ret = null;
        double width = 0, height = 0;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(originFile.getAbsolutePath(), options);
            width = options.outWidth;
            height = options.outHeight;

//      LogUtils.d("#### width=%s; height=%s", width, height);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (width != 0 && height != 0) {
            if (width <= maxWidth || height <= maxHeight)
                return originFile;

            int sample = 1;

            if (width > maxWidth) {
                sample = (int) Math.ceil(width / maxWidth);
                height = height / sample;
            }

            if (height > maxHeight)
                sample += (int) Math.ceil(height / maxHeight);

//      LogUtils.d("#### sample=%s", sample);
            BitmapFactory.Options options = new BitmapFactory.Options();
            try {
                options.inSampleSize = sample;
                ret = BitmapFactory.decodeFile(originFile.getAbsolutePath(), options);
            } catch (OutOfMemoryError e) {
                options.inSampleSize = sample * 2;
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                try {
                    ret = BitmapFactory.decodeFile(originFile.getAbsolutePath(), options);
                } catch (OutOfMemoryError e1) {
                }
            }
        } else {
            return originFile;
        }

        if (ret.getWidth() > maxWidth || ret.getHeight() > maxHeight) {
            ret = ViewUtils.createScaledBitmap(ret, maxWidth, maxHeight);
        }

        if (ret == null)
            return originFile;

        File newFile = new File(originFile.getAbsolutePath() + "_copy");
        IOUtils.saveBitmap(ret, newFile, Bitmap.CompressFormat.JPEG, 85);

        if (newFile.exists()) {
            return newFile;
        }
        return originFile;
    }


    public static void uploadImage(File file, String fileKey, String RequestURL,
                                   Map<String, String> param, OnUploadProcessListener onUploadProcessListener) {
        LogUtils.d("#### uploadImage url=%s", RequestURL);
        File currentFile = createAndSaveScaledBitmap(file, 720, 1080);//1080, 1920);

        LogUtils.d("#### currentFile=%s", currentFile.getAbsolutePath());

        String result = null;
        int readTimeOut = 60 * 1000; // 读取超时
        int connectTimeout = 60 * 1000; // 超时时间

        HttpURLConnection conn = null;
        try {
            URL url = new URL(RequestURL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(readTimeOut);
            conn.setConnectTimeout(connectTimeout);
            conn.setDoInput(true); // 允许输入流
            conn.setDoOutput(true); // 允许输出流
            conn.setUseCaches(false); // 不允许使用缓存
            conn.setRequestMethod("POST"); // 请求方式
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

            conn.setRequestProperty("Charset", CHARSET); // 设置编码
            conn.setRequestProperty("connection", "keep-alive");
//      conn.setRequestProperty("user-agent",
//       "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
//       conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            /**
             * 当文件不为空，把文件包装并且上传
             */
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            StringBuffer sb = null;
            String params = "";

            if (param != null && param.size() > 0) {
                Iterator<String> it = param.keySet().iterator();
                while (it.hasNext()) {
                    sb = new StringBuffer();
                    String key = it.next();
                    String value = param.get(key);
                    sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
                    sb.append("Content-Disposition: form-data; name=\"").append(key).append("\"")
                            .append(LINE_END).append(LINE_END);
                    sb.append(value).append(LINE_END);
                    params = sb.toString();
                    dos.write(params.getBytes());
                    // dos.flush();
                }
            }
            sb = new StringBuffer();

            String imgType = ImageTypeUtil.getFileType(file.getPath());

            sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
            sb.append("Content-Disposition:form-data; name=\""
                    + fileKey + "\"; filename=\""
                    + currentFile.getName()
                    + "\"" + LINE_END);

            sb.append("Content-Type:image/" + imgType + LINE_END); // 这里配置的Content-type很重要的 ，用于服务器端辨别文件的类型的
            sb.append(LINE_END);
            params = sb.toString();

            dos.write(params.getBytes());

            InputStream is = new FileInputStream(currentFile);
            if (onUploadProcessListener != null) {
                onUploadProcessListener.initUpload((int) currentFile.length());
            }
            byte[] bytes = new byte[1024];
            int len = 0;
            int curLen = 0;
            while ((len = is.read(bytes)) != -1) {
                curLen += len;
                dos.write(bytes, 0, len);
                if (onUploadProcessListener != null)
                    onUploadProcessListener.onUploadProcess(curLen);
            }
            is.close();

            dos.write(LINE_END.getBytes());
            byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
            dos.write(end_data);
            dos.flush();
            dos.close();

            int res = conn.getResponseCode();
            if (res == HttpURLConnection.HTTP_OK) {
                // Log.e(TAG, "request success");
                InputStream is1 = conn.getInputStream();
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len1 = -1;
                while ((len1 = is1.read(buffer)) != -1) {
                    os.write(buffer, 0, len1);
                    //Log.d(">>>", "####" + buffer.toString());
                }

                is1.close();
                result = os.toString();// 把流中的数据转换成字符串,采用的编码是utf-8(模拟器默认编码)
                os.close();

                if (onUploadProcessListener != null)
                    onUploadProcessListener.onUploadDone(UPLOAD_SUCCESS_CODE, result);

                if (!currentFile.getName().equals(file.getName()) && currentFile.exists()) {
                    currentFile.delete();
                }
            } else {
                // Log.e(TAG, "request error");
                if (onUploadProcessListener != null)
                    onUploadProcessListener.onUploadDone(UPLOAD_SERVER_ERROR_CODE, "failed: code=" + res);
            }
        } catch (MalformedURLException e) {
            if (onUploadProcessListener != null)
                onUploadProcessListener.onUploadDone(UPLOAD_SERVER_ERROR_CODE,
                        "failed MalformedURLException：error=" + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            if (onUploadProcessListener != null)
                onUploadProcessListener.onUploadDone(UPLOAD_SERVER_ERROR_CODE,
                        "上传失败IOException：error=" + e.getMessage());
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            if (onUploadProcessListener != null)
                onUploadProcessListener.onUploadDone(UPLOAD_SERVER_ERROR_CODE,
                        "上传失败IOException：error=" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (conn != null)
                conn.disconnect();
        }
    }

    public static void uploadXXAssistantImage(String filePath, final OnUploadProcessListener listener){
        uploadFile(filePath, "img", "/upload/pic/ic-note", null,listener);
//        Bitmap bitmap = ViewUtils.getBitmapFromDisk(filePath, 1920);
//        if (bitmap != null) {
//            byte[] bytes = ViewUtils.bitmap2bytes(bitmap);
//            UrlRequest r = new UrlRequest("/upload/pic/ic-note");
//            r.addPostParam("img", bytes);
//            r.setDelegate(new RequestDelegate() {
//                @Override
//                public void requestFailed(UrlRequest request, int statusCode, String errorString) {
//                    if (listener != null){
//                        listener.onUploadDone(UPLOAD_SERVER_ERROR_CODE, null);
//                    }
//                }
//
//                @Override
//                public void requestFinished(UrlRequest request) {
//                    JSONObject ret = request.getResponseJsonObject();
//                    if (ret != null){
//                        String url = ret.getString("url");
//                        if (listener != null){
//                            listener.onUploadDone(UPLOAD_SUCCESS_CODE, url);
//                        }
//                        return;
//                    }
//                    if (listener != null){
//                        listener.onUploadDone(UPLOAD_SERVER_ERROR_CODE, null);
//                    }
//                }
//            });
//            r.start();
//        }else{
//            if (listener != null){
//                listener.onUploadDone(UPLOAD_FILE_NOT_EXISTS_CODE, null);
//            }
//        }
    }


    public static void uploadImageBase64(File file, String serverUrl, UploadPicFileUtils.OnUploadProcessListener listener) {
        int readTimeOut = 10 * 1000; // 读取超时
        int connectTimeout = 10 * 1000; // 超时时间

        HttpURLConnection conn = null;
        try {
            URL url = new URL(serverUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(readTimeOut);
            conn.setConnectTimeout(connectTimeout);
            conn.setDoInput(true); // 允许输入流
            conn.setDoOutput(true); // 允许输出流
            conn.setUseCaches(false); // 不允许使用缓存
            conn.setRequestMethod("POST"); // 请求方式
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

            conn.setRequestProperty("Content-Type", "text/plain" + ";boundary=" + BOUNDARY);
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Charset", CHARSET); // 设置编码

            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

            //封装payload
            String imgType = ImageTypeUtil.getFileType(file.getPath());
            String payload = "data:image/";
            payload += imgType + ";base64,";
            payload += fileBase64String(file.getAbsolutePath());
            dos.write(payload.getBytes());

            dos.write(LINE_END.getBytes());
            byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
            dos.write(end_data);
            dos.flush();
            dos.close();

            int res = conn.getResponseCode();
            if (res == HttpURLConnection.HTTP_OK) {
                // Log.e(TAG, "request success");
                InputStream is1 = conn.getInputStream();
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len1;
                while ((len1 = is1.read(buffer)) != -1) {
                    os.write(buffer, 0, len1);
                }

                is1.close();

                // 把流中的数据转换成字符串,采用的编码是utf-8(模拟器默认编码)
                String result = os.toString();
                os.close();

                if (listener != null) {
                    listener.onUploadDone(UPLOAD_SUCCESS_CODE, result);
                }

                if (file.exists()) {
                    file.delete();
                }
            } else {
                if (listener != null) {
                    listener.onUploadDone(UPLOAD_SERVER_ERROR_CODE, "failed: code=" + res);
                }
            }
        } catch (MalformedURLException e) {
            if (listener != null)
                listener.onUploadDone(UPLOAD_SERVER_ERROR_CODE, "failed MalformedURLException：error=" + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            if (listener != null) {
                listener.onUploadDone(UPLOAD_SERVER_ERROR_CODE, "上传失败IOException：error=" + e.getMessage());
            }
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            if (listener != null) {
                listener.onUploadDone(UPLOAD_SERVER_ERROR_CODE, "上传失败IOException：error=" + e.getMessage());
            }
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    /**
     * 图片文件转Base64字符串 * @param path 文件所在的绝对路径加文件名　 * @return
     */
    private static String fileBase64String(String path) {
        try {
            Bitmap ret = ViewUtils.getBitmapFromDisk(path, 1920);
            byte[] bytes = ViewUtils.bitmap2bytes(ret);
            ret.recycle();
//            FileInputStream fis = new FileInputStream(path);//转换成输入流
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            byte[] buffer = new byte[1024];
//            int count = 0;
//            while ((count = fis.read(buffer)) >= 0) {
//                baos.write(buffer, 0, count);//读取输入流并写入输出字节流中 }
//            }
//
//            //关闭文件输入流
//            fis.close();

            //进行Base64编码
            return new String(Base64.encodeToString(bytes, Base64.DEFAULT));
        } catch (Exception e) {
            return null;
        }
    }
}
