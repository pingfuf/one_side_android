package com.oneside.ui.story;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.oneside.base.BaseActivity;
import com.oneside.base.inject.From;
import com.oneside.base.inject.XAnnotation;
import com.oneside.base.model.BasePageParam;
import com.oneside.R;
import com.oneside.base.net.UrlRequest;
import com.oneside.base.net.model.BaseResult;
import com.oneside.model.ShowApiService;
import com.oneside.model.request.BaseShowApiRequestParam;
import com.oneside.model.response.StoryDetailResponse;

/**
 * Created by MVEN on 16/5/9.
 */
public class StoryDetailActivity extends BaseActivity {
    @From(R.id.tv_content)
    private TextView tvContent;

    @XAnnotation
    private StoryDetailPageParam mPageParam;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_story_detail);

        setTitle(mPageParam.title, true);
        fetchStoryDetail();
        showLoadingDialog();
    }

    private void fetchStoryDetail() {
        BaseShowApiRequestParam param = new BaseShowApiRequestParam();
        param.addParam("id", mPageParam.id);
        startRequest(ShowApiService.GhostStoryDetail, param);
    }

    @Override
    protected void onResponseSuccess(UrlRequest request, BaseResult data) {
        super.onResponseSuccess(request, data);
        dismissLoadingDialog();
        StoryDetailResponse response = (StoryDetailResponse) data;
        if(response != null && response.data != null) {
            tvContent.setText(response.data.text);
        }
    }

    @Override
    protected void onResponseError(UrlRequest request, int code, String message) {
        super.onResponseError(request, code, message);
        dismissLoadingDialog();
    }

    @Override
    protected void onNetError(UrlRequest request, int statusCode) {
        super.onNetError(request, statusCode);
        dismissLoadingDialog();
    }

    @Override
    public void onClick(View v) {
    }

    public static class StoryDetailPageParam extends BasePageParam {
        public String title;
    }
}
