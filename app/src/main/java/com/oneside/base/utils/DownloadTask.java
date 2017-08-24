package com.oneside.base.utils;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidParameterException;

public class DownloadTask implements Runnable {
    public static final String TAG = DownloadTask.class.getSimpleName();

    public static final int DOWNLOAD_ONE = 10001;
    public static final int DOWNLOAD_LIST = 10002;

    public static final String DEFAULTPATH = Environment.getExternalStorageDirectory()
            .getAbsolutePath()
            + File.separator
            + "51talk"
            + File.separator + "PDF";
    public static final int TYPE_PDF = 0;//下载PDF
    public static final int TYPE_AUDIO = 1;//下载语音

    public static final int STATUS_NONE = 0;//
    public static final int STATUS_PENDDING = 1 << 0;
    public static final int STATUS_RUNNING = 1 << 1;
    public static final int STATUS_PAUSED = 1 << 2;
    public static final int STATUS_CANCELED = 1 << 3;
    public static final int STATUS_FINISHED = 1 << 4;
    public static final int STATUS_ERROR = 1 << 5;

    public int status = STATUS_NONE;//下载的状态
    public int type = TYPE_PDF;

    /**
     * 目标文件名，如：new_lesson.pdf
     */
    public String fileName;
    /**
     * 格式化(将空格替换)之后的下载地址
     */
    public String targetUrl;
    /**
     * 原始下载地址
     */
    public String url;//原始的下载地址
    public String mimeType;//文件类型(暂时未使用)

    public String dirPath = DEFAULTPATH;//文件保存位置
    /**
     * 最终目标文件，全路径.pdf
     */
    public File saveFile;//文件
    public String cacheSuffix = ".cache";// 临时文件后缀名称

    /**
     * 下载过程中使用的临时文件
     */
    public File targetFile;
    private Object extra;
    public int downloadType = DOWNLOAD_ONE;//是单个文件下载，还是多个文件连续下载

    public WeakReference<DownloadListener> callback;

    public void addCallBack(DownloadListener l) {
        callback = new WeakReference<DownloadListener>(l);
    }

    public void removeCallBack() {
        if(callback != null) {
            callback.clear();
        }
    }

    protected DownloadTask() {
        status = STATUS_PENDDING;
    }

    public boolean isRunning() {
        return status == DownloadTask.STATUS_RUNNING || status == DownloadTask.STATUS_PENDDING;
    }
    public void setExtra(Object obj) {
        extra = obj;
    }
    public Object getExtra() {
        return extra;
    }

    public File getTargetFile() {
        return targetFile;
    }
    public void setTargetFile(File f) {
        targetFile = f;
        if (f == null) {
            throw new InvalidParameterException("target file can not be null");
        }
    }

    /**
     * 空格替换成%20： 由于在地址中有空格的时候，本地处理存在问题，所以把空格替换成%20用做本地保存
     * @param downloadUrl
     * @return
     */
    public static String parseTargetUrl(String downloadUrl) {
        return downloadUrl.replace(" ", "%20");
    }

    /**
     * 4，根据文件的下载地址获取文件名
     * @param formattedUrl http://ac.51talk.com/text/FreeTalk.pdf
     * 1,formattedUrl 是以http://开头的 中间包含若干的‘/’并且是以（.扩展名）的形式结束的一个文件地址
     * 2,由于存在一种情况是：formattedUrl的文件名是相同的(FreeTalk)，但是所处的路径不同(/text)。
     * 所以在方法中会出现俩次的lastIndexOf和substring（textFreeTalk）。目的是为了避免上述情况出现
     * 3，taskName 我们约定以formattedUrl倒数第二个‘/’开始到（.扩展名）之间去掉中间‘/’作为本地保存的文件名称
     *   如果上述方法失败，则使用系统的当前时间(毫秒)
     * @return textFreeTalk
     */
    public static String buildTargetName(String formattedUrl) {
        int lastIndex = formattedUrl.lastIndexOf("/");
        String taskName = lastIndex <= 0 ? formattedUrl : formattedUrl.substring(0, lastIndex);

        int secondIndex = taskName.lastIndexOf("/");
        if(secondIndex != -1) {
            taskName = formattedUrl.substring(secondIndex + 1, formattedUrl.length()).replace("/","");
        } else {
            taskName = formattedUrl.substring(formattedUrl.lastIndexOf("/") + 1);
        }
//        String taskName = formattedUrl.substring(formattedUrl.lastIndexOf("/") + 1, formattedUrl.length());
        Log.d(TAG, "buildTargetName taskName="+taskName);
        int lastPointIndex = taskName.lastIndexOf(".");
        taskName = lastPointIndex != -1 ? taskName.substring(0, lastPointIndex) : String.valueOf(System.currentTimeMillis());
        return taskName;
    }

    /**
     * 生成最终下载完成后保存的文件
     * @param path
     * @param fileName
     * @param filefix
     * @return
     */
    public static File createTargetFile(String path, String fileName, String filefix) {
        return createTargetFile(path, fileName, filefix, true);
    }

