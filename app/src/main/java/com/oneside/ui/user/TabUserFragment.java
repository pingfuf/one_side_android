package com.oneside.ui.user;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.oneside.R;
import com.oneside.base.BaseFragment;
import com.oneside.base.inject.From;
import com.oneside.base.inject.XAnnotation;
import com.oneside.hy.HyConfigActivity;

/**
 * Created by fupingfu on 2017/1/12.
 */
@XAnnotation(layoutId = R.layout.fragment_tab_user)
public class TabUserFragment extends BaseFragment {
    @From(R.id.btn_mock)
    private Button btnMock;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btnMock.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        if(v == btnMock) {
            xStartActivity(HyConfigActivity.class);
        }
    }
}
