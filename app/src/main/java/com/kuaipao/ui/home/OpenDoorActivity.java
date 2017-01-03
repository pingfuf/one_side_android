package com.kuaipao.ui.home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.kuaipao.base.BaseActivity;
import com.kuaipao.base.adapter.XFragmentPageAdapter;
import com.kuaipao.base.inject.From;
import com.kuaipao.base.inject.XAnnotation;
import com.kuaipao.base.model.BasePageParam;
import com.kuaipao.base.net.UrlRequest;
import com.kuaipao.base.net.XService;
import com.kuaipao.base.net.model.BaseRequestParam;
import com.kuaipao.base.net.model.BaseResult;
import com.kuaipao.model.beans.XGym;
import com.kuaipao.model.response.UserGymStatusResponse;
import com.kuaipao.manager.R;
import com.kuaipao.utils.IOUtils;
import com.kuaipao.utils.LangUtils;
import com.kuaipao.utils.LogUtils;
import com.kuaipao.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 扫码开门页面
 * <p/>
 * Created by MVEN on 16/3/18.
 */
public class OpenDoorActivity extends BaseActivity {
    private static final String GYM_ID_KEY = "GYM_ID";

    public static final int IN_ACTION = 5;
    public static final int OUT_ACTION = 6;

    private static final int ITEM_HEIGHT = ViewUtils.rp(40);
    private static final int MAX_ITEM_SIZE = 7;
    private static final int TAB_NUM = 2;

    @From(R.id.vp_items)
    private ViewPager mViewPager;

    @From(R.id.tv_in)
    private TextView tvIn;

    @From(R.id.tv_out)
    private TextView tvOut;

    private TextView tvRight;

    private OpenDoorQRPicFragment openDoorFragment;
    private OpenDoorQRPicFragment leftDoorFragment;
    private ChosenGymAdapter mAdapter;

    @XAnnotation
    private OpenDoorPageParam mPageParam;

