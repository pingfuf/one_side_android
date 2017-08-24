package com.oneside.base.utils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 说明：
 * @author 51talk
 * @version 创建时间：2016-2-18  下午3:44:03
 */
public class DownloadManager {
    private static DownloadManager instance;

    private HashMap<String, DownloadTask> downloadTasks = new HashMap<String, DownloadTask>();

    private ExecutorService pool;
    public static final int MAX_DOWNLOAD_THREAD = 3;//

    private DownloadManager() {
        if (pool == null) {
            pool = Executors.newFixedThreadPool(MAX_DOWNLOAD_THREAD);
        }
    }

    public static DownloadManager getInstance() {
        if (instance == null) {
            synchronized (DownloadManager.class) {
                if (instance == null) {
                    instance = new DownloadManager();
                }
            }
        }
        return instance;
    }

    public DownloadTask buildDownLoadTask(String downUrl, DownloadListener listener) {
        return buildDownLoadTask(downUrl, listener, null);
    }

    /**
     * @param downUrl
     * @param listener
     */
    public DownloadTask buildDownLoadTask(String downUrl, DownloadListener listener, String dirPath) {
        if (TextUtils.isEmpty(downUrl)) {
            if(listener != null) {
                listener.onDownloadFailed(null);
            }
            return null;
        }
        DownloadTask task = DownloadTask.buildTask(downUrl, dirPath, listener);
        if (task.saveFile.exists()) {
            listener.onDownloadExist(task);
            return task;
        }
        addDownloadTask(task, listener);
        return task;
    }

    /**
     * 加载语音的时候调用的方法
     *
     * @param task
     * @param listener
     */
    public void addDownloadTask(DownloadTask task, DownloadListener listener) {
        String downloadUrl = task.url;
        if (downloadTasks.containsKey(downloadUrl)) {
            cancelDownload(downloadUrl, null);
            downloadTasks.remove(downloadUrl);
        }
        addDownloadCallback(task, listener);
        downloadTasks.put(downloadUrl, task);
        task.status = DownloadTask.STATUS_PENDDING;
        if(task.targetFile != null)task.targetFile.delete();
        pool.submit(task);
    }

    private void addDownloadCallback(DownloadTask task, DownloadListener listener) {
        String downUrl = task.url;
        if(!TextUtils.isEmpty(downUrl)) {
            task.status = DownloadTask.STATUS_RUNNING;
            if (listener != null) {
                listener.onDownloadStart(task);
            }
        }
    }

    public void cancelDownload(String downloadUrl, DownloadListener listener) {
        if(!TextUtils.isEmpty(downloadUrl)) {
            DownloadTask task = downloadTasks.get(downloadUrl);
            downloadTasks.remove(downloadUrl);
            if(task != null && listener != null) {
                listener.onDownloadCanceled(task);
            }
            if (task != null) {
                task.removeCallBack();
                task.status = DownloadTask.STATUS_CANCELED;
                task.cancelDownload();
            }
        }
    }

    public void updateDownloadTask(DownloadTask task, long toalSize, long finishedSize) {
        Message msg = Message.obtain();
        msg.what = UPDATE;
        msg.obj = task;
        Bundle data = new Bundle();
        data.putLong("toalSize", toalSize);
        data.putLong("finishedSize", finishedSize);
        msg.setData(data);
        mDownloadTaskHandler.sendMessage(msg);
    }

    /**
     * 通知listener 的 onDownloadCanceled回调
     * @param task
     */
    public void onDownloadCanceled(DownloadTask task) {
        Message msg = Message.obtain();
        msg.what = CANCEL;
        msg.obj = task;
        mDownloadTaskHandler.sendMessage(msg);
    }

    public void onDownloadSuccessed(DownloadTask task) {
        Message msg = Message.obtain();
        msg.what = SUCCESS;
        msg.obj = task;
        mDownloadTaskHandler.sendMessage(msg);
    }

    public void onDownloadFailed(DownloadTask task) {
        Message msg = Message.obtain();
        msg.what = FAIL;
        msg.obj = task;
        mDownloadTaskHandler.sendMessage(msg);
    }

    private static final int SUCCESS = 0;
    private static final int FAIL = 1;
    private static final int CANCEL = 2;
    private static final int UPDATE = 3;


    private final Handler mDownloadTaskHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            DownloadTask task = (DownloadTask) msg.obj;
            String downloadUrl = task.url;
            DownloadListener l = null;
            if(task.callback != null) {
                l = task.callback.get();
            }
            switch (msg.what) {
                case SUCCESS:
                    task.status = DownloadTask.STATUS_FINISHED;
                    downloadTasks.remove(downloadUrl);
                    if(l != null) {
                        l.onDownloadSuccess(task);
                    }
                    break;
                case FAIL:
                    task.status = DownloadTask.STATUS_ERROR;
                    downloadTasks.remove(downloadUrl);
                    if(l != null) {
                        l.onDownloadFailed(task);
                    }
                    if(task.targetFile != null) {
                        task.targetFile.delete();
                    }
                    if(task.saveFile != null) {
                        task.saveFile.delete();
                    }
                    break;
                case CANCEL:
                    task.status = DownloadTask.STATUS_CANCELED;
                    downloadTasks.remove(downloadUrl);
                    if(l != null) {
                        l.onDownloadCanceled(task);
                    }
                    if(task.targetFile != null) {
                        task.targetFile.delete();
                    }
                    break;
                case UPDATE:
                    task.status = DownloadTask.STATUS_RUNNING;
                    Bundle data = msg.getData();
                    long toalSize = data.getLong("toalSize");
                    long finishedSize = data.getLong("finishedSize");
                    if(l != null) {
                        l.onDownloadUpdated(task, toalSize, finishedSize, 0);
                    }
                    break;

            }
        }
    };

    public boolean isFileExist(String downloadUrl, String dirPath) {
        String targetUrl = DownloadTask.parseTargetUrl(downloadUrl);
        String name = DownloadTask.buildTargetName(targetUrl);
        File f = DownloadTask.createTargetFile(dirPath, name, DownloadTask.getUrlExtension(downloadUrl));
        if (f != null) {
            return f.exists();
        }
        return false;
    }

    public void close() {
        pool.shutdownNow();
    }

    /**
     * 在横竖屏切换的时候，如果activity被销毁，需要重新给设置listener，此时调用这个方法
     * @param downloadUrl
     * @param listener
     */
    public void addCallBack(String downloadUrl, DownloadListener listener){
        if(TextUtils.isEmpty(downloadUrl))return;
        DownloadTask task = downloadTasks.get(downloadUrl);
        if(task != null)task.addCallBack(listener);
    }

    /**
     * 获取要下载的音频的数量
     * @return
     */
    public long getDownloadAudioSize(){
        int size = 0;
        if(downloadTasks == null){
            return size;
        }
        Iterator<Map.Entry<String, DownloadTask>> it = downloadTasks.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, DownloadTask> entry = it.next();
            DownloadTask downloadTask = entry.getValue();
            if(downloadTask.type == DownloadTask.TYPE_AUDIO){
                size++;

            }
        }
        return size;

    }
}