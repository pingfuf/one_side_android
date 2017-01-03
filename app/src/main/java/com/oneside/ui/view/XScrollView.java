package com.oneside.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;

import com.oneside.utils.ViewUtils;
import com.oneside.R;

public class XScrollView extends ScrollView implements OnScrollListener {

    private float mLastY = -1;
    private Scroller mScroller;

    private OnScrollListener mScrollListener;

    private IXScrollViewListener mScorllViewListener;

    private XListViewHeader mHeaderView;
    private RelativeLayout mHeaderViewContent;
    private RelativeLayout mFooterViewContent;
    private TextView mHeaderTimeView;
    private int mHeaderViewHeight = ViewUtils.rp(60);
    private boolean mEnablePullRefresh = true;
    private boolean mPullRefreshing = false;

    private RelativeLayout mScrollView;
    private RelativeLayout mContentView;

    private XListViewFooter mFooterView;
    private boolean mEnablePullLoad;
    private boolean mPullLoading;

    private int mTotalItemCount;

    private int mScrollBack;
    private final static int SCROLLBACK_HEADER = 0;
    private final static int SCROLLBACK_FOOTER = 1;

    private final static int SCROLL_DURATION = 400;
    private final static int PULL_LOAD_MORE_DELTA = 50;
    private final static float OFFSET_RADIO = 1.8f;

