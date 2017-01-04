package com.oneside.ui.coach;

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

import com.oneside.base.BaseActivity;
import com.oneside.base.inject.From;
import com.oneside.base.net.UrlRequest;
import com.oneside.base.net.XService;
import com.oneside.base.net.model.BaseRequestParam;
import com.oneside.base.net.model.BaseResult;
import com.oneside.base.utils.BusinessStateHelper;
import com.oneside.manager.CardSessionManager;
import com.oneside.R;
import com.oneside.manager.XSoftKeyboardManager;
import com.oneside.model.beans.CoachMember;
import com.oneside.model.response.CoachMemberListResponse;
import com.oneside.ui.user.CustomerDetailActivity;
import com.oneside.ui.user.CustomerPageParam;
import com.oneside.ui.view.XListView;
import com.oneside.utils.LangUtils;
import com.oneside.utils.ViewUtils;

/**
 * 私教会员页面
 *
 * @author pingfu
 */
public class CoachPersonalCustomerActivity extends BaseActivity
        implements XListView.IXListViewListener, XSoftKeyboardManager.OnSoftKeyboardShowOrHideListener {
    @From(R.id.ll_search)
    private LinearLayout llSearch;

    @From(R.id.edt_search)
    private EditText edtSearch;

    @From(R.id.lv_items)
    private XListView lvItems;

    private CoachPersonalCustomerAdapter mAdapter;
//    private List<CoachMember> mCoachMembers;
    private CoachMemberListResponse coachMemberListResponse;
    private BusinessStateHelper mStateHelper;
    private String mSearchWord;
    XSoftKeyboardManager softKeyboardManager;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_coach_member);
        setTitle(getString(R.string.coach_personal_member), true);

        initUI();
        mStateHelper.setState(BusinessStateHelper.BusinessState.LOADING);
        fetchMembers(true, null);
    }

    private void initUI() {

        mAdapter = new CoachPersonalCustomerAdapter(this);
        lvItems.setPullLoadEnable(false);
        lvItems.setXListViewListener(this);
        lvItems.setAdapter(mAdapter);
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CoachMember member = mAdapter.getItem(position - 1);
                if(member != null && member.getUser() != null) {
                    CustomerPageParam pageParam = new CustomerPageParam();
                    pageParam.name = member.getUser().name;
                    pageParam.headerUrl = member.getUser().avatar;
                    pageParam.phone = member.getUser().phone;
                    pageParam.memberId = member.getUser().id;
                    xStartActivity(CustomerDetailActivity.class, pageParam);
                }

            }
        });

        mStateHelper = BusinessStateHelper.build(this, lvItems);
        mStateHelper.setNoDataImage(R.mipmap.coach_member_no_data_icon);
        mStateHelper.setNoDataMessage(getString(R.string.coach_member_no_data));
        mStateHelper.setNoDataViewBackgroundColor(R.color.layout_background);
        mStateHelper.setNoDataButtonText(null);
        mStateHelper.setNetErrorRetryListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchMembers(true, null);
            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mSearchWord = s.toString();
                fetchMembers(true, mSearchWord);
            }

            @Override
            public void afterTextChanged(Editable s) {
                fetchMembers(true, s.toString());
            }
        });

        softKeyboardManager = XSoftKeyboardManager.build(this, this);
        mRootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                llSearch.setFocusable(true);
                llSearch.setFocusableInTouchMode(true);
                llSearch.requestFocus();
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edtSearch.getWindowToken(),0);
                return false;
            }
        });
    }

    private void fetchMembers(boolean isRefresh, String key) {
        BaseRequestParam params = new BaseRequestParam();
        params.addUrlParams(CardSessionManager.getInstance().getUser().id);
        if (LangUtils.isNotEmpty(key)) {
            params.addParam("q", key);
        }
        params.addParam("page", isRefresh ? 1 : ((coachMemberListResponse == null ? 0 : coachMemberListResponse.getPagination().currentPage) + 1));
        params.addParam("page_size", 20);
        startRequest(XService.COACH_MEMBERS, params);
    }

    @Override
    public void onClick(View v) {
        if(v == mRootView) {
            softKeyboardManager.hideSoftKeyboard(edtSearch);
        }
    }

    @Override
    public void onRefresh() {
        fetchMembers(true, edtSearch.getEditableText().toString());
    }

    @Override
    public void onLoadMore() {
        fetchMembers(false, edtSearch.getEditableText().toString());

    }

    @Override
    protected void onNetError(UrlRequest request, int statusCode) {
        super.onNetError(request, statusCode);
        if (isKeyValid(request.getUrl(), edtSearch.getEditableText().toString())) {
            setState(BusinessStateHelper.BusinessState.NET_ERROR);
        }
    }

    @Override
    protected void onResponseError(UrlRequest request, int code, String message) {
        super.onResponseError(request, code, message);
        if (isKeyValid(request.getUrl(), edtSearch.getEditableText().toString())){
            setState(BusinessStateHelper.BusinessState.NET_ERROR);
    }
    }

    @Override
    protected void onResponseSuccess(UrlRequest request, BaseResult data) {
        super.onResponseSuccess(request, data);
        if (!isKeyValid(request.getUrl(), edtSearch.getEditableText().toString())){
            return;
        }
        final boolean isRefresh = request.getUrl().contains("page=1");
        if (isRefresh) {
            coachMemberListResponse = (CoachMemberListResponse) data;
        }else{
            coachMemberListResponse.addOtherResponse((CoachMemberListResponse) data);
        }
        refreshListData(isRefresh);
    }

    private void setState(final BusinessStateHelper.BusinessState state) {
        ViewUtils.runInHandlerThread(new Runnable() {
            @Override
            public void run() {
                mStateHelper.setState(state);
            }
        });
    }

    private void refreshListData(final boolean isRefresh){
        ViewUtils.runInHandlerThread(new Runnable() {
            @Override
            public void run() {
                if (isRefresh){
                    lvItems.stopRefresh();
                }else{
                    lvItems.stopLoadMore();
                }
                lvItems.setPullLoadEnable(coachMemberListResponse.getPagination().hasMore());
                if(LangUtils.isEmpty(coachMemberListResponse.getItems())) {
                    setState(BusinessStateHelper.BusinessState.NO_DATA);
                    if(LangUtils.isEmpty(mSearchWord)) {
                        mStateHelper.setNoDataMessage(getString(R.string.coach_member_no_data));
                    } else {
                        mStateHelper.setNoDataMessage("暂无搜索结果");
                    }
                } else {
                    setState(BusinessStateHelper.BusinessState.SUCCESS);
                }


                mAdapter.setData(coachMemberListResponse.getItems());

            }
        });

    }

    private boolean isKeyValid(String url, String key){
        if (url == null){
            return false;
        }

        return true;
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
