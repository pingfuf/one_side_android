package com.oneside.manager;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import com.oneside.utils.SysUtils;

/**
 * User: XXoneside(www.xxoneside.com)
 * Date: 2016-09-23
 * Time: 14:35
 * Author: pingfu
 * FIXME
 */
public class XSoftKeyboardManager {
    private static final int VISIABLE_SIZE = 50;

    private Activity mActivity;
    private OnSoftKeyboardShowOrHideListener mSoftKeyboardShowOrHideListener;
    private int softKeyboardHeight;
    private boolean isSoftKeyboardShowing;

    private XSoftKeyboardManager() {

    }

    public static XSoftKeyboardManager build(Activity activity, OnSoftKeyboardShowOrHideListener listener) {
        XSoftKeyboardManager manager = new XSoftKeyboardManager();
        manager.mActivity = activity;
        manager.mSoftKeyboardShowOrHideListener = listener;
        manager.setSoftKeyboardEvent(activity);

        return manager;
    }

    private void setSoftKeyboardEvent(Activity activity) {
        if (activity == null || activity.isFinishing()) {
            return;
        }
        final Window mRootWindow = activity.getWindow();
        View mRootView = mRootWindow.getDecorView().findViewById(android.R.id.content);
        mRootView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (mSoftKeyboardShowOrHideListener == null) {
                            return;
                        }
                        Rect r = new Rect();
                        View view = mRootWindow.getDecorView();
                        view.getWindowVisibleDisplayFrame(r);

                        if (SysUtils.HEIGHT - r.bottom > VISIABLE_SIZE) {
                            softKeyboardHeight = SysUtils.HEIGHT - r.bottom;
                            isSoftKeyboardShowing = true;
                            mSoftKeyboardShowOrHideListener.onKeyboardShow();
                        } else {
                            isSoftKeyboardShowing = false;
                            mSoftKeyboardShowOrHideListener.onKeyboardHide();
                        }
                    }
                }
        );
    }

    public int getSoftKeyboardHeight() {
        return softKeyboardHeight;
    }

    public boolean isSoftKeyboardShowing() {
        return isSoftKeyboardShowing;
    }

    public void showSoftKeyboard(View target) {
        if(mActivity == null || mActivity.isFinishing()) {
            return;
        }

        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(target, InputMethodManager.SHOW_FORCED);
    }

    public void hideSoftKeyboard(View target) {
        if(mActivity == null || mActivity.isFinishing()) {
            return;
        }

        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(target.getWindowToken(), 0);
    }

    /**
     * 软键盘的显示和隐藏事件监听函数
     */
    public interface OnSoftKeyboardShowOrHideListener {
        void onKeyboardShow();

        void onKeyboardHide();
    }
}