    public XScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWithContext(context);
    }

    private void initWithContext(Context context) {
        mScrollView = (RelativeLayout) View.inflate(context, R.layout.xscrollview_layout, null);

        mContentView = (RelativeLayout) mScrollView.findViewById(R.id.xscrollview_content);
        mScroller = new Scroller(context, new DecelerateInterpolator());
        this.setOnScrollListener(this);

        mHeaderView = new XListViewHeader(context);
        addHeaderView(mHeaderView);

        mFooterView = new XListViewFooter(context);
        addFooterView(mFooterView);

        this.addView(mScrollView);
    }

    private void addHeaderView(XListViewHeader mHeaderView) {
        if (mScrollView == null) {
            return;
        }
        mHeaderViewContent = (RelativeLayout) mScrollView.findViewById(R.id.xscrollview_header_content);
        android.widget.RelativeLayout.LayoutParams para =
                new android.widget.RelativeLayout.LayoutParams(
                        android.widget.RelativeLayout.LayoutParams.MATCH_PARENT,
                        android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
        mHeaderViewContent.bringToFront();
        mHeaderViewContent.addView(mHeaderView, para);
    }

    private void addFooterView(XListViewFooter mFooterView) {
        if (mScrollView == null) {
            return;
        }
        mFooterViewContent = (RelativeLayout) mScrollView.findViewById(R.id.xscrollview_footer_content);
        mFooterViewContent.addView(mFooterView);
    }

    public void setContentView(ViewGroup contentView) {
        if (mScrollView == null) {
            return;
        }
        if (mContentView == null) {
            mContentView = (RelativeLayout) mScrollView.findViewById(R.id.xscrollview_content);
        }

        if (mContentView.getChildCount() > 0) {
            mContentView.removeAllViews();
        }
        mContentView.addView(contentView);
    }

    public void setView(View contentView) {
        if (mScrollView == null) {
            return;
        }
        if (mContentView == null) {
            mContentView = (RelativeLayout) mScrollView.findViewById(R.id.xscrollview_content);
        }
        mContentView.addView(contentView);
    }

    public void setPullRefreshEnable(boolean enable) {
        mEnablePullRefresh = enable;
        if (!mEnablePullRefresh) {
            mHeaderViewContent.setVisibility(View.INVISIBLE);
        } else {
            mHeaderViewContent.setVisibility(View.VISIBLE);
        }
    }

    /**
     * enable or disable pull up load more feature.
     *
     * @param enable
     */
    public void setPullLoadEnable(boolean enable) {
        mEnablePullLoad = enable;
        if (!mEnablePullLoad) {
            mFooterView.hide();
            mFooterView.setOnClickListener(null);
            mFooterViewContent.setVisibility(View.GONE);
        } else {
            mPullLoading = false;
            mFooterViewContent.setVisibility(View.VISIBLE);
            mFooterView.show();
            mFooterView.setState(XListViewFooter.STATE_NORMAL);
            mFooterView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    startLoadMore();
                }
            });
        }
    }

    /**
     * stop refresh, reset header view.
     */
    public void stopRefresh() {
        if (mPullRefreshing == true) {
            mPullRefreshing = false;
            resetHeaderHeight();
        }
    }

    /**
     * stop load more, reset footer view.
     */
    public void stopLoadMore() {
        if (mPullLoading == true) {
            mPullLoading = false;
            mFooterView.setState(XListViewFooter.STATE_NORMAL);
        }
    }

    /**
     * set last refresh time
     *
     * @param time
     */
    public void setRefreshTime(String time) {
        mHeaderTimeView.setText(time);
    }

    private void invokeOnScrolling() {
        if (mScrollListener instanceof OnXScrollListener) {
            OnXScrollListener l = (OnXScrollListener) mScrollListener;
            l.onXScrolling(this);
        }
    }

    private void updateHeaderHeight(float delta) {
        mHeaderView.setVisibleHeight((int) delta + mHeaderView.getVisibleHeight());
        if (mEnablePullRefresh && !mPullRefreshing) {
            if (mHeaderView.getVisibleHeight() > mHeaderViewHeight * 3 / 4) {
                mHeaderView.setState(XListViewHeader.STATE_READY);
            } else {
                mHeaderView.setState(XListViewHeader.STATE_NORMAL);
            }
        }
    }

    /**
     * reset header view's height.
     */
    private void resetHeaderHeight() {
        int height = mHeaderView.getVisibleHeight();
        if (height == 0)
            return;
        if (mPullRefreshing && height <= mHeaderViewHeight) {
            return;
        }
        int finalHeight = 0;
        if (mPullRefreshing && height > mHeaderViewHeight) {
            finalHeight = mHeaderViewHeight;
        }
        mScrollBack = SCROLLBACK_HEADER;
        mScroller.startScroll(0, height, 0, finalHeight - height, SCROLL_DURATION);
        invalidate();
    }

    private void updateFooterHeight(float delta) {
        int height = mFooterView.getBottomMargin() + (int) delta;
        if (mEnablePullLoad && !mPullLoading) {
            if (height > PULL_LOAD_MORE_DELTA) {
                mFooterView.setState(XListViewFooter.STATE_READY);
            } else {
                mFooterView.setState(XListViewFooter.STATE_NORMAL);
            }
        }
        mFooterView.setBottomMargin(height);

    }

    private void resetFooterHeight() {
        int bottomMargin = mFooterView.getBottomMargin();
        if (bottomMargin > 0) {
            mScrollBack = SCROLLBACK_FOOTER;
            mScroller.startScroll(0, bottomMargin, 0, -bottomMargin, SCROLL_DURATION);
            invalidate();
        }
    }

    private void startRefresh() {
        mPullRefreshing = true;
        mHeaderView.setState(XListViewHeader.STATE_REFRESHING);
        if (mScorllViewListener != null) {
            mScorllViewListener.onRefresh();
        }
        updateHeaderHeight(mHeaderViewHeight);
        invokeOnScrolling();
    }

    private void startLoadMore() {
        mPullLoading = true;
        mFooterView.setState(XListViewFooter.STATE_LOADING);
        if (mScorllViewListener != null) {
            mScorllViewListener.onLoadMore();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Log.e("LIFE", "onTouchEvent(MotionEvent ev)");
        if (mLastY == -1) {
            mLastY = ev.getRawY();
        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaY = ev.getRawY() - mLastY;
                mLastY = ev.getRawY();
                if (getFirstVisiblePosition() == 0 && (mHeaderView.getVisibleHeight() > 0 || deltaY > 0)) {
                    updateHeaderHeight(deltaY / OFFSET_RADIO);
                    invokeOnScrolling();
                } else if (getLastVisiblePosition() == mTotalItemCount - 1
                        && (mFooterView.getBottomMargin() > 0 || deltaY < 0)) {
                    updateFooterHeight(-deltaY / OFFSET_RADIO);
                }
                break;
            default:
                mLastY = -1;
                if (getFirstVisiblePosition() == 0) {
                    if (mEnablePullRefresh && mHeaderView.getVisibleHeight() >= mHeaderViewHeight) {
                        mPullRefreshing = true;
                        mHeaderView.setState(XListViewHeader.STATE_REFRESHING);
                        if (mScorllViewListener != null) {
                            mScorllViewListener.onRefresh();
                        }
                    }
                    resetHeaderHeight();
                } else if (getLastVisiblePosition() == mTotalItemCount - 1) {
                    if (mEnablePullLoad && mFooterView.getBottomMargin() > PULL_LOAD_MORE_DELTA) {
                        startLoadMore();
                    }
                    resetFooterHeight();
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    private int getFirstVisiblePosition() {
        return this.getScrollY();
    }

    private int getLastVisiblePosition() {
        return 0;
    }

    /**
     * 由父视图调用用来请求子视图根据偏移值 mScrollX,mScrollY重新绘制
     **/
    @Override
    public void computeScroll() {
        Log.e("LIFE", "computeScroll() ");
        /**
         * 当startScroll执行过程中即在duration时间内，computeScrollOffset 方法会一直返回false，但当动画执行完成后会返回返加true.
         */
        if (mScroller.computeScrollOffset()) {
            if (mScrollBack == SCROLLBACK_HEADER) {
                mHeaderView.setVisibleHeight(mScroller.getCurrY());
            } else {
                mFooterView.setBottomMargin(mScroller.getCurrY());
            }
            postInvalidate();
            invokeOnScrolling();
        }
        super.computeScroll();
    }

    public void setOnScrollListener(OnScrollListener l) {
        Log.e("LIFE", "setOnScrollListener");
        mScrollListener = l;
    }

    /**
     * 正在滚动时回调，回调2-3次，手指没抛则回调2次。scrollState = 2的这次不回调 //回调顺序如下 //第1次：scrollState =
     * SCROLL_STATE_TOUCH_SCROLL(1) 正在滚动 //第2次：scrollState = SCROLL_STATE_FLING(2)
     * 手指做了抛的动作（手指离开屏幕前，用力滑了一下） //第3次：scrollState = SCROLL_STATE_IDLE(0) 停止滚动
     * //当屏幕停止滚动时为0；当屏幕滚动且用户使用的触碰或手指还在屏幕上时为1； //由于用户的操作，屏幕产生惯性滑动时为2 //当滚到最后一行且停止滚动时，执行加载
     **/

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        Log.e("LIFE", "onScrollStateChanged");
        if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
            Log.e("scrollState", "正在滚动 ");
        }
        if (scrollState == SCROLL_STATE_FLING) {
            Log.e("scrollState", " 手指做了抛的动作（手指离开屏幕前，用力滑了一下） ");
        }

        if (scrollState == SCROLL_STATE_IDLE) {
            Log.e("scrollState", "停止滚动     ");
        }
        if (mScrollListener != null) {
            mScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    /**
     * 滚动时一直回调，直到停止滚动时才停止回调。单击时回调一次。 firstVisibleItem：当前能看见的第一个列表项ID（从0开始）
     * visibleItemCount：当前能看见的列表项个数（小半个也算） totalItemCount：列表项共数
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                         int totalItemCount) {
        Log.e("LIFE", "onScroll");
        mTotalItemCount = totalItemCount;
        if (mScrollListener != null) {
            mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    public void setIXScrollViewListener(IXScrollViewListener l) {
        mScorllViewListener = l;
    }

    /**
     * you can listen ListView.OnScrollListener or this one. it will invoke onXScrolling when
     * header/footer scroll back.
     */
    public interface OnXScrollListener extends OnScrollListener {
        public void onXScrolling(View view);
    }

    /**
     * implements this interface to get refresh/load more event.
     */
    public interface IXScrollViewListener {
        public void onRefresh();

        public void onLoadMore();
    }
}
