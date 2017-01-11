package com.oneside.ui.news;

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
import com.oneside.base.net.UrlRequestFactory;
import com.oneside.base.net.model.BaseResult;
import com.oneside.hy.HyUtils;
import com.oneside.model.ShowApiService;
import com.oneside.model.beans.ArticleSummary;
import com.oneside.model.request.BaseShowApiRequestParam;
import com.oneside.model.response.NewsListResponse;
import com.oneside.ui.view.XListView;
import com.oneside.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fupingfu on 2017/1/4.
 */
@XAnnotation(layoutId = R.layout.fragment_news)
public class NewsFragment extends BaseFragment implements XListView.IXListViewListener{
    @From(R.id.lv_items)
    private XListView lvItems;

    private int mPage;
    private List<ArticleSummary> mItems;
    private NewsAdapter mAdapter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        lvItems.setXListViewListener(this);
        lvItems.setPullLoadEnable(false);
        lvItems.setDividerHeight(ViewUtils.rp(10));
        mItems = new ArrayList<>();
        mAdapter = new NewsAdapter(getActivity(), mItems);
        lvItems.setAdapter(mAdapter);

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //XListView添加了Header，能点击到的onItemClick中的position从1开始，需要特殊处理
                //使用Parent的Adapter可以保证点击的Item编号从0开始
                if (parent.getAdapter().getItem(position) != null) {
                    ArticleSummary summary = (ArticleSummary) parent.getAdapter().getItem(position);
                    HyUtils.gotoStoryDetailWebPage((BaseActivity) getActivity(), summary.title, summary.id);
                }
            }
        });

        fetchNews(true);
    }

    private void fetchNews(boolean isReset) {
        BaseShowApiRequestParam requestParam = new BaseShowApiRequestParam();
        mPage = isReset ? 1 : mPage + 1;
        requestParam.addParam("page", mPage);
        requestParam.addParam("type", "neihanguigushi");

        startRequest(ShowApiService.GhostStoryList, requestParam);
    }

    @Override
    public void onRefresh() {
        fetchNews(true);
        ViewUtils.postDelayed(new Runnable() {
            @Override
            public void run() {
                lvItems.stopRefresh();
            }
        }, 500);
    }

    @Override
    public void onLoadMore() {
        fetchNews(false);
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
        freshLvItems((NewsListResponse) result);
    }

    @Override
    protected void onResponseError(UrlRequest request, int code, String message) {
        super.onResponseError(request, code, message);
    }

    @Override
    protected void onNetError(UrlRequest request, int statusCode) {
        super.onNetError(request, statusCode);
    }

    private void freshLvItems(NewsListResponse response) {
        if(response == null || response.data == null || response.data.page == null) {
            lvItems.setPullLoadEnable(false);
            return;
        }

        if(mPage == 1) {
            mItems.clear();
        }

        mItems.addAll(response.data.page.contentList);
        mAdapter.notifyDataSetChanged();
        lvItems.setPullLoadEnable(response.data.page.currentPage < response.data.page.allPages);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
