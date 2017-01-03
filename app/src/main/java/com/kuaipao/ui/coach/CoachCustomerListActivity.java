package com.kuaipao.ui.coach;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.kuaipao.base.inject.From;
import com.kuaipao.base.inject.XAnnotation;
import com.kuaipao.base.model.BasePageParam;
import com.kuaipao.base.net.model.BaseRequestParam;
import com.kuaipao.base.utils.BusinessStateHelper;
import com.kuaipao.manager.CardSessionManager;
import com.kuaipao.manager.XSoftKeyboardManager;
import com.kuaipao.model.beans.XCustomer;
import com.kuaipao.base.BaseActivity;
import com.kuaipao.base.net.XService;
import com.kuaipao.base.net.model.BaseResult;
import com.kuaipao.model.beans.XGym;
import com.kuaipao.model.response.CustomerListResponse;
import com.kuaipao.ui.customer.CustomerDetailActivity;
import com.kuaipao.ui.customer.CustomerPageParam;
import com.kuaipao.ui.view.XListView;
import com.kuaipao.utils.Constant;
import com.kuaipao.utils.LangUtils;
import com.kuaipao.utils.LogUtils;
import com.kuaipao.utils.ViewUtils;
import com.kuaipao.base.net.UrlRequest;
import com.kuaipao.manager.R;

import java.util.ArrayList;
import java.util.List;

import static com.kuaipao.utils.ViewUtils.find;

/**
 * 我的顾客页面，显示顾客列表
 * <p>
 * Created by Magi on 2015/11/18.
 */
public class CoachCustomerListActivity extends BaseActivity
        implements XListView.IXListViewListener, XSoftKeyboardManager.OnSoftKeyboardShowOrHideListener {
    @From(R.id.ll_search)
    private LinearLayout llSearch;

    @From(R.id.edt_search)
    private EditText edtSearch;

    @From(R.id.lv_items)
    private XListView lvItems;

    private BusinessStateHelper mStateHelper;
    private List<XCustomer> mCustomers;
    private CoachCustomerListAdapter mAdapter;
    private View.OnClickListener mRetryListener;
    private int mPage;
    private String mSearchWord;
    private XSoftKeyboardManager softKeyboardManager;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_coach_students);
        setTitle(getString(R.string.coach_customer), true);

        initUI();

        fetchCustomers(true);
        mStateHelper.setState(BusinessStateHelper.BusinessState.LOADING);
    }

    private void initUI() {
        mCustomers = new ArrayList<>();
        mAdapter = new CoachCustomerListAdapter(this, mCustomers);
        lvItems.setAdapter(mAdapter);
        lvItems.setXListViewListener(this);

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //XListView添加了Header，能点击到的onItemClick中的position从1开始，需要特殊处理
                //使用Parent的Adapter可以保证点击的Item编号从0开始
                if (parent.getAdapter().getItem(position) != null) {
                    gotoCustomerDetailPage((XCustomer) parent.getAdapter().getItem(position));
                }
            }
        });

        mStateHelper = BusinessStateHelper.build(this, lvItems);
        mRetryListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchCustomers(true);
            }
        };
        mStateHelper.setNetErrorRetryListener(mRetryListener);

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mSearchWord = s.toString();
                fetchCustomers(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mStateHelper.setNoDataImage(R.mipmap.customer_no_data_icon);
        mStateHelper.setNoDataMessage("还未添加顾客");

        softKeyboardManager = XSoftKeyboardManager.build(this, this);
        mRootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                llSearch.setFocusable(true);
                llSearch.setFocusableInTouchMode(true);
                llSearch.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edtSearch.getWindowToken(), 0);
                return false;
            }
        });
    }

    /**
     * 跳转到客户详情页面
     *
     * @param customer 客户信息
     */
    private void gotoCustomerDetailPage(XCustomer customer) {
        if (customer == null) {
            return;
        }

        CustomerPageParam pageParam = new CustomerPageParam();
        pageParam.name = customer.name;
        pageParam.phone = customer.phone;
        pageParam.headerUrl = customer.avatar;

        if (customer.gym != null) {
            pageParam.gymId = customer.gym.id;
        }

        pageParam.memberId = customer.id;

        xStartActivity(CustomerDetailActivity.class, pageParam);
    }

    /**
     * 获取客户列表信息请求
     *
     * @param isReset 是否是刷新请求
     */
    private void fetchCustomers(boolean isReset) {
        BaseRequestParam param = new BaseRequestParam();
        param.addParam("user_id", CardSessionManager.getInstance().getUser().id);
        param.addParam("page_size", Constant.PAGE_SIZE);

        param.addParam("page", isReset ? 1 : mPage + 1);

        if (LangUtils.isNotEmpty(mSearchWord)) {
            param.addParam("q", mSearchWord);
        }

        startRequest(XService.CoachMemberList, param);
    }

    @Override
    protected void onResponseSuccess(UrlRequest request, BaseResult data) {
        super.onResponseSuccess(request, data);
        setBusinessState(BusinessStateHelper.BusinessState.SUCCESS);
        CustomerListResponse response = (CustomerListResponse) data;
        boolean isReload = request.getUrl().contains("page=1");
        if (response != null) {
            mPage = response.pagination.currentPage;
            freshCoachStudents(response.items, isReload);
            if (response.pagination != null) {
                lvItems.setPullLoadEnable(response.pagination.hasMore());
            }
        }
        if(isReload){
            lvItems.stopRefresh();
        }else{
            lvItems.stopLoadMore();
        }
    }

    @Override
    protected void onResponseError(UrlRequest request, int code, String message) {
        super.onResponseError(request, code, message);
        setBusinessState(BusinessStateHelper.BusinessState.NET_ERROR);
    }

    @Override
    protected void onNetError(UrlRequest request, int statusCode) {
        super.onNetError(request, statusCode);
        setBusinessState(BusinessStateHelper.BusinessState.NET_ERROR);
    }

    @Override
    public void onRefresh() {
        fetchCustomers(true);
    }

    @Override
    public void onLoadMore() {
        fetchCustomers(false);
    }

    private void setBusinessState(final BusinessStateHelper.BusinessState state) {

        mStateHelper.setState(state);

    }

    /**
     * 刷新列表
     *
     * @param customers 列表数据
     */
    private void freshCoachStudents(List<XCustomer> customers, boolean isReload) {
        if (isReload) {
            mCustomers.clear();
        }
        mCustomers.addAll(customers);
        mAdapter.notifyDataSetChanged();

        if (LangUtils.isEmpty(mCustomers)) {
            setBusinessState(BusinessStateHelper.BusinessState.NO_DATA);
            if (LangUtils.isNotEmpty(mSearchWord)) {
                mStateHelper.setNoDataMessage("暂无搜索结果");
            } else {
                mStateHelper.setNoDataMessage("还未添加顾客");
            }
        }
    }

    @Override
    public void onKeyboardShow() {

    }

    @Override
    public void onKeyboardHide() {
        llSearch.setFocusable(true);
        llSearch.setFocusableInTouchMode(true);
        llSearch.requestFocus();
    }
}
