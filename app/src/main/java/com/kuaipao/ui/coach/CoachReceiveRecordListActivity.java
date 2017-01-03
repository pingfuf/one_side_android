package com.kuaipao.ui.coach;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.kuaipao.base.BaseActivity;
import com.kuaipao.base.inject.From;
import com.kuaipao.base.inject.XAnnotation;
import com.kuaipao.base.model.BasePageParam;
import com.kuaipao.base.net.UrlRequest;
import com.kuaipao.base.net.XService;
import com.kuaipao.base.net.model.BaseRequestParam;
import com.kuaipao.base.net.model.BaseResult;
import com.kuaipao.base.utils.BusinessStateHelper;
import com.kuaipao.manager.R;
import com.kuaipao.model.XReceiveRecord;
import com.kuaipao.model.response.CoachReceiveRecordsResponse;
import com.kuaipao.ui.customer.CustomerPageParam;
import com.kuaipao.ui.view.XListView;
import com.kuaipao.utils.Constant;
import com.kuaipao.utils.LangUtils;
import com.kuaipao.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

import static com.kuaipao.utils.WebUtils.getJsonInt;
import static com.kuaipao.utils.WebUtils.getJsonObject;

/**
 * Created by ZhanTao on 1/14/16.
 */
public class CoachReceiveRecordListActivity extends BaseActivity
        implements XListView.IXListViewListener, CoachReceiveRecordAdapter.OnAddActionHandler {
    @From(R.id.lv_items)
    private XListView lvItems;

    private CoachReceiveRecordAdapter mAdapter;
    private List<XReceiveRecord> mRecords;
    private BusinessStateHelper mStateHelper;

    public int mPage;

    @XAnnotation
    private CustomerPageParam mPageParam;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_coach_receive_record_list);
        setTitle("跟进记录", true);

        initUI();
        mStateHelper.setState(BusinessStateHelper.BusinessState.LOADING);
        fetchRecords(true);
    }

    private void initUI() {
        mRecords = new ArrayList<>();
        mAdapter = new CoachReceiveRecordAdapter(this, mRecords);
        mAdapter.setAddActionHandler(this);

        lvItems.setPullLoadEnable(false);
        lvItems.setXListViewListener(this);
        lvItems.setAdapter(mAdapter);
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        mStateHelper = BusinessStateHelper.build(this, lvItems);
        mStateHelper.setNetErrorRetryListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchRecords(true);
            }
        });
    }

    private void fetchRecords(boolean isReset) {
        BaseRequestParam param = new BaseRequestParam();
        param.addParam("member_id", mPageParam.memberId);
        param.addParam("page_size", Constant.PAGE_SIZE);
        param.addParam("page", (isReset ? 0 : mPage) + 1);

        startRequest(XService.CoachReceiveRecordList, param);
    }

    @Override
    public void onClick(View view) {
    }

    @Override
    public void onRefresh() {
        fetchRecords(true);
    }

    @Override
    public void onLoadMore() {
        fetchRecords(false);
    }

    @Override
    protected void onNetError(UrlRequest request, int statusCode) {
        super.onNetError(request, statusCode);
        setBusinessState(BusinessStateHelper.BusinessState.SUCCESS);
    }

    @Override
    protected void onResponseError(UrlRequest request, int code, String message) {
        super.onResponseError(request, code, message);
        setBusinessState(BusinessStateHelper.BusinessState.SUCCESS);
    }

    @Override
    protected void onResponseSuccess(UrlRequest request, BaseResult data) {
        super.onResponseSuccess(request, data);
        setBusinessState(BusinessStateHelper.BusinessState.SUCCESS);
        CoachReceiveRecordsResponse response = (CoachReceiveRecordsResponse) data;
        boolean isReload = request.getUrl().contains("page=1");
        refreshListView(response, isReload);
        if (isReload){
            lvItems.stopRefresh();
        }else{
            lvItems.stopLoadMore();
        }
    }

    private void setBusinessState(final BusinessStateHelper.BusinessState state) {
        ViewUtils.postDelayed(new Runnable() {
            @Override
            public void run() {
                mStateHelper.setState(state);
            }
        }, 500);
    }

    private void refreshListView(CoachReceiveRecordsResponse response, boolean isReload) {
        if (response == null) {
            return;
        }
        mPage = response.pagination.currentPage;
        if (isReload) {
            mRecords.clear();
        }

        if (!LangUtils.isEmpty(response.items)) {
            mRecords.addAll(response.items);
        }

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void addRecord() {
        xStartActivity(AddReceiveRecordActivity.class, mPageParam);
    }
}
