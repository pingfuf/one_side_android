package com.oneside.ui.user;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.oneside.R;
import com.oneside.base.BaseFragment;
import com.oneside.base.inject.From;
import com.oneside.base.inject.XAnnotation;
import com.oneside.base.hy.HyConfigActivity;
import com.oneside.base.rn.RNPageParam;
import com.oneside.base.rn.RNRootActivity;
import com.oneside.base.rn.RnActivity;
import com.oneside.base.rn.CardReactActivity;
import com.oneside.kotlin.ui.KotlinHomeActivity;
import com.oneside.kotlin.ui.KotlinStudyActivity;
import com.oneside.ui.study.StudyActivity;

import java.util.HashMap;

/**
 * Created by fupingfu on 2017/1/12.
 */
@XAnnotation(layoutId = R.layout.fragment_tab_user)
public class TabUserFragment extends BaseFragment {
    @From(R.id.btn_mock)
    private Button btnMock;

    @From(R.id.btn_study)
    private Button btnStudy;

    @From(R.id.btn_my_rn)
    private Button btnMyRn;

    @From(R.id.btn_rn)
    private Button btnRn;

    @From(R.id.btn_kotlin_rn)
    private Button btnKotlinRn;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btnMock.setOnClickListener(this);
        btnStudy.setOnClickListener(this);
        btnMyRn.setOnClickListener(this);
        btnRn.setOnClickListener(this);
        btnKotlinRn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        RNPageParam param = new RNPageParam();
        if(v == btnMock) {
            xStartActivity(HyConfigActivity.class);
        } else if (v == btnStudy) {
            xStartActivity(StudyActivity.class);
        } else if (v == btnMyRn) {
            param.scheme = "rn://android/test";
            param.params = new HashMap<>();
            param.params.put("username", "xiaoming");
            param.params.put("password", "xiao12234%3");
            xStartActivity(RNRootActivity.class, param);
        } else if (v == btnRn) {
            xStartActivity(RnActivity.class);
        } else if (v == btnKotlinRn) {
            xStartActivity(KotlinHomeActivity.class);
        }
    }
}