    private long mGymId;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_open_merchant_door);
        tvRight = ViewUtils.createTitleBarRightTextView(this, "切换场馆");
        tvRight.setTextColor(getResources().getColor(R.color.white));
        tvRight.setOnClickListener(this);
        setTitle("", true, tvRight);
        mTitleBar.setBackPressedImageResource(R.drawable.gym_card_manager_close_white);

        initUI();
    }


    @Override
    protected boolean isTitleBarOverlay() {
        return true;
    }

    private void initUI() {
        tvIn.setOnClickListener(this);
        tvOut.setOnClickListener(this);
        ArrayList<Fragment> fragments = new ArrayList<>(TAB_NUM);
        openDoorFragment = new OpenDoorQRPicFragment();
        fragments.add(openDoorFragment);
        leftDoorFragment = new OpenDoorQRPicFragment();
        fragments.add(leftDoorFragment);

        mViewPager.setOffscreenPageLimit(TAB_NUM);
        mViewPager.setAdapter(new XFragmentPageAdapter(fragments, getSupportFragmentManager()));
        mViewPager.setEnabled(false);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeUserStatus(position == 0);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if (mPageParam.mGyms != null && mPageParam.mGyms.size() == 1) {
            mGymId = mPageParam.mGyms.get(0).id;
            fetchUserState();
        } else {
            mGymId = LangUtils.parseLong(IOUtils.getPreferenceValue(GYM_ID_KEY), 0l);
            if (mGymId > 0) {
                fetchUserState();
            } else {
                showDialog(mPageParam.mGyms);
            }
        }
    }

    /**
     * 获取客户在场馆的状态，是否已经进店
     */
    private void fetchUserState() {
        BaseRequestParam param = new BaseRequestParam();
        param.addUrlParams(mGymId);
        startRequest(XService.UserGymStatus, param);
    }

    @Override
    protected void onResponseSuccess(UrlRequest request, BaseResult data) {
        super.onResponseSuccess(request, data);
        UserGymStatusResponse response = (UserGymStatusResponse) data;
        if (response != null) {
            if (response.isInGym) {
                mViewPager.setCurrentItem(1);
                fetchQrPic(1, mGymId);
            } else {
                mViewPager.setCurrentItem(0);
                fetchQrPic(0, mGymId);
            }

//            mViewPager.setCurrentItem(response.isInGym ? 1 : 0);
        } else {
            LogUtils.e("fetchUserState fail null");
        }

    }

    @Override
    protected void onResponseError(UrlRequest request, int code, String message) {
        super.onResponseError(request, code, message);
        mViewPager.setCurrentItem(0);
        LogUtils.e("fetchUserState fail");
    }

    @Override
    protected void onNetError(UrlRequest request, int statusCode) {
        super.onNetError(request, statusCode);
        mViewPager.setCurrentItem(0);
        LogUtils.e("fetchUserState fail net");
    }

    @Override
    public void onClick(View v) {
        if (v == tvIn) {
            mViewPager.setCurrentItem(0);
            changeUserStatus(true);
            fetchQrPic(0, mGymId);
        } else if (v == tvOut) {
            mViewPager.setCurrentItem(1);
            changeUserStatus(false);
            fetchQrPic(1, mGymId);
        } else if (v == tvRight) {
            showDialog(mPageParam.mGyms);
        }
    }

    /**
     * 更改状态，用户如果是进店状态，默认显示离开二维码；否则显示进店二维码
     *
     * @param isIn 用户是否是进店状态
     */
    private void changeUserStatus(boolean isIn) {
        if (isIn) {
            ViewUtils.setViewBackgroundDrawable(tvIn, R.drawable.circle_border_tv);
            tvIn.setTextColor(getResources().getColor(R.color.title_red_color));
            ViewUtils.setViewBackgroundDrawable(tvOut, R.drawable.circle_text_gray);
            tvOut.setTextColor(getResources().getColor(R.color.white));
        } else {
            ViewUtils.setViewBackgroundDrawable(tvIn, R.drawable.circle_text_gray);
            tvIn.setTextColor(getResources().getColor(R.color.white));
            ViewUtils.setViewBackgroundDrawable(tvOut, R.drawable.circle_border_tv);
            tvOut.setTextColor(getResources().getColor(R.color.title_red_color));
        }
    }

    /**
     * 刷新fragment二维码
     *
     * @param position fragment编号
     * @param gymId    选中的场馆id
     */
    private void fetchQrPic(int position, long gymId) {
        if (position == 0) {
            openDoorFragment.startFetchQrPic(IN_ACTION, gymId);
            leftDoorFragment.stopFetch();
        } else {
            leftDoorFragment.startFetchQrPic(OUT_ACTION, gymId);
            openDoorFragment.stopFetch();
        }
    }

    /**
     * 显示选择场馆对话框
     *
     * @param items 场馆列表
     */
    private void showDialog(List<XGym> items) {
        if (LangUtils.isEmpty(items)) {
            return;
        }
        final Dialog dialog = new AlertDialog.Builder(this, R.style.DialogTheme).create();
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
        dialogWindow.setWindowAnimations(R.style.shareDiaLogWindowAnim);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();

        dialog.setContentView(R.layout.ui_choose_gym);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialogWindow.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ListView llContent = (ListView) dialog.findViewById(R.id.ll_content);
        TextView tvCancel = (TextView) dialog.findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
        addItems(dialog, items, llContent);
    }

    private void addItems(final Dialog dialog, final List<XGym> items, ListView llContent) {
        if (LangUtils.isEmpty(items)) {
            return;
        }

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) llContent.getLayoutParams();
        if (items.size() > MAX_ITEM_SIZE) {
            params.height = MAX_ITEM_SIZE * ITEM_HEIGHT + ViewUtils.rp(MAX_ITEM_SIZE / 2);
        }
        llContent.setLayoutParams(params);

        int selectedPosition = -1;
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).id == mGymId) {
                selectedPosition = i;
                break;
            }
        }
        if (mAdapter == null) {
            mAdapter = new ChosenGymAdapter(this, items);
        }
        mAdapter.setSelectedPosition(selectedPosition);
        llContent.setAdapter(mAdapter);

        llContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isFinishing()) {
                    return;
                }
                dialog.dismiss();

                if (mGymId != items.get(position).id) {
                    mGymId = items.get(position).id;
                    fetchUserState();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (mGymId > 0) {
            IOUtils.savePreferenceValue(GYM_ID_KEY, String.valueOf(mGymId));
        }

        super.onDestroy();
    }

    /**
     * 扫码开门页面参数
     */
    public static class OpenDoorPageParam extends BasePageParam {
        public List<XGym> mGyms;
    }
}
