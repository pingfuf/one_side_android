package com.oneside.ui.story;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.widget.TextView;

import com.oneside.base.BaseActivity;
import com.oneside.base.inject.From;
import com.oneside.base.inject.XAnnotation;
import com.oneside.R;
import com.oneside.ui.home.ChosenGymAdapter;
import com.oneside.utils.ViewUtils;

/**
 * 扫码开门页面
 * <p/>
 * Created by MVEN on 16/3/18.
 */
public class JokeDetailActivity extends BaseActivity {
    @From(R.id.tv_content)
    private TextView tvContent;

    @XAnnotation
    private JokeDetailPageParam mPageParam;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_joke_detail);
        setTitle(mPageParam.title, true);

        tvContent.setText(Html.fromHtml(mPageParam.content));
    }
}
