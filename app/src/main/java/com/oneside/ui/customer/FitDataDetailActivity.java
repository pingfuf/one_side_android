package com.oneside.ui.customer;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.oneside.base.BaseActivity;
import com.oneside.ui.view.CircularProgressView;
import com.oneside.R;

import java.util.Date;

import static com.oneside.utils.LangUtils.cc_dateByMovingToBeginningOfDay;
import static com.oneside.utils.ViewUtils.find;
import static com.oneside.utils.WebUtils.getJsonArray;
import static com.oneside.utils.WebUtils.getJsonObject;

/**
 * Created by ZhanTao on 1/14/16.
 */
public class FitDataDetailActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout layoutBack;
    private LinearLayout layoutShare;

    private TextView tvTabTitleWeek;
    private TextView tvTabTitleMonth;
    public static final int DATE_RANGE_UNIT_OF_WEEK = 0;
    public static final int DATE_RANGE_UNIT_OF_MONTH = 1;
    private int mCurrentDateRangeUnit = DATE_RANGE_UNIT_OF_WEEK;

    private TextView tvCurrentDateRange;
    private TextView tvFitTime;
    private TextView tvFitEnergy;
    private TextView tvFitProject;
    private TextView tvFitEnergyChartMax;
    private ViewPager mViewPager;
    private ImageView ivLastPage;
    private ImageView ivNextPage;
    private CircularProgressView progressBar;

    public static final int MAX_VIEW_PAGER_COUNT = 8000;


    private volatile Date mStartDate;
    private volatile Date mEndDate;
    private volatile Integer mDateCount;


    private ListView mListView;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_fit_data_detail);

        initUI();

    }

    private void initUI() {
        layoutBack = (LinearLayout) findViewById(R.id.left_layout);
        layoutBack.setOnClickListener(this);

        layoutShare = (LinearLayout) findViewById(R.id.right_layout);
        layoutShare.setOnClickListener(this);

        tvTabTitleWeek = (TextView) findViewById(R.id.tv_week_title);
        tvTabTitleWeek.setOnClickListener(this);
        tvTabTitleMonth = (TextView) findViewById(R.id.tv_month_title);
        tvTabTitleMonth.setOnClickListener(this);

        tvCurrentDateRange = (TextView) findViewById(R.id.tv_fit_date_range);
        tvFitTime = (TextView) findViewById(R.id.tv_fit_time);
        tvFitEnergy = (TextView) findViewById(R.id.tv_fit_energy);
        tvFitProject = (TextView) findViewById(R.id.tv_fit_project);
        tvFitEnergyChartMax = (TextView) findViewById(R.id.tv_fit_energy_max);
        mViewPager = (ViewPager) findViewById(R.id.viewpager_fit_data_detail);
        mViewPager.setCurrentItem(MAX_VIEW_PAGER_COUNT / 2);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        ivLastPage = (ImageView) findViewById(R.id.iv_scroll_to_left);
        ivLastPage.setOnClickListener(this);
        ivNextPage = (ImageView) findViewById(R.id.iv_scroll_to_right);
        ivNextPage.setOnClickListener(this);

        mListView = (ListView) findViewById(R.id.listview_fit_data_detail);

        progressBar = (CircularProgressView) findViewById(R.id.progress_bar);
    }


}
