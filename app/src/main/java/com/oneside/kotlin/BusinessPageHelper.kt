package com.oneside.kotlin

import android.content.Context
import android.view.View
import android.view.ViewGroup

/**
 * Created by fupingfu on 2017/9/18.
 */
object BusinessPageHelper {
    lateinit var mContext : Context

    lateinit var mContentView : View

    lateinit var mParentView : ViewGroup

    //加载页面
    lateinit var mLoadingView : View

    //没有数据页面
    lateinit var mNoDataView : View

    lateinit var mNetErrorView : View

    //当前显示的界面
    lateinit var mCurrentView : View

    lateinit var mState : PageState

    fun build(contentView: View) : BusinessPageHelper {
        mContentView = contentView
        mContext = mContentView?.context
        mParentView = mCurrentView?.parent as ViewGroup

        return this
    }

    fun setPageState(state : PageState) {
        if (state == PageState.LOADING) {
            showContentView()
        } else if (state == PageState.NET_ERROR) {

        }
    }

    private fun showContentView() {

    }

    private fun showView(view : View) {
        if (mCurrentView == mContentView) {
            mContentView.visibility = View.VISIBLE
            return
        }

        mContentView.visibility = View.GONE
        if (mCurrentView.parent != null && mCurrentView.parent == mParentView) {
            mParentView?.removeView(mCurrentView)
        }
    }

    enum class PageState {
        LOADING, NO_DATA, NET_ERROR, FINISHED
    }
}