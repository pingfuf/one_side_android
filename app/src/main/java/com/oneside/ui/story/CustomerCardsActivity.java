package com.oneside.ui.story;

import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oneside.base.inject.XAnnotation;
import com.oneside.base.model.BasePageParam;
import com.oneside.R;
import com.oneside.base.BaseActivity;
import com.oneside.utils.LogUtils;
import com.oneside.utils.ViewUtils;

public class CustomerCardsActivity extends BaseActivity {
    private static final int TITLE_HEIGHT = ViewUtils.rp(40);
    private TextView tvCardTitle;
    private TextView tvCourseTitle;

    private MembershipCardFragment mCardFragment;
    private CourseCardFragment mCourseFragment;

    @XAnnotation
    private CustomerCardPageParam mPageParam;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_customer_cards);
        initTitle();
        initUI();
    }

    private void initUI() {
        if (mPageParam.type == 0) {
            showMembershipCardFragment();
        } else {
            showCourseCardFragment();
        }
    }

    private void initTitle() {
        setTitle("", true);
        tvCardTitle = createTitleBar("会员卡");
        tvCourseTitle = createTitleBar("私教卡");
        mTitleBar.setViewTitle(tvCardTitle, tvCourseTitle);
        ViewUtils.setViewBorder(mTitleBar.getCenterContent(), getResources().getColor(R.color.title_red_color), 3);
        tvCardTitle.setOnClickListener(this);
        tvCourseTitle.setOnClickListener(this);
    }

    private TextView createTitleBar(String text) {
        TextView textView = new TextView(this);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(TITLE_HEIGHT * 2, TITLE_HEIGHT);
        textView.setLayoutParams(param);
        textView.setText(text);
        textView.setPadding(TITLE_HEIGHT / 4, 0, TITLE_HEIGHT / 4, 0);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        textView.setGravity(Gravity.CENTER);

        return textView;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v == tvCardTitle) {
            showMembershipCardFragment();
        } else if (v == tvCourseTitle) {
            showCourseCardFragment();
        }
    }

    private void changeTitleColor(TextView textView, int backgroundColor, int textColorId) {
        if (textView == null) {
            return;
        }

        ViewUtils.setViewBackground(textView, backgroundColor);
        textView.setTextColor(textColorId);
    }

    private void showMembershipCardFragment() {
        if (mCardFragment == null) {
            mCardFragment = new MembershipCardFragment();
            mCardFragment.setFragmentParam(mPageParam);
            LogUtils.e("CustomerCardsActivity gymId = %s", mPageParam.gymId);
        }
        showFragment(mCardFragment);
        hideFragment(mCourseFragment);
        changeTitleColor(tvCourseTitle, Color.TRANSPARENT, getResources().getColor(R.color.title_red_color));
        changeTitleColor(tvCardTitle, getResources().getColor(R.color.title_red_color), Color.WHITE);
    }

    private void showCourseCardFragment() {
        if (mCourseFragment == null) {
            mCourseFragment = new CourseCardFragment();
            mCourseFragment.setFragmentParam(mPageParam);
        }
        showFragment(mCourseFragment);
        hideFragment(mCardFragment);
        changeTitleColor(tvCardTitle, Color.TRANSPARENT, getResources().getColor(R.color.title_red_color));
        changeTitleColor(tvCourseTitle, getResources().getColor(R.color.title_red_color), Color.WHITE);
    }

    private void showFragment(Fragment fragment) {
        if (fragment == null || fragment.isVisible()) {
            return;
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (!fragment.isAdded()) {
            fragmentTransaction.add(R.id.rl_container, fragment);
        }

        fragmentTransaction.show(fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void hideFragment(Fragment fragment) {
        if (fragment == null || !fragment.isAdded() || fragment.isHidden()) {
            return;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    public static class CustomerCardPageParam extends BasePageParam {
        public int type;
    }
}
