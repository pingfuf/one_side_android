package com.oneside.ui.news;

import android.app.Activity;
import android.os.Bundle;

import com.oneside.R;
import com.oneside.base.BaseFragment;
import com.oneside.base.inject.From;
import com.oneside.base.inject.XAnnotation;
import com.oneside.ui.view.XListView;

/**
 * Created by fupingfu on 2017/1/4.
 */
@XAnnotation(layoutId = R.layout.fragment_news)
public class NewsFragment extends BaseFragment {
    @From(R.id.lv_items)
    XListView lvItems;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        startRequest();
    }

    private void startRequest() {

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