    public static File createTargetFile(String path, String fileName, String filefix, boolean remove) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File newFile = new File(dir, fileName + "." +filefix);
        double fileOrFilesSize = FileSizeUtil.getFileOrFilesSize(newFile.getAbsolutePath(), 2);
        if (remove && fileOrFilesSize < 11)
            newFile.delete();
        return newFile;
    }

    /**
     * 暂时保留的方法
     * 获取文件扩展名
     * 由于现在项目中的所有下载相关都是带文件扩展名的，所以此方法目前是可靠的
     * @param url 地址
     * @return
     */
    public static String getUrlExtension(String url) {
        return url == null ? "" : url.substring(url.lastIndexOf(".") + 1);
    }

    /**
     * 构建一个downloadTask
     * @param downUrl
     * @param dirPath
     * @return
     */
    public static DownloadTask buildTask(String downUrl, String dirPath, DownloadListener l) {
        DownloadTask task = new DownloadTask();
        task.url = downUrl;
        task.targetUrl = DownloadTask.parseTargetUrl(downUrl);
        task.fileName = DownloadTask.buildTargetName(task.targetUrl);
        if (!TextUtils.isEmpty(dirPath)) {
            task.dirPath = dirPath;
        }
        task.saveFile = DownloadTask.createTargetFile(task.dirPath, task.fileName, DownloadTask.getUrlExtension(task.targetUrl));
        if(l != null) {
            task.addCallBack(l);
        }
        return task;
    }

    /***********************下载相关start******************/
    private static final long REFRESH_INTEVAL_SIZE = 100 * 1024;
    private volatile boolean pauseFlag;
    private volatile boolean stopFlag;
    private int tryTimes;//当前重试的次数
    public static final int RETRY_TIME = 3;//最大重试的次数

    @Override
    public void run() {
        do {
            try {
                doDownload();
                if(totalSize <= 0) {
                    DownloadManager.getInstance().onDownloadFailed(this);
                    break;
                }
                if (stopFlag && !saveFile.exists()) {
                    DownloadManager.getInstance().onDownloadCanceled(this);
                } else {
                    if (type == DownloadTask.TYPE_PDF)
                        targetFile.renameTo(saveFile);
                    DownloadManager.getInstance().onDownloadSuccessed(this);
                }
                break;
            } catch (IOException e) {
                e.printStackTrace();
                if (tryTimes > RETRY_TIME) {
                    DownloadManager.getInstance().onDownloadFailed(this);
                    break;
                } else {
                    tryTimes++;
                    continue;
                }
            }
        } while (true);
    }

    /**
     * 需要下载的文件大小
     */
    private int totalSize = 0;

    /**
     * 下载方法
     * @throws IOException
     */
    private void doDownload() throws IOException {
        HttpURLConnection conn = getConnection();
        conn.connect();
        int downloadTotalSize = conn.getContentLength();
        totalSize = downloadTotalSize;
        if(downloadTotalSize <= 0) {
            return;
        }
        this.mimeType = conn.getContentType();
        if(type == TYPE_PDF) {
            targetFile = buildCacheFile(targetUrl, dirPath, cacheSuffix);
        } else {
            targetFile = getTargetFile();
        }
        RandomAccessFile raf = new RandomAccessFile(targetFile, "rw");
        this.status = DownloadTask.STATUS_RUNNING;
        InputStream is = conn.getInputStream();
        byte[] buffer = new byte[8192];
        int count = 0;
        long downloadSize = 0;
        long prevTime = System.currentTimeMillis();
        long achieveSize = downloadSize;
        while (!stopFlag && (count = is.read(buffer)) != -1) {
            while (pauseFlag) {
//                        manager.onDownloadPaused(task);
                synchronized (this) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
//                                manager.onDownloadResumed(task);
                    }
                }
            }
            raf.write(buffer, 0, count);
            downloadSize += count;
            long tempSize = downloadSize - achieveSize;
            if (tempSize > REFRESH_INTEVAL_SIZE) {
                long tempTime = System.currentTimeMillis() - prevTime;
//                long speed = tempSize * 1000 / tempTime;
                achieveSize = downloadSize;
                prevTime = System.currentTimeMillis();
                Log.d(TAG, "doDownload downloadTotalSize="+downloadTotalSize);
                DownloadManager.getInstance().updateDownloadTask(this, downloadTotalSize, downloadSize);
            }
        }
    }

    public static File buildCacheFile(String downloadUrl, String dirPath, String filefix) {
//        String fileName = FileUtil.getFileNameByUrl(downloadUrl);
//        String fileName = buildTargetName(downloadUrl);
        long fileName = System.currentTimeMillis();
        File downloadFolder = new File(dirPath);
        if (downloadFolder.isFile()) {
            downloadFolder.delete();
        }
        if (!downloadFolder.mkdirs() && !downloadFolder.isDirectory()) {
            return null;
        }
        File f = new File(dirPath, fileName + filefix);
        return f;
    }

    private HttpURLConnection getConnection() throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(targetUrl).openConnection();
        conn.setConnectTimeout(30000);
        conn.setReadTimeout(30000);
//        conn.setRequestMethod("GET");
        conn.setUseCaches(true);
//        if ((!(this instanceof AudioDownloadTask)) && this.downloadFinishedSize != 0) {
//            conn.setRequestProperty("Range", "bytes=" + downloadFinishedSize + "-");
//        }
        conn .setRequestProperty("Accept-Encoding", "identity");
        return conn;
    }

    /***********************下载相关end******************/

  /*  public void pauseDownload() {
        if (pauseFlag) {
            return;
        }
        pauseFlag = true;
    }*/

    public void resumeDownload() {
        if (!pauseFlag) {
            return;
        }
        pauseFlag = false;
        synchronized (this) {
            notify();
        }
    }

    public void cancelDownload() {
        stopFlag = true;
        resumeDownload();
    }

}
