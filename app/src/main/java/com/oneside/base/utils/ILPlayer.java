package com.oneside.base.utils;

import android.media.MediaPlayer;

/**
 * 说明：MediaPlay播放状态执行的接口
 */
public interface ILPlayer {

	/**
	 * 正在缓存时，执行的方法
	 */
	void prepare();

	/**
	 * 当语音播放时,执行的方法
	 * @param mp 
	 */
	void play(MediaPlayer mp);
	
	/**
	 * 当语音停止时,执行的方法
	 */
	void stop();
	
	/**
	 * 当语音暂停时,执行的方法
	 */
	void pause();
}
