package com.oneside.ui.view;

import static com.oneside.utils.ViewUtils.find;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.oneside.utils.SysUtils;
import com.oneside.R;

public class PopUpFromBottomView extends RelativeLayout implements View.OnClickListener {
    private Scroller mScroller;
    private int mScreenHeigh = 0;
    private int mScreenWidth = 0;
    private int downY = 0;
    private int moveY = 0;
    private int scrollY = 0;
    private int upY = 0;
    private Boolean isMoving = false;
    private int viewHeight = 0;
    public boolean isShow = false;
    public boolean mEnabled = true;
    public boolean mOutsideTouchable = true;
    private int mDuration = 800;

    public PopUpFromBottomView(Context context) {
        super(context);
        init(context);
    }

    public PopUpFromBottomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PopUpFromBottomView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void setView() {

    }

    private void init(Context context) {
        setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
        setFocusable(true);
        mScroller = new Scroller(context);
        mScreenHeigh = SysUtils.HEIGHT;
        mScreenWidth = SysUtils.WIDTH;
        initViews(context);
    }

    TextView share, delete, cancel;

    private void initViews(Context context) {
        final View view = LayoutInflater.from(context).inflate(R.layout.ui_circle_delete_layout, null);
        share = find(view, R.id.ui_circle_share);
        delete = find(view, R.id.ui_circle_delete);
        cancel = find(view, R.id.ui_circle_cancel);
        share.setOnClickListener(this);
        delete.setOnClickListener(this);
        cancel.setOnClickListener(this);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addView(view, params);
        this.setBackgroundColor(Color.argb(0, 0, 0, 0));
        view.post(new Runnable() {

            @Override
            public void run() {
                viewHeight = view.getHeight();
            }
        });
        PopUpFromBottomView.this.scrollTo(0, mScreenHeigh);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!mEnabled) {
            return false;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = (int) event.getY();
                if (isShow) {
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                moveY = (int) event.getY();
                scrollY = moveY - downY;
                if (scrollY > 0) {
                    if (isShow) {
                        scrollTo(0, -Math.abs(scrollY));
                    }
                } else {
                    if (mScreenHeigh - this.getTop() <= viewHeight && !isShow) {
                        scrollTo(0, Math.abs(viewHeight - scrollY));
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                upY = (int) event.getY();
                if (isShow) {
                    if (this.getScrollY() <= -(viewHeight / 2)) {
                        startMoveAnim(this.getScrollY(), -(viewHeight - this.getScrollY()), mDuration);
                        isShow = false;
                        Log.d("isShow", "false");
                    } else {
                        startMoveAnim(this.getScrollY(), -this.getScrollY(), mDuration);
                        isShow = true;
                        Log.d("isShow", "true");
                    }
                }
                Log.d("this.getScrollY()", "" + this.getScrollY());
                changed();
                break;
            case MotionEvent.ACTION_OUTSIDE:
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * @param startY
     * @param dy
     * @param duration
     */
    public void startMoveAnim(int startY, int dy, int duration) {
        isMoving = true;
        mScroller.startScroll(0, startY, 0, dy, duration);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
            isMoving = true;
        } else {
            isMoving = false;
        }
        super.computeScroll();
    }

    public void show() {
        if (!isShow && !isMoving) {
            PopUpFromBottomView.this.startMoveAnim(-viewHeight, viewHeight, mDuration);
            isShow = true;
            Log.d("isShow", "true");
            changed();
        }
    }

    public void dismiss() {
        if (isShow && !isMoving) {
            PopUpFromBottomView.this.startMoveAnim(0, -viewHeight, mDuration);
            isShow = false;
            changed();
        }
    }

    public boolean isShow() {
        return isShow;
    }

    public boolean isSlidingEnabled() {
        return mEnabled;
    }

    public void setSlidingEnabled(boolean enabled) {
        mEnabled = enabled;
    }

    public void setOnStatusListener(onStatusListener listener) {
        this.statusListener = listener;
    }

    public void setOutsideTouchable(boolean touchable) {
        mOutsideTouchable = touchable;
    }

    public void setOnPopClickListener(onPopClickListener l) {
        popClickListener = l;
    }

    public void changed() {
        if (statusListener != null) {
            if (isShow) {
                statusListener.onShow();
            } else {
                statusListener.onDismiss();
            }
        }
    }

    public onStatusListener statusListener;

    public interface onStatusListener {
        public void onShow();

        public void onDismiss();
    }

    public onPopClickListener popClickListener;

    public interface onPopClickListener {
        public void onDelete();

        public void onShare();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    public void onClick(View v) {
        if (v == share) {
            if (popClickListener != null) {
                popClickListener.onShare();
            }
        } else if (v == delete) {
            if (popClickListener != null) {
                popClickListener.onDelete();
            }
        } else if (v == cancel) {
            dismiss();
        }
    }
}
