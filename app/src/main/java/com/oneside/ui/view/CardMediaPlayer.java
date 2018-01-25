package com.oneside.ui.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.oneside.R;
import com.oneside.utils.LangUtils;
import com.oneside.utils.ViewUtils;

import java.io.IOException;

/**
 * Created by fupingfu on 2017/8/16.
 */

public class CardMediaPlayer extends RelativeLayout implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnInfoListener, SurfaceHolder.Callback,
        View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private static final int MAX_PROGRESS = 100;

    private View mLayoutPlayer;
    private SurfaceView mVideoSurFaceView;
    private View mLayoutController;
    private ImageView mIvExtend;
    private ImageView mIvPlay;

    private ImageView mIvPlayer;

    private View mLayoutSeek;
    private SeekBar mSeekBar;
    private TextView mTvProgressTime;
    private TextView mTvMaxTime;

    private View mLayoutTitle;
    private ImageView mIvBack;
    private TextView mTvTitle;

    private MediaPlayer mMediaPlayer;

    /**
     * 目前仅在视频切换到后台和转回前台的时候使用
     */
    private int currentPosition;

    /**
     * 视频播放地址
     */
    private String mUrl;

    private String imgUrl;

    /**
     * 视频总长度
     */
    private int duration = 0;

    private boolean isLandscape = false;

    private long mCurrentTimes;
    private boolean hasPrepared = false;
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (hasPrepared) {
                return;
            }
            if (System.currentTimeMillis() - mCurrentTimes > 15 * 1000) {
                ViewUtils.showToast("网络连接超时", Toast.LENGTH_LONG);
            }
            postDelayed(mRunnable, 6000);
        }
    };

    private MediaState mState;

    public CardMediaPlayer(Context context) {
        super(context);
        init();
    }

    public CardMediaPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CardMediaPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.ui_card_media_player, this);
        mLayoutPlayer = findViewById(R.id.fl_player);
        mVideoSurFaceView = (SurfaceView) findViewById(R.id.video_surfaceview);
        mVideoSurFaceView.setOnClickListener(this);

        mLayoutController = findViewById(R.id.rl_controller);

        mIvExtend = (ImageView) findViewById(R.id.iv_extend);
        mIvExtend.setOnClickListener(this);

        mIvPlay = (ImageView) findViewById(R.id.iv_play);
        mIvPlay.setOnClickListener(this);
        mIvPlay.setClickable(false);

        mIvPlayer = (ImageView) findViewById(R.id.iv_media);

        mLayoutSeek = findViewById(R.id.ll_controller);
        mSeekBar = (SeekBar) findViewById(R.id.sb_progress);
        mSeekBar.setMax(MAX_PROGRESS);
        mSeekBar.setOnSeekBarChangeListener(this);

        mTvProgressTime = (TextView) findViewById(R.id.tv_progress_time);
        mTvMaxTime = (TextView) findViewById(R.id.tv_max_time);

        mLayoutTitle = findViewById(R.id.ll_title);
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mIvBack.setOnClickListener(this);

        SurfaceHolder mSurfaceHolder = mVideoSurFaceView.getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mSeekBar.setEnabled(false);
        mLayoutController.setVisibility(View.GONE);
    }

    public void initData(String url, String image) {
        mUrl = url;
        imgUrl = image;

        initPlayer();
    }

    /**
     * 初始化mediaPlayer
     */
    private void initPlayer() {
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
        }

        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnErrorListener(this);
        mMediaPlayer.setOnInfoListener(this);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnBufferingUpdateListener(this);

        mState = MediaState.INIT;
        setMediaPlayerState(mState);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mMediaPlayer.setDisplay(holder);
        switch (mState) {
            case PAUSE:
            case COMPLETE:
            case START:
                mState = MediaState.START;
                break;
            case ERROR:
                initPlayer();
                mState = MediaState.INIT;
                mLayoutController.setVisibility(View.GONE);
                showTitleLayer(false);
                break;
            default:
                break;
        }

        setMediaPlayerState(mState);

        mCurrentTimes = System.currentTimeMillis();
        postDelayed(mRunnable, 6000);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //stopMediaPlayer();
        mState = MediaState.PAUSE;
        currentPosition = mMediaPlayer.getCurrentPosition();

        setMediaPlayerState(mState);
        removeCallbacks(mRunnable);
    }

    private void setMediaPlayerState(MediaState state) {
        if (state == MediaState.INIT) {
            // 初始化MediaPlayer
            try {
                mMediaPlayer.setDataSource(mUrl);
                // 设置音频流的类型
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mMediaPlayer.prepareAsync();

                showFirstFrameImage();

            } catch (IOException e) {
                e.printStackTrace();

            } catch (IllegalStateException e) {
                mState = MediaState.ERROR;
                mMediaPlayer.reset();
                showTitleLayer(true);
                mLayoutController.setVisibility(View.VISIBLE);

            }
        } else if (state == MediaState.START) {
            // 播放视频
            mMediaPlayer.start();
            //当视频播放的时候不显示控制浮层
            mLayoutController.setVisibility(View.GONE);
            showTitleLayer(false);
        } else if (state == MediaState.PAUSE) {
            // 暂停视频
            mMediaPlayer.pause();
            mLayoutController.setVisibility(View.VISIBLE);
            showTitleLayer(true);
        }

        handleMsg(new Message());
    }

    public void handleMsg(Message msg) {
        if (mMediaPlayer == null || !mMediaPlayer.isPlaying()) {
            return;
        }

        int current = mMediaPlayer.getCurrentPosition();
        mTvProgressTime.setText(LangUtils.formatTime(current));
        mSeekBar.setProgress(current * MAX_PROGRESS / duration);
//        mHandler.sendEmptyMessageDelayed(0, 1000);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mIvPlay.setClickable(false);
        mIvPlay.setOnClickListener(this);
        mSeekBar.setEnabled(true);
        mVideoSurFaceView.setBackgroundColor(Color.TRANSPARENT);
        duration = mMediaPlayer.getDuration();
        mTvMaxTime.setText(LangUtils.formatTime(duration));
//            mHandler.sendEmptyMessageDelayed(0, 1000);

        mState = MediaState.START;
        setMediaPlayerState(mState);
    }

    private int mPercent = 0;
    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        if (percent == 100 && mState == MediaState.INIT) {
            return;
        }

        //防止进度条抖动
        if (percent > mPercent) {
            mPercent = percent;
            mSeekBar.setSecondaryProgress(percent);
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (mState == MediaState.START) {
            mState = MediaState.COMPLETE;
        }

        mSeekBar.setProgress(0);
        mTvProgressTime.setText("00:00:00");
        if (mMediaPlayer == null) {
            return;
        }

        mMediaPlayer.seekTo(0);
        mMediaPlayer.pause();
        mLayoutController.setVisibility(View.VISIBLE);
        showTitleLayer(true);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        if (mMediaPlayer != null) {
            mState = MediaState.ERROR;
            mMediaPlayer.reset();

            showTitleLayer(true);
            mLayoutController.setVisibility(View.VISIBLE);
        }

        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.video_surfaceview:
                if (mLayoutController.getVisibility() == View.GONE) {
                    showControlLayer();
                }
                break;
            case R.id.iv_extend:
                changePageOrientation(!isLandscape);
                break;
            case R.id.iv_play:
                if (mState == MediaState.ERROR) {
                    initPlayer();
                    mState = MediaState.INIT;
                } else {
                    mState = MediaState.START;
                }

                setMediaPlayerState(mState);
                break;
            case R.id.iv_back:
                if (isLandscape) {
                    changePageOrientation(false);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            isLandscape = true;
            showTitleLayer(mLayoutController.getVisibility() == View.VISIBLE);
            mIvExtend.setVisibility(View.GONE);
        } else {
            isLandscape = false;
            showTitleLayer(true);
            mIvExtend.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 切换横竖屏
     */
    private void changePageOrientation(boolean isLandscape) {
        if (isLandscape) {
            //切换竖屏
            mIvExtend.setVisibility(View.GONE);
        } else {
            mIvExtend.setVisibility(View.VISIBLE);
        }
        this.isLandscape = isLandscape;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        switch (what) {
            case MediaPlayer.MEDIA_INFO_UNKNOWN:
                break;
            case MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:
                break;
            case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                break;
            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                if (mState == MediaState.INIT) {
                    hasPrepared = true;
                }

                break;
            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                hasPrepared = true;
                closeFirstFrameImage();
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            mMediaPlayer.seekTo(progress * duration / MAX_PROGRESS);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mState = MediaState.PAUSE;
        setMediaPlayerState(mState);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int progress = mSeekBar.getProgress();
        mTvProgressTime.setText(LangUtils.formatTime(progress * duration / MAX_PROGRESS));
        mState = MediaState.START;
        setMediaPlayerState(mState);
    }

    /**
     * 显示第一帧图片，提高用户体验
     */
    private void showFirstFrameImage() {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!TextUtils.isEmpty(imgUrl) && !hasPrepared) {
                    mIvPlayer.setVisibility(View.VISIBLE);
//                    .getInstance().displayImage(imgUrl, mIvPlayer);
                }
            }
        }, 1800);
    }

    /**
     * 关闭第一帧
     */
    private void closeFirstFrameImage() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mIvPlayer, "alpha", 1, 0);
        animator.setDuration(80);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mIvPlayer.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }

    /**
     * 显示控制图层
     */
    private void showControlLayer() {
        boolean isShowing = mLayoutController.getVisibility() == View.VISIBLE;
        mLayoutController.setVisibility(isShowing ? View.GONE : View.VISIBLE);
        if (!isShowing) {
            mIvPlay.setImageResource(R.drawable.ic_media_play);
            mMediaPlayer.pause();
        } else {
            mMediaPlayer.start();
        }
        showTitleLayer(!isShowing);
    }

    /**
     * 显示进度条等
     *
     * @param showController 是否显示控制板
     */
    private void showTitleLayer(boolean showController) {
        RelativeLayout.LayoutParams params;
        if (isLandscape) {
            mLayoutTitle.setVisibility(showController ? View.VISIBLE : View.GONE);
            mLayoutSeek.setVisibility(showController ? View.VISIBLE : View.GONE);

            params = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            mLayoutPlayer.setPadding(0, 0, 0, 0);
            mLayoutPlayer.setLayoutParams(params);
        } else {
            mLayoutSeek.setVisibility(View.VISIBLE);
            mLayoutTitle.setVisibility(View.VISIBLE);

            params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewUtils.rp(290));
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            mLayoutPlayer.setPadding(0, 0, 0, ViewUtils.rp(60));
            mLayoutPlayer.setLayoutParams(params);
        }
    }

    protected void onDestroy() {
        mMediaPlayer.release();
        mMediaPlayer = null;
        mRunnable = null;
    }

    /**
     * 视频状态
     */
    private enum MediaState {
        INIT,
        START,
        PAUSE,
        ERROR,
        COMPLETE
    }
}
