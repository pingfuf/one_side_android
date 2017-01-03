package com.oneside.ui.view;

/**
 * Created by ZhanTao on 12/7/15.
 */

import android.content.Context;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.OverScroller;
import android.widget.ScrollView;

import com.oneside.utils.LogUtils;
import com.oneside.utils.ViewUtils;
import com.oneside.R;

public class StickyNavLayout extends LinearLayout {

    private View mTop;
    private View mNav;
    private ViewPager mViewPager;

    private int mTopViewHeight;
    private ViewGroup mInnerScrollView;
    private boolean isTopHidden = false;
    private boolean isTopPartlyHidden = false;

    private OverScroller mScroller;
    private VelocityTracker mVelocityTracker;
    private int mTouchSlop;
    private int mMaximumVelocity, mMinimumVelocity;

    private float mLastY;
    private float mLastX;
    private boolean mDragging;

    private boolean isInControl = false;

    private float mOriginalStartY;
    private int mTitleAndNavHeight;

    public StickyNavLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);

        mScroller = new OverScroller(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mMaximumVelocity = ViewConfiguration.get(context)
                .getScaledMaximumFlingVelocity();
        mMinimumVelocity = ViewConfiguration.get(context)
                .getScaledMinimumFlingVelocity();

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTop = findViewById(R.id.id_stickynavlayout_topview);
        mNav = findViewById(R.id.id_stickynavlayout_indicator);
        View view = findViewById(R.id.id_stickynavlayout_viewpager);
        if (!(view instanceof ViewPager)) {
            throw new RuntimeException(
                    "id_stickynavlayout_viewpager show used by ViewPager !");
        }
        mViewPager = (ViewPager) view;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ViewGroup.LayoutParams params = mViewPager.getLayoutParams();
        mTitleAndNavHeight = (int) (mNav.getMeasuredHeight()
                + getResources().getDimension(R.dimen.user_home_title_height));
        params.height = getMeasuredHeight() - mTitleAndNavHeight;

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mTopViewHeight = (int) (mTop.getMeasuredHeight()
                    - getResources().getDimension(R.dimen.user_home_title_height)
                    - ViewUtils.getStatusBarHeight(getContext()))/*getResources().getDimension(R.dimen.status_bar_height))*/;
        } else {
            mTopViewHeight = (int) (mTop.getMeasuredHeight()
                    - getResources().getDimension(R.dimen.user_home_title_height));
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        float y = ev.getY();
        float x = ev.getX();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastY = y;
                mLastX = x;
                break;
            case MotionEvent.ACTION_MOVE:
                float dy = y - mLastY;
                float dx = Math.abs(x - mLastX);
                getCurrentScrollView();
//                LogUtils.d("v3030 isTopHidden=%s", isTopHidden);

//                mViewPager.requestDisallowInterceptTouchEvent(isTopHidden && dx > mTouchSlop ? true : false);
                if (mInnerScrollView instanceof ScrollView) {
                    if (mInnerScrollView.getScrollY() == 0 && isTopHidden && dy > 0
                            && !isInControl) {
                        isInControl = true;
                        ev.setAction(MotionEvent.ACTION_CANCEL);
                        MotionEvent ev2 = MotionEvent.obtain(ev);
                        dispatchTouchEvent(ev);
                        ev2.setAction(MotionEvent.ACTION_DOWN);
                        return dispatchTouchEvent(ev2);
                    }
                } else if (mInnerScrollView instanceof ListView) {

                    ListView lv = (ListView) mInnerScrollView;
                    View c = lv.getChildAt(lv.getFirstVisiblePosition());

                    if (!isInControl && c != null && c.getTop() == 0 && isTopHidden
                            && dy > 0) {
                        LogUtils.d("2244 dispatchTouchEvent isInControl = true;");
                        isInControl = true;
                        ev.setAction(MotionEvent.ACTION_CANCEL);
                        MotionEvent ev2 = MotionEvent.obtain(ev);
                        dispatchTouchEvent(ev);
                        ev2.setAction(MotionEvent.ACTION_DOWN);
                        return dispatchTouchEvent(ev2);
                    }
                } else if (mInnerScrollView instanceof RecyclerView) {

                    RecyclerView rv = (RecyclerView) mInnerScrollView;

                    if (!isInControl && android.support.v4.view.ViewCompat.canScrollVertically(rv, -1) && isTopHidden
                            && dy > 0) {
                        isInControl = true;
                        ev.setAction(MotionEvent.ACTION_CANCEL);
                        MotionEvent ev2 = MotionEvent.obtain(ev);
                        dispatchTouchEvent(ev);
                        ev2.setAction(MotionEvent.ACTION_DOWN);
                        return dispatchTouchEvent(ev2);
                    }
                } else if (mInnerScrollView instanceof GridView) {
                    GridView gv = (GridView) mInnerScrollView;
                    View c = gv.getChildAt(gv.getFirstVisiblePosition());

                    if (!isInControl && c != null && c.getTop() == 0 && isTopHidden
                            && dy > 0) {
                        isInControl = true;
                        ev.setAction(MotionEvent.ACTION_CANCEL);
                        MotionEvent ev2 = MotionEvent.obtain(ev);
                        dispatchTouchEvent(ev);
                        ev2.setAction(MotionEvent.ACTION_DOWN);
                        return dispatchTouchEvent(ev2);
                    }
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     *
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        float y = ev.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mOriginalStartY = mLastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                float dy = y - mLastY;
                getCurrentScrollView();
                if (Math.abs(dy) > mTouchSlop) {
                    mDragging = true;
                    if (mInnerScrollView instanceof ScrollView) {
                        // 如果topView没有隐藏
                        // 或sc的scrollY = 0 && topView隐藏 && 下拉，则拦截
                        if ((!isTopHidden && !(mInnerScrollView.getScrollY() == 0 && dy > 0))
                                || isTopPartlyHidden
                                || (mInnerScrollView.getScrollY() == 0
                                && isTopHidden && dy > 0)) {

                            initVelocityTrackerIfNotExists();
                            mVelocityTracker.addMovement(ev);
                            mLastY = y;
                            return true;
                        }
                    } else if (mInnerScrollView instanceof ListView) {

                        ListView lv = (ListView) mInnerScrollView;
                        View c = lv.getChildAt(lv.getFirstVisiblePosition());
                        // 拦截条件：1、topView没有隐藏 && 上拉；
                        // 2、topView部分隐藏；
                        // 3、topView隐藏 && 下拉 && (sc的listView在顶部 || 按住titleOrNav)

                        LogUtils.d("2244 isTopHidden=%s; c.getTop()=%s; dy=%s; mOriginalStartY=%s; mTitleAndNavHeight=%s",
                                isTopHidden, c != null ? c.getTop() : 77, dy, mOriginalStartY, mTitleAndNavHeight);
                        if ((!isTopHidden && dy < 0)
                                || isTopPartlyHidden
                                || (isTopHidden && dy > 0 &&
                                ((c != null && c.getTop() == 0)) || (mOriginalStartY > 0 && mOriginalStartY < mTitleAndNavHeight))) {

                            LogUtils.d("2244 onInterceptTouchEvent return true");
                            initVelocityTrackerIfNotExists();
                            mVelocityTracker.addMovement(ev);
                            mLastY = y;
                            return true;
                        }
                    } else if (mInnerScrollView instanceof RecyclerView) {
                        RecyclerView rv = (RecyclerView) mInnerScrollView;

                        // 拦截条件：1、topView没有隐藏 && 上拉；
                        // 2、topView部分隐藏；
                        // 3、topView隐藏 && 下拉 && (sc的listView在顶部 || 按住titleOrNav)
                        if ((!isTopHidden && dy < 0)
                                || isTopPartlyHidden
                                || (isTopHidden && dy > 0 &&
                                (android.support.v4.view.ViewCompat.canScrollVertically(rv, -1))
                                || (mOriginalStartY > 0 && mOriginalStartY < mTitleAndNavHeight))) {
                            initVelocityTrackerIfNotExists();
                            mVelocityTracker.addMovement(ev);
                            mLastY = y;
                            return true;
                        }
                    } else if (mInnerScrollView instanceof GridView) {
                        GridView gv = (GridView) mInnerScrollView;
                        View c = gv.getChildAt(gv.getFirstVisiblePosition());
                        // 如果topView没有隐藏
                        // 或sc的listView在顶部 && topView隐藏 && 下拉，则拦截


                        if ((!isTopHidden && dy < 0)
                                || isTopPartlyHidden
                                || (isTopHidden && dy > 0 &&
                                ((c != null && c.getTop() == 0)) || (mOriginalStartY > 0 && mOriginalStartY < mTitleAndNavHeight))) {

                            LogUtils.d("2244 onInterceptTouchEvent return true");
                            initVelocityTrackerIfNotExists();
                            mVelocityTracker.addMovement(ev);
                            mLastY = y;
                            return true;
                        }
                    }

                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mDragging = false;
                recycleVelocityTracker();
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    private void getCurrentScrollView() {

        int currentItem = mViewPager.getCurrentItem();
        PagerAdapter a = mViewPager.getAdapter();
        if (a instanceof FragmentPagerAdapter) {
            FragmentPagerAdapter fadapter = (FragmentPagerAdapter) a;
            Fragment item = (Fragment) fadapter.instantiateItem(mViewPager,
                    currentItem);
            mInnerScrollView = (ViewGroup) (item.getView()
                    .findViewById(R.id.id_stickynavlayout_innerscrollview));
        } else if (a instanceof FragmentStatePagerAdapter) {
            FragmentStatePagerAdapter fsAdapter = (FragmentStatePagerAdapter) a;
            Fragment item = (Fragment) fsAdapter.instantiateItem(mViewPager,
                    currentItem);
            mInnerScrollView = (ViewGroup) (item.getView()
                    .findViewById(R.id.id_stickynavlayout_innerscrollview));
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        initVelocityTrackerIfNotExists();
        mVelocityTracker.addMovement(event);
        int action = event.getAction();
        float y = event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished())
                    mScroller.abortAnimation();
                mLastY = y;
                return true;
            case MotionEvent.ACTION_MOVE:
                float dy = y - mLastY;

//                Log.e("TAG", "dy = " + dy + " , y = " + y + " , mLastY = " + mLastY);

                if (!mDragging && Math.abs(dy) > mTouchSlop) {
                    mDragging = true;
                }
                if (mDragging) {
                    scrollBy(0, (int) -dy);

                    // 如果topView隐藏，且上滑动时，则改变当前事件为ACTION_DOWN
                    if ((getScrollY() == mTopViewHeight && dy < 0)
                            || (getScrollY() <= 0 && dy > 0)) {
                        LogUtils.d("2244 onTouchEvent dispatchTouchEvent(event);");
                        event.setAction(MotionEvent.ACTION_DOWN);
                        dispatchTouchEvent(event);
                        isInControl = false;
                    }
                }

                mLastY = y;
                break;
            case MotionEvent.ACTION_CANCEL:
                mDragging = false;
                recycleVelocityTracker();
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                break;
            case MotionEvent.ACTION_UP:
                mDragging = false;
                mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                int velocityY = (int) mVelocityTracker.getYVelocity();
                if (Math.abs(velocityY) > mMinimumVelocity) {
                    fling(-velocityY);
                }
                recycleVelocityTracker();
                break;
        }

        return super.onTouchEvent(event);
    }

    public void fling(int velocityY) {
        mScroller.fling(0, getScrollY(), 0, velocityY, 0, 0, 0, mTopViewHeight);
        invalidate();
    }

    @Override
    public void scrollTo(int x, int y) {
        if (y < 0) {
            y = 0;
        }
        if (y > mTopViewHeight) {
            y = mTopViewHeight;
        }
        if (y != getScrollY()) {
            super.scrollTo(x, y);
        }

//        LogUtils.d("2244 scrollTo getScrollY()=%s; mTopViewHeight=%s", getScrollY(), mTopViewHeight);

        isTopHidden = getScrollY() == mTopViewHeight;
        isTopPartlyHidden = getScrollY() < mTopViewHeight && getScrollY() > 0;

        if (mScrollListener != null) {
            mScrollListener.showTitle(getScrollY(), mTopViewHeight);
        }
    }

    private ScrollListener mScrollListener;

    public void setOnScrollListener(ScrollListener scrollListener) {
        mScrollListener = scrollListener;
    }

    public static interface ScrollListener {
        void showTitle(int currentScrollY, int topHeight);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(0, mScroller.getCurrY());
            invalidate();
        }
    }

    private void initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
    }

    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

}