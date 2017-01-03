package com.oneside.ui.course;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.oneside.base.BaseActivity;
import com.oneside.base.inject.From;
import com.oneside.base.net.UrlRequest;
import com.oneside.base.net.XService;
import com.oneside.base.net.model.BaseRequestParam;
import com.oneside.base.net.model.BaseResult;
import com.oneside.base.utils.BusinessStateHelper;
import com.oneside.manager.CardManager;
import com.oneside.manager.CardSessionManager;
import com.oneside.model.event.CourseDraftChangedEvent;
import com.oneside.model.response.CoachCourseDetailResponse;
import com.oneside.model.response.CoachCourseListResponse;
import com.oneside.ui.view.XListView;
import com.oneside.utils.LangUtils;
import com.oneside.utils.ViewUtils;
import com.oneside.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 教练课程列表
 *
 * Created by Magi on 2015/12/2.
 */
public class CoachCourseListActivity extends BaseActivity
        implements XListView.IXListViewListener, CoachCourseHistoryAdapter.OnCourseItemClickHandler {
    private static final int COURSE_DETAIL_CODE = 102;
    @From(R.id.lv_items)
    private XListView lvItems;

    private CoachCourseHistoryAdapter mAdapter;
    private List<CoachCourseListResponse.CoachCourseItem> mItems;
    private BusinessStateHelper mStateHelper;
    private boolean isReset;
    private Date mDate;
    private CoachCourseDetailResponse mDetailResponse;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_coach_course_list);
        setTitle("课程记录", true);

        initUI();

        isReset = true;
        fetchCoachCourses();
        mStateHelper.setState(BusinessStateHelper.BusinessState.LOADING);
        mDetailResponse = CardManager.getFailedCourseDetail();
        EventBus.getDefault().register(this);
    }

    private void initUI() {
        mItems = new ArrayList<>();
        mAdapter = new CoachCourseHistoryAdapter(this, mItems);
        mAdapter.setItemClickHandler(this);
        lvItems.setAdapter(mAdapter);
        lvItems.setXListViewListener(this);
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                xStartActivity(CoachPersonalCourseDetailActivity.class);
            }
        });

        mStateHelper = BusinessStateHelper.build(this, lvItems);
        mStateHelper.setNoDataImage(R.mipmap.coach_course_no_data_icon);
        mStateHelper.setNoDataMessage("暂无课程记录");
        mStateHelper.setNetErrorRetryListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchCoachCourses();
            }
        });
    }

    private void fetchCoachCourses() {
        BaseRequestParam param = new BaseRequestParam();
        param.addParam("coach_id", CardSessionManager.getInstance().getUser().id);
        param.addParam("order_by", "desc");
        param.addParam("day_size", 7);

        if(mDate == null) {
            mDate = new Date();
        }
        param.addParam("qstart_date", LangUtils.formatDate(mDate, "yyyy-MM-dd"));
        startRequest(XService.CoachCourseList, param);
    }

    @Override
    public void onRefresh() {
        isReset = true;
        mDate = new Date();
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
        if(mItems == null || mItems.size() <= position) {
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

    private void setState(final BusinessStateHelper.BusinessState state) {
        ViewUtils.postDelayed(new Runnable() {
            @Override
            public void run() {
                mStateHelper.setState(state);
            }
        }, 500);
    }

    private void refreshListView(CoachCourseListResponse response) {
        if (response == null) {
            return;
        }

        if (isReset) {
            mItems.clear();
        }
        mItems.addAll(response.items);
        mAdapter.notifyDataSetChanged();

        if(LangUtils.isEmpty(mItems)) {
            setState(BusinessStateHelper.BusinessState.NO_DATA);
        }

        for(int i = 0; i < mItems.size(); i++) {
            CoachCourseListResponse.CoachCourseItem item = mItems.get(i);
            if(item == null) {
                continue;
            }

            if(mDetailResponse != null && item.id == mDetailResponse.id) {
                item.isUploadFailed = true;
            }

            if(i == 0) {
                item.showDate = true;
            } else {
                CoachCourseListResponse.CoachCourseItem item1 = mItems.get(i - 1);
                if(item1 != null) {
                    item.showDate = !LangUtils.isSameDay(item1.date, item.date);
                } else {
                    item.showDate = true;
                }
            }
        }

        mDate = response.nextDay;
        lvItems.setPullLoadEnable(response.hasMore);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCoachCourseUploadChanged(CourseDraftChangedEvent event) {
        mDetailResponse = CardManager.getFailedCourseDetail();
        if(mDetailResponse == null) {
            for(CoachCourseListResponse.CoachCourseItem item :mAdapter.getData()) {
                if(item != null && item.isUploadFailed) {
                    item.isUploadFailed = false;
                    mAdapter.notifyDataSetChanged();
                    break;
                }
            }
        } else {
            for(CoachCourseListResponse.CoachCourseItem item :mAdapter.getData()) {
                if(item != null && item.id == mDetailResponse.id) {
                    item.isUploadFailed = true;
                    mAdapter.notifyDataSetChanged();
                    break;
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            if(requestCode == COURSE_DETAIL_CODE) {
                mDate = new Date();
                isReset = true;
                fetchCoachCourses();
            }
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}