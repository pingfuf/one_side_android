package com.oneside.ui.study;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.oneside.R;
import com.oneside.base.BaseActivity;
import com.oneside.base.inject.From;
import com.oneside.ui.study.ui.CanvasStudyView;
import com.oneside.ui.study.ui.VideoActivity;
import com.oneside.utils.LogUtils;

public class StudyActivity extends BaseActivity {
    @From(R.id.btn_test)
    private Button btnTest;

    @From(R.id.btn_canvas)
    private Button btnCanvas;

    @From(R.id.tv_temp)
    private TextView tvTemp;

    @From(R.id.canvas_view)
    private CanvasStudyView canvasStudyView;

    @From(R.id.btn_go_to_rx)
    private Button btnGotoRx;

    @From(R.id.btn_go_to_video)
    private Button btnGotoVideo;

    private float mProcess = 0.1f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);

        btnTest.setOnClickListener(this);
        btnCanvas.setOnClickListener(this);
        btnGotoRx.setOnClickListener(this);
        btnGotoVideo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (isFinishing()) {
            return;
        }

        if (v == btnTest) {
            canvasStudyView.startAnimation();
        } else if (v == btnCanvas) {
            canvasStudyView.setProcess(mProcess);
            mProcess = mProcess + 0.1f;
        } else if (v == btnGotoRx) {
            xStartActivity(RxStudyActivity.class);
        } else if (v == btnGotoVideo) {
            xStartActivity(VideoActivity.class);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.i("StudyActivity onDestroy");
    }
}
