package com.oneside.ui.news;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oneside.base.BaseActivity;
import com.oneside.base.inject.From;
import com.oneside.base.inject.InjectUtils;
import com.oneside.base.inject.XAnnotation;
import com.oneside.base.model.BasePageParam;
import com.oneside.utils.Constant;
import com.oneside.ui.view.CollapsibleTextViewMerchant;
import com.oneside.R;

/**
 * Created by MVEN on 16/5/9.
 */
public class StoryDetailActivity extends BaseActivity {
    @From(R.id.tv_content)
    private TextView tvContent;

    @XAnnotation
    private BasePageParam mPageParam;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_story_detail);


    }

    @Override
    public void onClick(View v) {
    }
}
