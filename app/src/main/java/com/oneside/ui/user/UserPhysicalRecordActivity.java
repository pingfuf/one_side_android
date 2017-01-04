package com.oneside.ui.user;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import com.oneside.base.BaseActivity;
import com.oneside.base.inject.From;
import com.oneside.base.inject.XAnnotation;
import com.oneside.base.net.UrlRequest;
import com.oneside.base.net.XService;
import com.oneside.base.net.model.BaseRequestParam;
import com.oneside.base.net.model.BaseResult;
import com.oneside.base.utils.BusinessStateHelper;
import com.oneside.R;
import com.oneside.model.beans.XUserPhysicalRecord;
import com.oneside.model.response.UserPhysicalRecordResponse;
import com.oneside.ui.view.XListView;
import com.oneside.utils.LangUtils;
import com.oneside.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户体侧列表页面
 *
 * @author pingfu
 */
public class UserPhysicalRecordActivity extends BaseActivity
        implements XListView.IXListViewListener, UserPhysicalRecordsAdapter.OnAddActionHandler {
    //每一页请求拉取10条数据
    private static final int PAGE_SIZE = 10;

    @From(R.id.lv_items)
    private XListView lvItems;

    private List<XUserPhysicalRecord> mRecords;
    private UserPhysicalRecordsAdapter mAdapter;
    private BusinessStateHelper mStateHelper;

    private int mPage;

    @XAnnotation
    private CustomerPageParam mPageParam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_physical);
        setTitle("体测报告", true);

        initUI();
        mStateHelper.setState(BusinessStateHelper.BusinessState.LOADING);
        fetchPhysicalRecords(true);
    }

    private void fetchPhysicalRecords(boolean isReset) {
        BaseRequestParam param = new BaseRequestParam();
        param.addUrlParams(mPageParam.memberId);
        param.addParam("page_size", PAGE_SIZE);
        param.addParam("page", isReset ? 1 : mPage + 1);
        startRequest(XService.UserPhysicalRecord, param);
    }

    private void initUI() {
        mStateHelper = BusinessStateHelper.build(this, lvItems);

        mStateHelper.setNoDataImage(R.drawable.guanzhu_no_pic);
        mStateHelper.setNoDataMessage("暂无体测报告");
        mStateHelper.setNoDataButtonText("");
        mStateHelper.setNetErrorRetryListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchPhysicalRecords(true);
            }
        });

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        mStateHelper.setLayoutParam(params);

        mRecords = new ArrayList<>();
        mAdapter = new UserPhysicalRecordsAdapter(this, mRecords);
        mAdapter.setAddActionHandler(this);
        lvItems.setPullLoadEnable(false);
        lvItems.setAdapter(mAdapter);
        lvItems.setXListViewListener(this);
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(isFinishing()) {
                    return;
                }

                if(parent.getAdapter().getItem(position) != null) {
                    XUserPhysicalRecord record = (XUserPhysicalRecord) parent.getAdapter().getItem(position);
                    gotoAddPhysicalRecordMenuPage(record);
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        fetchPhysicalRecords(true);
    }

    @Override
    public void onLoadMore() {
        fetchPhysicalRecords(false);
    }

    @Override
    protected void onNetError(UrlRequest request, int statusCode) {
        super.onNetError(request, statusCode);
        setStateSuccess(BusinessStateHelper.BusinessState.SUCCESS);
    }

    @Override
    protected void onResponseError(UrlRequest request, int code, String message) {
        super.onResponseError(request, code, message);
        setStateSuccess(BusinessStateHelper.BusinessState.SUCCESS);
    }

    @Override
    protected void onResponseSuccess(UrlRequest request, BaseResult data) {
        super.onResponseSuccess(request, data);
        boolean isReload = request.getUrl().contains("page=1");
        freshPhysicalRecords((UserPhysicalRecordResponse) data, isReload);
        if (isReload){
            lvItems.stopRefresh();
        }else{
            lvItems.stopLoadMore();
        }
    }

    /**
     * 刷新列表
     *
     * @param response 体侧记录请求返回结果
     */
    private void freshPhysicalRecords(UserPhysicalRecordResponse response, boolean isReload) {
        if(response == null || LangUtils.isEmpty(response.items)) {
            setStateSuccess(BusinessStateHelper.BusinessState.NO_DATA);
            return;
        }
        mPage = response.pagination.currentPage;
        if(isReload) {
            mRecords.clear();
        }

        mRecords.addAll(response.items);
        mAdapter.notifyDataSetChanged();

        if(LangUtils.isEmpty(mRecords)) {
            setStateSuccess(BusinessStateHelper.BusinessState.NO_DATA);
        } else {
            setStateSuccess(BusinessStateHelper.BusinessState.SUCCESS);
        }
        lvItems.setPullLoadEnable(response.pagination.hasMore());
    }

    private void setStateSuccess(final BusinessStateHelper.BusinessState state) {
        ViewUtils.postDelayed(new Runnable() {
            @Override
            public void run() {
                mStateHelper.setState(state);
            }
        }, 500);
    }

    private void gotoAddPhysicalRecordMenuPage(XUserPhysicalRecord record) {
        if(isFinishing() ) {
            return;
        }

        CustomerPageParam pageParam = mPageParam;

        pageParam.record = record;

        xStartActivity(AddPhysicalRecordMenuActivity.class, pageParam);
    }

    private UserPhysicalRecordResponse getTestData() {
        UserPhysicalRecordResponse response = new UserPhysicalRecordResponse();
//        response.moreUrl = "";
        response.items = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            XUserPhysicalRecord record = new XUserPhysicalRecord();
            record.fatRate = 40.0f;
            record.weight = 34;
            record.muscle = 60;
            record.date = "2016-09-09";

            response.items.add(record);
        }

        return response;
    }

    @Override
    public void addAction() {
        gotoAddPhysicalRecordMenuPage(null);
    }
}
