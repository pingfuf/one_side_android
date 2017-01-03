package com.oneside.ui.course;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oneside.base.BaseActivity;
import com.oneside.base.inject.From;
import com.oneside.base.inject.XAnnotation;
import com.oneside.base.model.BasePageParam;
import com.oneside.base.net.UrlRequest;
import com.oneside.base.net.XService;
import com.oneside.base.net.model.BaseRequestParam;
import com.oneside.base.net.model.BaseResult;
import com.oneside.base.utils.BusinessStateHelper;
import com.oneside.base.view.XRoundImageView;
import com.oneside.manager.CardManager;
import com.oneside.manager.CardSessionManager;
import com.oneside.R;
import com.oneside.model.beans.CoachCourse;
import com.oneside.model.beans.XMember;
import com.oneside.model.event.CourseDraftChangedEvent;
import com.oneside.model.response.CoachCourseDetailResponse;
import com.oneside.model.response.CoachCourseListResponse;
import com.oneside.model.response.CoachPersonalCardsResponse;
import com.oneside.ui.view.XListView;
import com.oneside.utils.Constant;
import com.oneside.utils.LangUtils;
import com.oneside.utils.ViewUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 私教课程页面
 * <p/>
 * Created by pingfu on 16-9-8.
 */
public class CoachPersonalCourseActivity extends BaseActivity
        implements XListView.IXListViewListener, CoachCourseAdapter.OnCourseItemClickHandler {
    private static final int ADD_COURSE_PAGE_CODE = 101;
    private static final int COURSE_DETAIL_CODE = 102;
    @From(R.id.iv_header)
    private XRoundImageView ivHeader;

    @From(R.id.tv_coach_name)
    private TextView tvCoachName;

    @From(R.id.ll_add_action)
    private LinearLayout llAddAction;

    @From(R.id.ll_add_course)
    private LinearLayout llAddCourse;

    @From(R.id.tv_add)
    private TextView tvAddCourse;

    LinearLayout llRight;
    private TextView tvCourses;
    private ImageView ivWarn;

    @From(R.id.lv_items)
    private XListView lvItems;

    private CoachCourseAdapter mAdapter;
    private List<CoachCourseListResponse.CoachCourseItem> mItems;
    private BusinessStateHelper mStateHelper;

    private Date mDate;
    private boolean isReset;

    private CoachCourseDetailResponse mDetailResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_personal_course_desc);
        llRight = new LinearLayout(this);
        tvCourses = ViewUtils.createTitleBarRightTextView(this, "课程记录", R.color.text_color_gray_33);
        ivWarn = new ImageView(this);
        ivWarn.setVisibility(View.GONE);
        ivWarn.setImageResource(R.mipmap.ic_warn);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        llRight.setLayoutParams(params);
        tvCourses.setPadding(0, 0, 0, 0);
        llRight.addView(tvCourses);
        llRight.addView(ivWarn, params);
        llRight.setOnClickListener(this);
        llRight.setPadding(0, 0, ViewUtils.rp(10), 0);
        setTitle("私教课程", true, llRight);

        initUI();

        isReset = true;
        mDate = new Date();
        fetchCoachCourses();
        mStateHelper.setState(BusinessStateHelper.BusinessState.LOADING);

        mDetailResponse = CardManager.getFailedCourseDetail();
        EventBus.getDefault().register(this);
    }

    private void initUI() {
        ivHeader.setImageUri(CardSessionManager.getInstance().getUser().avatar);
        tvCoachName.setText(CardSessionManager.getInstance().getUser().name);

        llAddAction.setOnClickListener(this);

        ViewUtils.setViewBorder(llAddCourse, getResources().getColor(R.color.title_red_color),
                ViewUtils.rp(1), ViewUtils.rp(4));

        mItems = new ArrayList<>();
        mAdapter = new CoachCourseAdapter(this, mItems);
        mAdapter.setItemClickHandler(this);
        lvItems.setAdapter(mAdapter);
        lvItems.setXListViewListener(this);
        lvItems.getHeaderView().setBackgroundColor(getResources().getColor(R.color.white));

        mStateHelper = BusinessStateHelper.build(this, lvItems);
        mStateHelper.setNoDataImage(R.mipmap.coach_course_no_data_icon);
        mStateHelper.setNoDataMessage("暂无私教课");
        mStateHelper.setNoDataViewBackgroundColor(R.color.white);
        mStateHelper.setNetErrorRetryListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchCoachCourses();
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        if (v == llRight) {
            xStartActivity(CoachCourseListActivity.class);
        } else if (v == llAddAction) {
            CoachAddCourseActivity.AddCoursePageParam pageParam = new CoachAddCourseActivity.AddCoursePageParam();
            pageParam.type = CoachAddCourseActivity.ADD_TYPE;
            xStartActivity(CoachAddCourseActivity.class, pageParam, ADD_COURSE_PAGE_CODE);
        }
    }

    private void fetchCoachCourses() {
        BaseRequestParam param = new BaseRequestParam();
        param.addParam("coach_id", CardSessionManager.getInstance().getUser().id);
        param.addParam("order_by", "asc");
        param.addParam("day_size", 7);

        if(mDate == null) {
            mDate = new Date();
        }

        param.addParam("qstart_date", LangUtils.formatDate(mDate, "yyyy-MM-dd"));
        startRequest(XService.CoachCourseList, param);
    }

    @Override
    public void onRefresh() {
        mDate = new Date();
        isReset = true;
        fetchCoachCourses();
    }

    @Override
    public void onLoadMore() {
        isReset = false;
        fetchCoachCourses();
    }

    @Override
    protected void onNetError(UrlRequest request, int statusCode) {
        super.onNetError(request, statusCode);
        setState(BusinessStateHelper.BusinessState.NET_ERROR);
    }

    @Override
    protected void onResponseError(UrlRequest request, int code, String message) {
        super.onResponseError(request, code, message);
        setState(BusinessStateHelper.BusinessState.NET_ERROR);
    }

    @Override
    protected void onResponseSuccess(UrlRequest request, BaseResult data) {
        super.onResponseSuccess(request, data);
        setState(BusinessStateHelper.BusinessState.SUCCESS);
        refreshListView((CoachCourseListResponse) data);
        if (isReset){
            lvItems.stopRefresh();
        }else{
            lvItems.stopLoadMore();
        }
    }

    @Override
    public void onItemClick(int position) {
        if (mItems == null || mItems.size() <= position) {
            return;
        }
        CoachPersonalCourseDetailActivity.CoachPersonalCourseDetailPageParam pageParam =
                new CoachPersonalCourseDetailActivity.CoachPersonalCourseDetailPageParam();
        CoachCourseListResponse.CoachCourseItem item = mItems.get(position);
        if (item == null) {
            return;
        }
        pageParam.member = item.member;
        pageParam.orderId = item.orderId;
        pageParam.arrangementId = item.id;
        xStartActivity(CoachPersonalCourseDetailActivity.class, pageParam, COURSE_DETAIL_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == ADD_COURSE_PAGE_CODE) {
                mDate = new Date();
                isReset = true;
                fetchCoachCourses();
            } else if (requestCode == COURSE_DETAIL_CODE) {
                mDate = new Date();
                isReset = true;
                fetchCoachCourses();
            }
        }
    }

    private void refreshListView(CoachCourseListResponse response) {
        if (response == null) {
            return;
        }

        if (isReset) {
            mItems.clear();
        }
        mItems.addAll(response.items);

        boolean flag = false;
        for (int i = 0; i < mItems.size(); i++) {
            CoachCourseListResponse.CoachCourseItem item = mItems.get(i);
            if (item == null) {
                continue;
            }

            if (mDetailResponse != null && item.id == mDetailResponse.id) {
                item.isUploadFailed = true;
                flag = true;
            }

            if (i == 0) {
                item.showDate = true;
            } else {
                CoachCourseListResponse.CoachCourseItem item1 = mItems.get(i - 1);
                if (item1 != null) {
                    item.showDate = !LangUtils.isSameDay(item1.date, item.date);
                } else {
                    item.showDate = true;
                }
            }
        }

        if (mDetailResponse != null && !flag) {
            ivWarn.setVisibility(View.VISIBLE);
        } else {
            ivWarn.setVisibility(View.GONE);
        }

        if (LangUtils.isEmpty(mItems)) {
            setState(BusinessStateHelper.BusinessState.NO_DATA);
            mStateHelper.showCourseItem();
        }

        mDate = response.nextDay;
        mAdapter.notifyDataSetChanged();
        lvItems.setPullLoadEnable(response.hasMore);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCoachCourseUploadChanged(CourseDraftChangedEvent event) {
        mDetailResponse = CardManager.getFailedCourseDetail();
        if (mDetailResponse == null) {
            ivWarn.setVisibility(View.GONE);
            for (CoachCourseListResponse.CoachCourseItem item : mAdapter.getData()) {
                if (item != null && item.isUploadFailed) {
                    item.isUploadFailed = false;
                    mAdapter.notifyDataSetChanged();
                    break;
                }
            }
        } else {
            boolean flag = false;
            for (CoachCourseListResponse.CoachCourseItem item : mAdapter.getData()) {
                if (item != null && item.id == mDetailResponse.id) {
                    item.isUploadFailed = true;
                    flag = true;
                    mAdapter.notifyDataSetChanged();
                    break;
                }
            }

            ivWarn.setVisibility(flag ? View.GONE : View.VISIBLE);
        }
    }

    private void setState(final BusinessStateHelper.BusinessState state) {
        ViewUtils.postDelayed(new Runnable() {
            @Override
            public void run() {
                mStateHelper.setState(state);
            }
        }, 500);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
