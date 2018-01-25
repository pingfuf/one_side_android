package com.oneside.base.utils;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;

import com.oneside.CardApplication;

/**
 * Created by fupingfu on 2017/12/12.
 */
public class DownloadUtil {
    private static final String FILE_NAME = "download.zip";
    private static String DOWNLOAD_PATH = "";

    public static void download(String url) {
        download(url, FILE_NAME);
    }

    public static void download(String url, String fileName) {
        //创建下载任务,downloadUrl就是下载链接
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

        //指定下载路径和下载文件名
        request.setDestinationInExternalPublicDir("/download/", fileName);

        //获取下载管理器
        DownloadManager downloadManager= (DownloadManager) CardApplication.getApplication().getSystemService(Context.DOWNLOAD_SERVICE);

        //将下载任务加入下载队列，否则不会进行下载
        downloadManager.enqueue(request);
    }

    public static void download(String url, String fileName, DownloadListener listener) {

    }


    public interface DownloadListener {
        void onStart();

        void onProcess(float process);

        void onFinished();

        void onError();
    }
}
