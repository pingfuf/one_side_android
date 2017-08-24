package com.oneside.ui.study.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.BaseAdapter;

import com.oneside.R;
import com.oneside.base.BaseActivity;
import com.oneside.base.inject.From;
import com.oneside.ui.view.CardMediaPlayer;

import java.util.ArrayList;

public class VideoActivity extends BaseActivity {
    @From(R.id.v_media)
    CardMediaPlayer mPlayer;

    private ArrayList<String> mUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        String url = "";

        mUrls = new ArrayList<>();
        mUrls.add("http://ac.51talk.com/apk_img/brand/video1.mp4");
        mUrls.add("http://ac.51talk.com/apk_img/brand/video2.mp4");
        mUrls.add("http://ac.51talk.com/apk_img/brand/video3.mp4");
        mUrls.add("http://ac.51talk.com/apk_img/brand/video4.mp4");
        mPlayer.initData(mUrls.get(0), "lalal");
    }
}
