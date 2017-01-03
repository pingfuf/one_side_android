package com.kuaipao.base.view.filter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.kuaipao.base.BaseActivity;
import com.kuaipao.base.view.filter.model.FilterData;
import com.kuaipao.base.view.filter.model.MultiColumnItemData;
import com.kuaipao.utils.LangUtils;
import com.kuaipao.utils.LogUtils;
import com.kuaipao.utils.SysUtils;
import com.kuaipao.manager.R;

/**
 * User: XXKuaipao(www.xxkuaipao.com)
 * Date: 2016-05-10
 * Time: 13:18
 * Author: pingfu
 * FIXME
 */
public class FilterView extends LinearLayout implements FilterItemView.OnFilterItemClickListener {
    private static final String DEFAULT_ALL_TITLE = "全部";
    private static final String DEFAULT_OTHER_TITLE = "其他";
    private LinearLayout llContent;
    private PopupWindow mPopupWindow;
    private PopupContentView mPopupWindowContent;
    private BaseActivity mContext;

    //下拉菜单的最大高度是屏幕高度的60%
    private static final int MAX_CONTENT_HEIGHT = (int) (0.6 * SysUtils.HEIGHT);
    private String mCurrentCheckTitleTag;

    public FilterView(Context context) {
        super(context);
        init(context);
    }

    public FilterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context) {
        if (context instanceof BaseActivity) {
            mContext = (BaseActivity) context;
        }
        inflate(context, R.layout.filter_title, this);
        llContent = (LinearLayout) findViewById(R.id.ll_content);

        mPopupWindowContent = new PopupContentView(getContext());
        mPopupWindow = new PopupWindow(mPopupWindowContent, SysUtils.WIDTH, SysUtils.HEIGHT);
        mPopupWindow.setFocusable(false);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.alpha_black)));
        mPopupWindowContent.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hidePopupWindow();
            }
        });
    }

    public void refreshTitle(){
        llContent.removeAllViews();
    }

    public void addFilterItemView(FilterItemView filterItemView) {
        addFilterItemView(filterItemView, 1);
    }

    public void addFilterItemView(FilterItemView filterItemView, float weight) {
        final FilterTitleItemView titleView = createTitleItemView(filterItemView.getTitle());
        final String tag = filterItemView.getTitle();
        LayoutParams params = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT);
        params.weight = weight;
        params.gravity = Gravity.CENTER;
        titleView.setTag(tag);
        llContent.addView(titleView, params);

        titleView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext != null && !mContext.isFinishing()) {
                    boolean flag = tag.equals(mPopupWindowContent.getCurrentShowViewTag());
                    boolean isChecked = flag && mPopupWindow.isShowing();
                    if (isChecked) {
                        hidePopupWindow();
                    } else {
                        mPopupWindowContent.showView(tag);
                        showPopupWindow();
                    }
                    updateTitleStates(tag, isChecked);
                }
            }
        });

        addPopupWindowContent(filterItemView);
    }

    private void updateTitleStates(String tag, boolean isChecked) {
        if(LangUtils.isEmpty(tag)) {
            return;
        }

        View child = findViewWithTag(tag);
        if(child == null || !(child instanceof FilterTitleItemView)) {
            return;
        }
        FilterTitleItemView itemView = (FilterTitleItemView) child;
        if(mCurrentCheckTitleTag != null && !tag.equals(mCurrentCheckTitleTag)) {
            FilterTitleItemView itemView1 = (FilterTitleItemView) findViewWithTag(mCurrentCheckTitleTag);
            itemView1.resetTitleItem();
        }

        itemView.onItemClick(!isChecked);
        mCurrentCheckTitleTag = tag;
    }
    /**
     * 生成filterTitle
     *
     * @param title title名称
     * @return filterTitle
     */
    private FilterTitleItemView createTitleItemView(String title) {
        FilterTitleItemView itemView = new FilterTitleItemView(getContext());
        itemView.setData(title);

        return itemView;
    }

    /**
     * 向popupWindow中添加View
     *
     * @param itemView filterItemView
     */
    private void addPopupWindowContent(FilterItemView itemView) {
        if(mPopupWindowContent == null || itemView == null) {
            return;
        }
        int itemHeight = itemView.getItemHeight();
        if(itemHeight > MAX_CONTENT_HEIGHT) {
            itemHeight = MAX_CONTENT_HEIGHT;
        }
        itemView.setOnItemClickListener(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1, itemHeight);
        mPopupWindowContent.addChildView(itemView, params);
    }

    /**
     * 显示popupWindow
     */
    private void showPopupWindow() {
        if (mPopupWindow.isShowing()) {
            return;
        }
        mPopupWindow.showAsDropDown(this, 0, 0);
    }

    /**
     * 关闭popupWindow
     */
    public void hidePopupWindow() {
        if (mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
        updateTitleStates(mCurrentCheckTitleTag, true);
    }

    @Override
    public void onFilterItemClick(String tag, FilterData data, int type) {
        if(LangUtils.isEmpty(tag) || data == null) {
            return;
        }

        LogUtils.e("itemClick -> " + tag);

        FilterTitleItemView titleItemView = (FilterTitleItemView)findViewWithTag(tag);
        if(type == FilterItemView.MULTI_COLUMN_TYPE) {
            MultiColumnItemData itemData = (MultiColumnItemData) data;
            if(isDefaultName(itemData.name) && itemData.parentData != null) {
                String name = itemData.parentData.name + "/" + itemData.name;
                titleItemView.setTitle(name);
            } else {
                titleItemView.setTitle(data.name);
            }
        } else {
            if(DEFAULT_ALL_TITLE.equals(data.name)) {
                titleItemView.setTitle(tag);
            } else {
                titleItemView.setTitle(data.name);
            }
        }

        titleItemView.resetTitleItem();
        hidePopupWindow();
    }

    private boolean isDefaultName(String name) {
        return DEFAULT_ALL_TITLE.equals(name) || DEFAULT_OTHER_TITLE.equals(name);
    }

    /**
     * popupWindow的显示页面
     */
    public static class PopupContentView extends RelativeLayout {
        public RelativeLayout rlContent;
        private String currentShowViewTag;

        public PopupContentView(Context context) {
            super(context);
            inflate(context, R.layout.filter_popupwindow_content, this);
            rlContent = (RelativeLayout) findViewById(R.id.rl_content);
        }

        public String getCurrentShowViewTag() {
            return currentShowViewTag;
        }

        public void showView(String tag) {
            if (LangUtils.isEmpty(tag)) {
                return;
            }

            for (int i = 0; i < rlContent.getChildCount(); i++) {
                FilterItemView view = (FilterItemView) rlContent.getChildAt(i);
                if (tag.equals(view.getTitle())) {
                    view.setVisibility(VISIBLE);
                    setItemViewLayoutParams(view);
                } else {
                    view.setVisibility(GONE);
                }
            }
            currentShowViewTag = tag;
        }

        public void addChildView(View view, ViewGroup.LayoutParams params) {
            rlContent.addView(view, params);
        }

        private void setItemViewLayoutParams(FilterItemView itemView) {
            if(itemView == null) {
                return;
            }

            int height = itemView.getItemHeight();
            if(height > MAX_CONTENT_HEIGHT) {
                height = MAX_CONTENT_HEIGHT;
            }
            ViewGroup.LayoutParams params = itemView.getLayoutParams();
            if(params == null) {
                params = new RelativeLayout.LayoutParams(-1, height);
            }
            params.height = height;
            itemView.setLayoutParams(params);
        }
    }
}
