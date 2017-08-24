package com.oneside.base.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.widget.Toast;

import com.oneside.utils.ViewUtils;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

/**
 * MediaPlayer管理类，用于播放音频文件，视频文件需要SurfaceView支持，本类无法完成
 * <p>
 * Created by fupingfu on 2017/6/20.
 */
public final class TalkPlayerManager implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnSeekCompleteListener {
    private static TalkPlayerManager instance;
    private MediaPlayer mPlayer;
    private ILPlayer mPlayerCallback;
    private String mUrl;

    private TalkPlayerManager() {

    }

    public static synchronized TalkPlayerManager getInstance() {
        if (instance == null) {
            instance = new TalkPlayerManager();
        }

        return instance;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (mPlayerCallback != null) {
            mPlayerCallback.stop();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        if (mPlayerCallback != null) {
            mPlayerCallback.stop();
        }
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if (mPlayerCallback != null) {
            mPlayerCallback.play(mp);
        }
        mPlayer.start();
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
        mPlayer.seekTo(0);
        mPlayer.pause();
        if (mPlayerCallback != null) {
            mPlayerCallback.pause();
        }
    }

    public synchronized void startMediaPlayer(Context context, String url, ILPlayer callback) {
        if (TextUtils.isEmpty(url)) {
            return;
        }

        mPlayerCallback = callback;
        if (!TextUtils.equals(mUrl, url)) {
            resetMediaPlayer();
            mUrl = url;
            String parseTargetUrl = DownloadTask.parseTargetUrl(url);
            try {
                mPlayer.setDataSource(parseTargetUrl);
                mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mPlayer.prepareAsync();
            } catch (IOException e) {
                ViewUtils.showToast("加载音频文件出错，请重新加载", Toast.LENGTH_LONG);
                if (mPlayerCallback != null) {
                    mPlayerCallback.stop();
                }
            }
        } else {
            mPlayer.start();
            if (mPlayerCallback != null) {
                mPlayerCallback.play(mPlayer);
            }
        }
    }

    public synchronized void pauseMediaPlayer() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.pause();
            if (mPlayerCallback != null) {
                mPlayerCallback.pause();
            }
        }
    }

    /**
     * 关闭播放器
     */
    public synchronized void stopMediaPlayer() {
        if (mPlayer != null) {
            try {
                mPlayer.stop();
                resetMediaPlayer();
                if (mPlayerCallback != null) {
                    mPlayerCallback.stop();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mUrl = "";
    }

    /**
     * 重置播放器
     */
    public synchronized void resetMediaPlayer() {
        if (mPlayer == null) {
            mPlayer = new MediaPlayer();
        }
        mPlayer.reset();
        mPlayer.setOnErrorListener(this);
        mPlayer.setOnPreparedListener(this);
        mPlayer.setOnCompletionListener(this);
        mUrl = "";
    }

    public synchronized void release() {
        if (mPlayer != null) {
            try {
                if (mPlayer.isPlaying()) {
                    mPlayer.stop();
                }
                mPlayer.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mPlayer = null;
        mPlayerCallback = null;
    }
}