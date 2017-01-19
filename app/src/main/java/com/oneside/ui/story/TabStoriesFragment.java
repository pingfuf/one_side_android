package com.oneside.ui.story;

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
import com.oneside.hy.HyNativeUtils;
import com.oneside.model.ShowApiService;
import com.oneside.model.beans.ArticleSummary;
import com.oneside.model.request.BaseShowApiRequestParam;
import com.oneside.model.response.JokeListResponse;
import com.oneside.model.response.NewsListResponse;
import com.oneside.ui.view.XListView;
import com.oneside.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fupingfu on 2017/1/4.
 */
@XAnnotation(layoutId = R.layout.fragment_news)
public class TabStoriesFragment extends BaseFragment
        implements XListView.IXListViewListener, StoryFilterManager.onFilterChosenListener{
    @From(R.id.v_filter)
    private FilterView filterView;

    @From(R.id.lv_items)
    private XListView lvItems;

    private int mPage;
    private List<ArticleSummary> mItems;
    private StoriesAdapter mAdapter;
    private List<JokeListResponse.JokeArticle> mJokeArticles;
    private JokesAdapter mJokeAdapter;
    private StoryFilterManager filterManager;
    private BusinessStateHelper mStateHelper;
    private int mMenuType = 0;
    private int mContentType = 0;
    private String mType = "dp";

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        filterManager = new StoryFilterManager(getActivity(), filterView);
        filterManager.setFilterChosenListener(this);
        lvItems.setXListViewListener(this);
        lvItems.setPullLoadEnable(false);
        lvItems.setDividerHeight(ViewUtils.rp(10));
        mItems = new ArrayList<>();
        mAdapter = new StoriesAdapter(getActivity(), mItems);

        mJokeArticles = new ArrayList<>();
        mJokeAdapter = new JokesAdapter(getActivity(), mJokeArticles);
        lvItems.setAdapter(mAdapter);

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //XListView添加了Header，能点击到的onItemClick中的position从1开始，需要特殊处理
                //使用Parent的Adapter可以保证点击的Item编号从0开始
                if (parent.getAdapter().getItem(position) != null) {
                    if(mMenuType == 0) {
                        ArticleSummary summary = (ArticleSummary) parent.getAdapter().getItem(position);
                        HyNativeUtils.gotoStoryDetailWebPage((BaseActivity) getActivity(), summary.title, summary.id);

                    } else {
                        JokeListResponse.JokeArticle article = (JokeListResponse.JokeArticle)parent.getAdapter().getItem(position);
                        JokeDetailPageParam pageParam = new JokeDetailPageParam();
                        pageParam.title = article.title;
                        pageParam.content = article.text;
                        xStartActivity(JokeDetailActivity.class, pageParam);
                    }
                }
            }
        });

        mStateHelper = BusinessStateHelper.build(getActivity(), lvItems);
        mStateHelper.setState(BusinessStateHelper.BusinessState.LOADING);
        fetchNews(true, mType);
    }

    private void gotoStoryDetailPage(ArticleSummary summary) {
        if(!isValidFragment() || summary == null) {
            return;
        }

        StoryDetailActivity.StoryDetailPageParam pageParam = new StoryDetailActivity.StoryDetailPageParam();
        pageParam.title = summary.title;
        pageParam.id = summary.id;

        xStartActivity(StoryDetailActivity.class, pageParam);
    }

    private void fetchContent(boolean isReset, String type) {
        switch (mMenuType) {
            case 0:
                fetchNews(isReset, type);
                break;
            case 1:
                fetchJokes(isReset);
                break;
            default:
                break;
        }
    }

    private void fetchNews(boolean isReset, String type) {
        BaseShowApiRequestParam requestParam = new BaseShowApiRequestParam();
        mPage = isReset ? 1 : mPage + 1;
        requestParam.addParam("page", mPage);
        requestParam.addParam("type", type);

        startRequest(ShowApiService.GhostStoryList, requestParam);
    }

    private void fetchJokes(boolean isReset) {
        BaseShowApiRequestParam requestParam = new BaseShowApiRequestParam();
        mPage = isReset ? 1 : mPage + 1;
        startRequest(ShowApiService.JokesList, requestParam);
    }

    @Override
    public void onRefresh() {
        fetchContent(true, mType);
        ViewUtils.postDelayed(new Runnable() {
            @Override
            public void run() {
                lvItems.stopRefresh();
            }
        }, 500);
    }

    @Override
    public void onLoadMore() {
        fetchContent(false, mType);
        ViewUtils.postDelayed(new Runnable() {
            @Override
            public void run() {
                lvItems.stopLoadMore();
            }
        }, 500);
    }

    @Override
    protected void onResponseSuccess(UrlRequest request, BaseResult result) {
        super.onResponseSuccess(request, result);
        mStateHelper.setState(BusinessStateHelper.BusinessState.SUCCESS);
        if(isSameUrl(ShowApiService.GhostStoryList, request)) {
            freshLvItems((NewsListResponse) result);
        } else if(isSameUrl(ShowApiService.JokesList, request)) {
            freshLvItems((JokeListResponse)result);
        }
    }

    @Override
    protected void onResponseError(UrlRequest request, int code, String message) {
        super.onResponseError(request, code, message);
        mStateHelper.setState(BusinessStateHelper.BusinessState.SUCCESS);
    }

    @Override
    protected void onNetError(UrlRequest request, int statusCode) {
        super.onNetError(request, statusCode);
        mStateHelper.setState(BusinessStateHelper.BusinessState.SUCCESS);
    }

    private void freshLvItems(NewsListResponse response) {
        if(response == null || response.data == null || response.data.page == null) {
            lvItems.setPullLoadEnable(false);
            return;
        }

        if(mPage == 1) {
            mItems.clear();
        }

        lvItems.setAdapter(mAdapter);
        mItems.addAll(response.data.page.contentList);
        mAdapter.notifyDataSetChanged();
        lvItems.setPullLoadEnable(response.data.page.hasMore());
    }

    private void freshLvItems(JokeListResponse response) {
        if(response == null || response.data == null) {
            return;
        }

        if(mPage == 1) {
            mJokeArticles.clear();
        }
        lvItems.setAdapter(mJokeAdapter);
        mJokeArticles.addAll(response.data.contentlist);
        mAdapter.notifyDataSetChanged();
        lvItems.setPullLoadEnable(response.data.hasMore());
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onFilterChosen(int menuType, int contentType, String content) {
        if(menuType == mMenuType && contentType == mContentType) {
            return;
        }

        mMenuType = menuType;
        mContentType = contentType;
        mType = content;
        mStateHelper.setState(BusinessStateHelper.BusinessState.LOADING);
        fetchContent(true, mType);
    }
}
