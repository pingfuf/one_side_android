package com.oneside.base.utils;

public interface DownloadListener {
	
	void onDownloadStart(DownloadTask task);
	
	void onDownloadUpdated(DownloadTask task, long totalSize, long finishedSize, long trafficSpeed);

	void onDownloadCanceled(DownloadTask task);

	void onDownloadSuccess(DownloadTask task);

	void onDownloadFailed(DownloadTask task);

	void onDownloadExist(DownloadTask task);
}
