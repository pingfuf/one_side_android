package com.oneside.ui.home;

import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.oneside.ui.QRCodeScanResultActivity;
import com.oneside.base.BaseFragment;
import com.oneside.base.QRScanBaseActivity;
import com.oneside.utils.Constant;
import com.oneside.utils.JumpCenter;
import com.oneside.utils.ViewUtils;
import com.oneside.R;
import com.oneside.zxing.camera.CameraManager;
import com.oneside.zxing.decoding.InactivityTimer;
import com.oneside.zxing.decoding.ScanningHandler;
import com.oneside.zxing.view.ViewfinderView;

import java.io.IOException;
import java.util.Vector;

import static com.oneside.utils.ViewUtils.find;

/**
 * Created by MVEN on 16/3/18.
 */
public class OpenDoorQRScanFragment extends BaseFragment implements SurfaceHolder.Callback {
    private ScanningHandler handler;
    private View layoutContent;
    private ViewfinderView viewfinderView;

    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutContent = inflater.inflate(R.layout.fragment_open_door_scan, container, false);
        initUI();
        initData();
        return layoutContent;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        if (isVisibleToUser) {

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        SurfaceView surfaceView = find(layoutContent, R.id.open_door_preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getActivity().getSystemService(getActivity().AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    public void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    /**
     * UI
     */
    private void initUI() {

        CameraManager.init(getActivity().getApplication());
        viewfinderView = find(layoutContent, R.id.open_door_viewfinder_view);

        hasSurface = false;

    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new ScanningHandler((QRScanBaseActivity) getActivity(), decodeFormats, characterSet);
        }
    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }

    /**
     * DATA
     */
    private void initData() {
        hasSurface = false;
        inactivityTimer = new InactivityTimer(getActivity());
    }


    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        String resultString = result.getText();
        if (resultString.equals("")) {
            ViewUtils.showToast("Scan failed!", Toast.LENGTH_SHORT);
        } else {
            Bundle bundle = new Bundle();
            bundle.putString(Constant.SCAN_QR_CODE_RESULT, resultString);
            JumpCenter.Jump2Activity(getActivity(), QRCodeScanResultActivity.class, -1,
                    bundle);
        }
        getActivity().finish();
    }

    /*************
     * NET
     *************/


    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
            try {
                mediaPlayer
                        .setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getActivity().getSystemService(getActivity().VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final MediaPlayer.OnCompletionListener beepListener = new MediaPlayer.OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };
}
