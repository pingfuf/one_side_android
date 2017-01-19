package com.oneside.base.utils;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import com.oneside.base.view.XLoadingView;
import com.oneside.base.view.XNetErrorView;
import com.oneside.base.view.XNoDataView;
import com.oneside.utils.LangUtils;
import com.oneside.R;

/**
 * 页面状态转换工具
 *
 * User: XXoneside(www.xxoneside.com)
 * Date: 2016-05-09
 * Time: 11:22
 * Author: pingfu
 */
public class BusinessStateHelper {
    private Context mContext;

    //真正显示的View，一般是ListView
    private View mContentView;
    private ViewGroup mParentView;

    //加载动画View
    private XLoadingView mLoadingView;

    //网络失败View
    private XNetErrorView mNetErrorView;

    //没有数据View
    private XNoDataView mNoDataView;

    private View mCurrentVisibleView;
    private ViewGroup.LayoutParams mContentParam;

    private BusinessStateHelper(Context context) {
        mContext = context;
    }

    /**
     * 生成BusinessStateHelper
     *
     * @param context      context
     * @param contentView  contentView，一般是ListView
     *
     * @return BusinessStateHelper
     */
    public static BusinessStateHelper build(Context context, View contentView) {

        return build(context, contentView, null);
    }

    /**
     * 生成BusinessStateHelper
     *
     * @param context      context
     * @param contentView  contentView，一般是ListView
     *
     * @return BusinessStateHelper
     */
    public static BusinessStateHelper build(Context context, View contentView, View.OnClickListener netErrorRetryListener) {
        return build(context, contentView, netErrorRetryListener, null);
    }

    /**
     * 生成BusinessStateHelper
     *
     * @param context      context
     * @param contentView  contentView，一般是ListView
     *
     * @return BusinessStateHelper
     */
    public static BusinessStateHelper build(Context context, View contentView,
                                            View.OnClickListener netErrorRetryListener, View.OnClickListener noDataRetryListener){
        BusinessStateHelper stateHelper = new BusinessStateHelper(context);
        stateHelper.mContentView = contentView;
        if(contentView != null) {
            stateHelper.mContentParam = contentView.getLayoutParams();
            stateHelper.mParentView = (ViewGroup)contentView.getParent();
        }

        if(netErrorRetryListener != null) {
            stateHelper.setNetErrorRetryListener(netErrorRetryListener);
        }

        if(noDataRetryListener != null) {
            stateHelper.setNoDataRetryListener(noDataRetryListener);
        }

        return stateHelper;
    }

    /**
     * 设置状态
     *
     * @param state 状态
     */
    public void setState(BusinessState state) {
        switch (state) {
            case LOADING:
                if(mLoadingView == null) {
                    mLoadingView = new XLoadingView(mContext);
                }
                removeView(mCurrentVisibleView);
                showView(mLoadingView);
                break;
            case SUCCESS:
                removeView(mCurrentVisibleView);
                showView(mContentView);
                break;
            case NET_ERROR:
            case NO_NETWORK:
                if(mNetErrorView == null) {
                    mNetErrorView = new XNetErrorView(mContext);
                }
                removeView(mCurrentVisibleView);
                showView(mNetErrorView);
                break;
            case NO_DATA:
                if(mNoDataView == null) {
                    mNoDataView = new XNoDataView(mContext);
                }
                removeView(mCurrentVisibleView);
                showView(mNoDataView);
                break;
            case FINISHED:
                removeView(mCurrentVisibleView);
                showView(mContentView);
                break;
            default:
                break;

        }
    }

    /**
     * ListView的状态
     */
    public enum BusinessState {
        LOADING,
        NET_ERROR,
        NO_DATA,
        NO_NETWORK,
        FINISHED,
        SUCCESS
    }

    private void showView(View view) {
        if(view == null) {
            return;
        }

        if(view == mContentView) {
            mContentView.setVisibility(View.VISIBLE);
            removeView(mCurrentVisibleView);
            return;
        }

        if(view.getParent() == mParentView) {
            return;
        }

        mCurrentVisibleView = view;
        mParentView.addView(view, mContentParam);
        mContentView.setVisibility(View.GONE);
    }

    private void removeView(View view) {
        if(view == mContentView || view == null) {
            return;
        }

        if(mCurrentVisibleView == view) {
            mCurrentVisibleView = null;
        }

        if(view.getParent() == mParentView) {
            mParentView.removeView(view);
        }
    }

    /**
     * 在没有数据的情况下，设置没有数据的点击重试事件
     *
     * @param listener 点击重试
     */
    public void setNoDataRetryListener(View.OnClickListener listener) {
        if(mNoDataView == null) {
            mNoDataView = new XNoDataView(mContext);
        }

        mNoDataView.setReloadClickListener(listener);
    }

    /**
     * 在网络异常的情况下，设置没有数据的点击重试事件
     *
     * @param listener 点击重试
     */
    public void setNetErrorRetryListener(View.OnClickListener listener) {
        if(mNetErrorView == null) {
            mNetErrorView = new XNetErrorView(mContext);
        }

        mNetErrorView.setReloadClickListener(listener);
    }

    public void setNoDataViewBackgroundColor(int color) {
        if(mNoDataView == null) {
            mNoDataView = new XNoDataView(mContext);
        }
        mNoDataView.setBackgroundColor(color);
    }

    public void setNoDataImage(int resId) {
        if(mNoDataView == null) {
            mNoDataView = new XNoDataView(mContext);
        }

        try {
            mNoDataView.getNoDataImageView().setImageDrawable(mContext.getResources().getDrawable(resId));
//            mNoDataView.getNoDataImageView().setBackgroundResource(resId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setNoDataMessage(String message) {
        //默认的字体是14号字体
        setNoDataMessage(message, 14);
    }

    public void setNoDataMessage(String message, int textSize) {
        if(mNoDataView == null) {
            mNoDataView = new XNoDataView(mContext);
        }

        mNoDataView.getMessageView().setText(message);
        mNoDataView.getMessageView().setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    }

    /**
     * 设置重新请求按钮的提示文案，如果文案为空，不显示重新请求按钮
     *
     * @param msg 重新请求按钮文案
     */
    public void setNoDataButtonText(String msg) {
        if(mNoDataView == null) {
            mNoDataView = new XNoDataView(mContext);
        }

        mNoDataView.getReLoadButton().setVisibility(LangUtils.isEmpty(msg) ? View.GONE : View.VISIBLE);
        mNoDataView.getReLoadButton().setText(msg);
    }

    public void setLayoutParam(ViewGroup.LayoutParams params) {
        if(params == null) {
            return;
        }
        mContentParam = params;
    }

    public void showCourseItem() {
        if(mNoDataView != null) {
            mNoDataView.vCourseBottom.setVisibility(View.VISIBLE);
        }
    }
}
