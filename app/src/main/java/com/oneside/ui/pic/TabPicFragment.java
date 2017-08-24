package com.oneside.ui.pic;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.oneside.R;
import com.oneside.base.BaseActivity;
import com.oneside.base.BaseFragment;
import com.oneside.base.inject.From;
import com.oneside.base.inject.XAnnotation;
import com.oneside.base.net.UrlRequest;
import com.oneside.base.net.model.BaseResult;
import com.oneside.base.utils.BusinessStateHelper;
import com.oneside.base.view.filter.FilterView;
import com.oneside.base.hy.HyNativeUtils;
import com.oneside.model.ShowApiService;
import com.oneside.model.beans.ArticleSummary;
import com.oneside.model.request.BaseShowApiRequestParam;
import com.oneside.model.response.ConnotativePicsResponse;
import com.oneside.model.response.StoryPicsResponse;
import com.oneside.ui.view.XListView;
import com.oneside.utils.LogUtils;
import com.oneside.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fupingfu on 2017/1/12.
 */
@XAnnotation(layoutId = R.layout.fragment_tab_pic)
public class TabPicFragment extends BaseFragment
        implements XListView.IXListViewListener, PicFilterManager.onFilterChosenListener {
    @From(R.id.v_filter)
    private FilterView filterView;

    @From(R.id.lv_items)
    private XListView lvItem;

    private PicFilterManager mFilterManager;
    private ConnotativePicAdapter mConnotativePicAdapter;
    private List<ArticleSummary> mConnotativePics;

    private StoryPicAdapter mStoryPicAdapter;
    private List<ArticleSummary> mStoryPics;

    private int mType;
    private int mPage = 1;
    private int mContentType;
    private String mContent;

    private BusinessStateHelper mStateHelper;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mFilterManager = new PicFilterManager(getActivity(), filterView);
        mFilterManager.setFilterChosenListener(this);
        lvItem.setPullLoadEnable(false);
        lvItem.setXListViewListener(this);

        lvItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(mType == 0) {
                    ArticleSummary summary = (ArticleSummary) parent.getAdapter().getItem(position);
                    HyNativeUtils.gotoStoryPicDetailWebPage((BaseActivity)getActivity(), summary.title, summary.id);
                } else {
                    ArticleSummary summary = (ArticleSummary) parent.getAdapter().getItem(position);
                    HyNativeUtils.gotoConnotativePicDetailWebPage((BaseActivity)getActivity(), summary.title, summary.id);
                }
            }
        });

        mStoryPics = new ArrayList<>();
        mStoryPicAdapter = new StoryPicAdapter(getActivity(), mStoryPics);

        mConnotativePics = new ArrayList<>();
        mConnotativePicAdapter = new ConnotativePicAdapter(getActivity(), mConnotativePics);

        mStateHelper = BusinessStateHelper.build(getActivity(), lvItem);
        mStateHelper.setState(BusinessStateHelper.BusinessState.LOADING);

        initData();

        fetchContent(true, mContent);
    }

    private void initData() {
        mType = 0;
        mContent = "/category/weimanhua/kbmh";
        lvItem.setAdapter(mStoryPicAdapter);
    }

    private void fetchContent(boolean isReset, String content) {
        LogUtils.e("TabPicFragment fetchContent");
        switch (mType) {
            case 0:
                BaseShowApiRequestParam requestParam = new BaseShowApiRequestParam();
                mPage = isReset ? 1 : mPage + 1;
                requestParam.addParam("page", mPage);
                requestParam.addParam("type", mContent);

                startRequest(ShowApiService.StoryPics, requestParam);
                break;
            case 1:
                fetchConnotativePics(isReset);
                break;
            default:
                break;
        }
    }

    private void fetchConnotativePics(boolean isReset) {
        BaseShowApiRequestParam requestParam = new BaseShowApiRequestParam();
        mPage = isReset ? 1 : mPage + 1;
        requestParam.addParam("page", mPage);

        startRequest(ShowApiService.ConnotativePics, requestParam);
    }

    @Override
    public void onFilterChosen(int menuType, int contentType, String content) {
        if(menuType == mType && contentType == mContentType) {
            return;
        }
        mType = menuType;
        mContentType = contentType;
        mContent = content;

        if(mType == 0) {
            lvItem.setAdapter(mStoryPicAdapter);
        } else {
            lvItem.setAdapter(mConnotativePicAdapter);
        }

        mStateHelper.setState(BusinessStateHelper.BusinessState.LOADING);
        fetchContent(true, content);
    }

    @Override
    public void onRefresh() {
        fetchContent(true, mContent);
        ViewUtils.postDelayed(new Runnable() {
            @Override
            public void run() {
                lvItem.stopRefresh();
            }
        }, 500);
    }

    @Override
    public void onLoadMore() {
        fetchContent(false, mContent);
        ViewUtils.postDelayed(new Runnable() {
            @Override
            public void run() {
                lvItem.stopLoadMore();
            }
        }, 500);
    }

    @Override
    protected void onResponseSuccess(UrlRequest request, BaseResult result) {
        super.onResponseSuccess(request, result);
        mStateHelper.setState(BusinessStateHelper.BusinessState.SUCCESS);
        if(isSameUrl(ShowApiService.ConnotativePics, request)) {
            freshConnotativePicsListView((ConnotativePicsResponse) result);
        } else if(isSameUrl(ShowApiService.StoryPics, request)) {
            freshStoryPicsListView((StoryPicsResponse) result);
        }
    }

    private void freshStoryPicsListView(StoryPicsResponse result) {
        if(result == null || result.data == null || result.data.pagebean == null) {
            return;
        }

        if(mPage == 1) {
            mStoryPics.clear();
        }
        mStoryPics.addAll(result.data.pagebean.contentlist);
        lvItem.setPullLoadEnable(result.data.hasMore);
        mStoryPicAdapter.notifyDataSetChanged();
    }

    private void freshConnotativePicsListView(ConnotativePicsResponse result) {
        if(result == null || result.data == null || result.data.pagebean == null) {
            return;
        }

        if(mPage == 1) {
            mConnotativePics.clear();
        }
        mConnotativePics.addAll(result.data.pagebean.contentlist);
        lvItem.setPullLoadEnable(result.data.hasMore);
        mConnotativePicAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResponseError(UrlRequest request, int code, String message) {
        super.onResponseError(request, code, message);
        mStateHelper.setState(BusinessStateHelper.BusinessState.NET_ERROR);
    }

    @Override
    protected void onNetError(UrlRequest request, int statusCode) {
        super.onNetError(request, statusCode);
        mStateHelper.setState(BusinessStateHelper.BusinessState.NET_ERROR);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
