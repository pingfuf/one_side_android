package com.oneside.ui.story;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.oneside.base.BaseFragment;
import com.oneside.base.inject.From;
import com.oneside.base.inject.XAnnotation;
import com.oneside.base.model.BasePageParam;
import com.oneside.base.net.UrlRequest;
import com.oneside.base.net.XService;
import com.oneside.base.net.model.BaseRequestParam;
import com.oneside.base.net.model.BaseResult;
import com.oneside.base.utils.BusinessStateHelper;
import com.oneside.R;
import com.oneside.model.beans.CoachPersonalCard;
import com.oneside.model.response.CoachPersonalCardsResponse;
import com.oneside.ui.view.XListView;
import com.oneside.utils.Constant;
import com.oneside.utils.LangUtils;
import com.oneside.utils.ViewUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 会员卡列表界面
 * <p>
 * Created by pingfu on 16-9-9.
 */
@XAnnotation(layoutId = R.layout.fragment_membership_card)
public class CourseCardFragment extends BaseFragment implements XListView.IXListViewListener {
    @From(R.id.lv_items)
    private XListView lvItems;

    private CourseCardAdapter mAdapter;
    private List<CoachPersonalCard> mCardList;
    private BusinessStateHelper mStateHelper;

    @XAnnotation
    private BasePageParam mPageParam;

    private int mPage = 1;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initUI();
        mStateHelper.setState(BusinessStateHelper.BusinessState.LOADING);
        fetchCoachCards(true);
    }

    private void initUI() {
        mCardList = new ArrayList<>();
        mAdapter = new CourseCardAdapter(getActivity(), mCardList);
        lvItems.setAdapter(mAdapter);
        lvItems.setPullLoadEnable(false);
        lvItems.setXListViewListener(this);
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        mStateHelper = BusinessStateHelper.build(getActivity(), lvItems);
        mStateHelper.setNoDataImage(R.mipmap.coach_card_no_data_icon);
        mStateHelper.setNoDataMessage("暂无私教卡");
        mStateHelper.setNoDataViewBackgroundColor(R.color.layout_background);
        mStateHelper.setNetErrorRetryListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchCoachCards(true);
            }
        });
    }

    private void fetchCoachCards(boolean isReset) {
        BaseRequestParam param = new BaseRequestParam();
        param.addParam("member_id", mPageParam.memberId);

        param.addParam("page", isReset ? 1 : mPage + 1);
        param.addParam("page_size", Constant.PAGE_SIZE);
        startRequest(XService.CoachPersonalCardList, param);
    }

    @Override
    public void onRefresh() {
        fetchCoachCards(true);
    }

    @Override
    public void onLoadMore() {
        fetchCoachCards(false);
    }

    @Override
    protected void onNetError(UrlRequest request, int statusCode) {
        super.onNetError(request, statusCode);
        setState(BusinessStateHelper.BusinessState.SUCCESS);
    }

    @Override
    protected void onResponseError(UrlRequest request, int code, String mesasge) {
        super.onResponseError(request, code, mesasge);
        setState(BusinessStateHelper.BusinessState.SUCCESS);
    }

    @Override
    protected void onResponseSuccess(UrlRequest request, BaseResult result) {
        super.onResponseSuccess(request, result);
        setState(BusinessStateHelper.BusinessState.SUCCESS);
        CoachPersonalCardsResponse response = (CoachPersonalCardsResponse) result;
        boolean isReload = request.getUrl().contains("page=1");
        refreshListView(response, isReload);
        if (isReload){
            lvItems.stopRefresh();
        }else{
            lvItems.stopLoadMore();
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

    private void refreshListView(CoachPersonalCardsResponse response, boolean isReload) {
        if (response == null) {
            return;
        }
        mPage = response.pagination.currentPage;
        lvItems.setPullLoadEnable(response.hasMore);
        if (isReload) {
            mCardList.clear();
        }

        if (!LangUtils.isEmpty(response.items)) {
            mCardList.addAll(response.items);
        }

        for(int i = 0; i < mCardList.size(); i ++) {
            CoachPersonalCard card = mCardList.get(i);
            Date date = LangUtils.formatAllDayDate(card.beginTime);
            String dateStr = LangUtils.formatDate(date, "yyyy年MM月");

            if(i == 0) {
                card.isShowDate = true;
            } else {
                String dateStr1 = mCardList.get(i - 1).date;
                card.isShowDate = !dateStr.equals(dateStr1);
            }
        }

        mAdapter.notifyDataSetChanged();

        if(LangUtils.isEmpty(mCardList)) {
            setState(BusinessStateHelper.BusinessState.NO_DATA);
        }
    }
}
