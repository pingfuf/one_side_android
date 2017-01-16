package com.oneside.ui.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oneside.utils.LangUtils;
import com.oneside.utils.SysUtils;
import com.oneside.utils.ViewUtils;
import com.oneside.R;
import com.oneside.R.color;

import java.util.ArrayList;


public class ExpandTabView extends LinearLayout implements OnDismissListener {

    public static final int INDEX_OF_TIME = 0;
    public static final int INDEX_OF_BD = 1;
    public static final int INDEX_OF_GYM_TYPE = 2;

    private int mPopupViewDisplayWidth;
    private int mPopupViewDisplayHeight;

    private ArrayList<String> mTitleStrArray = new ArrayList<String>();

    private RelativeLayout mPopupViewLayout;
    private PopupWindow mPopupWindow;

    private ArrayList<Boolean> mArrowIsDownArray = new ArrayList<Boolean>();
    private ArrayList<TextView> mTitleTvArray = new ArrayList<TextView>();
    private ArrayList<RelativeLayout> mArrowLayoutArray = new ArrayList<RelativeLayout>();
    private ArrayList<ImageView> mArrowIvArray = new ArrayList<ImageView>();
    private ArrayList<ImageView> mArrowRedIvArray = new ArrayList<ImageView>();

    private int mLastSelectPosition = -1;
    private int mSelectPosition;
    private RelativeLayout mLayoutTitleAndArrowSelected;

    private static final int ANI_DURTION = 450;
    private static final String MAIN_ANI_NAME = "rotation";

    public ExpandTabView(Context context) {
        super(context);
        init(context);
    }

    public ExpandTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mPopupViewDisplayWidth = SysUtils.WIDTH;
        mPopupViewDisplayHeight = SysUtils.HEIGHT;
        setOrientation(LinearLayout.HORIZONTAL);
        setBackgroundResource(R.drawable.ic_normal_bg);
    }

    public void setValue(ArrayList<String> titleArray, ArrayList<LinearLayout> popviewArray) {
        if (LangUtils.isEmpty(popviewArray) || popviewArray.size() < 2)
            return;

        mTitleStrArray = titleArray;

        mPopupViewLayout = new RelativeLayout(getContext());
        LayoutInflater inflater =
                (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (mPopupWindow == null) {
            for (int i = 0; i < popviewArray.size(); i++) {
                RelativeLayout.LayoutParams popupViewLayoutParams =
                        new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                                (int) (mPopupViewDisplayHeight * 0.6));
                mPopupViewLayout.addView(popviewArray.get(i), popupViewLayoutParams);
            }
            if (mPopupViewLayout.getChildAt(INDEX_OF_TIME) != null)
                mPopupViewLayout.getChildAt(INDEX_OF_TIME).setVisibility(View.GONE);
            if (mPopupViewLayout.getChildAt(INDEX_OF_BD) != null)
                mPopupViewLayout.getChildAt(INDEX_OF_BD).setVisibility(View.GONE);
            if (mPopupViewLayout.getChildAt(INDEX_OF_GYM_TYPE) != null)
                mPopupViewLayout.getChildAt(INDEX_OF_GYM_TYPE).setVisibility(View.GONE);

            mPopupWindow =
                    new PopupWindow(mPopupViewLayout, mPopupViewDisplayWidth, mPopupViewDisplayHeight);
            mPopupWindow.setFocusable(false);
            mPopupWindow.setBackgroundDrawable(new ColorDrawable(getResources().getColor(
                    color.alpha_black)));
        }

        for (int i = 0; i < popviewArray.size(); i++) {
            mArrowIsDownArray.add(false);

            RelativeLayout layoutTitleAndArrow =
                    (RelativeLayout) inflater.inflate(R.layout.widget_expand_title_arrow, this, false);
            layoutTitleAndArrow.setTag(i);
            final TextView tvTitle = (TextView) layoutTitleAndArrow.findViewById(R.id.tv_choose_title);
            tvTitle.setTag(i);
            tvTitle.setText(mTitleStrArray.get(i));
            mTitleTvArray.add(tvTitle);

            RelativeLayout layoutArrow = (RelativeLayout) layoutTitleAndArrow.findViewById(R.id.layout_arrow);
            layoutArrow.setTag(i);
            mArrowLayoutArray.add(layoutArrow);
            ImageView ivArrow = (ImageView) layoutTitleAndArrow.findViewById(R.id.iv_choose_icon);
            ivArrow.setTag(i);
            ImageView ivArrowRed = (ImageView) layoutTitleAndArrow.findViewById(R.id.iv_choose_icon_red);
            ivArrowRed.setTag(i);
            mArrowIvArray.add(ivArrow);
            mArrowRedIvArray.add(ivArrowRed);
            addView(layoutTitleAndArrow);

            View lineView = new TextView(getContext());
            lineView.setBackgroundColor(getResources().getColor(color.line_gray));
            if (i < popviewArray.size() - 1) {
                LinearLayout.LayoutParams lineLayoutParams =
                        new LinearLayout.LayoutParams(ViewUtils.rp(1), LinearLayout.LayoutParams.MATCH_PARENT);
                lineLayoutParams.bottomMargin = ViewUtils.rp(12);
                lineLayoutParams.topMargin = ViewUtils.rp(12);
                addView(lineView, lineLayoutParams);
            }
            mPopupViewLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onPressBack();
                }
            });

            layoutTitleAndArrow.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    RelativeLayout rLayout = (RelativeLayout) view;

                    if (mLayoutTitleAndArrowSelected == null) {
                        mLayoutTitleAndArrowSelected = rLayout;
                        mSelectPosition = (Integer) rLayout.getTag();
                        mArrowIsDownArray.set(mSelectPosition, true);
                    } else {
                        if (!mLayoutTitleAndArrowSelected.equals(rLayout)) {
                            rotateUpAnimRun(mArrowLayoutArray.get(mSelectPosition), mSelectPosition);

                            mLastSelectPosition = (Integer) mLayoutTitleAndArrowSelected.getTag();
                            mArrowIsDownArray.set(mLastSelectPosition, false);
                            mLayoutTitleAndArrowSelected = rLayout;
                            mSelectPosition = (Integer) rLayout.getTag();
                            mArrowIsDownArray.set(mSelectPosition, true);
                        } else {
                            mSelectPosition = (Integer) rLayout.getTag();
                            mArrowIsDownArray.set(mSelectPosition, false);
                        }
                    }

                    startAnimation();
                }
            });
        }
    }

    public void setTabTitle(String valueText, int position) {
        if (position < mTitleTvArray.size()) {
            mTitleTvArray.get(position).setText(valueText);
        }
    }

    public String getTabTitle(int position) {
        if (position < mTitleTvArray.size()
                && mTitleTvArray.get(position).getText() != null) {
            return mTitleTvArray.get(position).getText().toString();
        }
        return "";
    }

    private void startAnimation() {
        if (mArrowIsDownArray.get(mSelectPosition)) {
            mArrowIsDownArray.set(mSelectPosition, false);
            updatePopupViewList();
            if (!mPopupWindow.isShowing()) {
                mPopupWindow.showAsDropDown(this, 0, 0);
                rotateDownAnimRun(mArrowLayoutArray.get(mSelectPosition), mSelectPosition);
                mTitleTvArray.get(mSelectPosition).setTextColor(
                        getResources().getColor(color.papaya_primary_color));
            } else {
                mArrowLayoutArray.get(mSelectPosition).clearAnimation();
                rotateDownAnimRun(mArrowLayoutArray.get(mSelectPosition), mSelectPosition);

                mTitleTvArray.get(mSelectPosition).setTextColor(
                        getResources().getColor(color.papaya_primary_color));

//          int another = (mSelectPosition + i) % mArrowLayoutArray.size();
                if (mLastSelectPosition != -1 && mLastSelectPosition != mSelectPosition) {
                    mArrowLayoutArray.get(mLastSelectPosition).clearAnimation();
                    rotateUpAnimRun(mArrowLayoutArray.get(mLastSelectPosition), mLastSelectPosition);
                    mTitleTvArray.get(mLastSelectPosition).setTextColor(getResources().getColor(color.text_color_deep_gray));

                }
            }
        } else {
            if (mPopupWindow.isShowing()) {
                mPopupWindow.dismiss();
                mArrowLayoutArray.get(mSelectPosition).clearAnimation();
                rotateUpAnimRun(mArrowLayoutArray.get(mSelectPosition), mSelectPosition);
                mTitleTvArray.get(mSelectPosition).setTextColor(
                        getResources().getColor(color.text_color_deep_gray));
                resetBackground();
            } else {
                mLayoutTitleAndArrowSelected = null;
                mArrowLayoutArray.get(mSelectPosition).clearAnimation();
                rotateUpAnimRun(mArrowLayoutArray.get(mSelectPosition), mSelectPosition);
                mTitleTvArray.get(mSelectPosition).setTextColor(
                        getResources().getColor(color.text_color_deep_gray));
            }
            mArrowIsDownArray.set(mSelectPosition, true);
        }
        mLastSelectPosition = mSelectPosition;
    }

    private void updatePopupViewList() {
        if (mPopupViewLayout.getChildAt(INDEX_OF_TIME) != null)
            mPopupViewLayout.getChildAt(INDEX_OF_TIME).setVisibility(View.GONE);
        if (mPopupViewLayout.getChildAt(INDEX_OF_BD) != null)
            mPopupViewLayout.getChildAt(INDEX_OF_BD).setVisibility(View.GONE);
        if (mPopupViewLayout.getChildAt(INDEX_OF_GYM_TYPE) != null)
            mPopupViewLayout.getChildAt(INDEX_OF_GYM_TYPE).setVisibility(View.GONE);

        mPopupViewLayout.getChildAt(mSelectPosition).setVisibility(View.VISIBLE);
        if (mSelectPosition == INDEX_OF_GYM_TYPE) {
        } else if (mSelectPosition == INDEX_OF_BD) {
        } else if (mSelectPosition == INDEX_OF_TIME) {
//      if(mPopupViewTime != null)
//        mPopupViewTime.defaultChooseListView();
        }
    }

    public boolean onPressBack() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {

            mPopupWindow.dismiss();
            mArrowLayoutArray.get(mSelectPosition).clearAnimation();
            rotateUpAnimRun(mArrowLayoutArray.get(mSelectPosition), mSelectPosition);
            mTitleTvArray.get(mSelectPosition).setTextColor(
                    getResources().getColor(color.text_color_deep_gray));
            resetBackground();
            return true;
        } else {// when touch outside is true, pop dismiss before onpresss back
            resetBackground();
            return false;
        }
    }

    private void resetBackground() {
        mLayoutTitleAndArrowSelected = null;
        mLastSelectPosition = -1;
//    setBackgroundResource(R.drawable.ic_normal_bg);
    }


    @Override
    public void onDismiss() {
//    setBackgroundResource(R.drawable.ic_normal_bg);
        // showPopup(mSelectPosition);
        // mPopupWindow.setOnDismissListener(null);
    }

    public void stopAnimation(int selectPosition) {
        if (selectPosition < 0) {
            for (ImageView imageView : mArrowIvArray) {
                imageView.clearAnimation();
            }
            for (ImageView imageView : mArrowRedIvArray) {
                imageView.clearAnimation();
            }
        } else {
            mArrowIvArray.get(selectPosition).clearAnimation();
            mArrowRedIvArray.get(selectPosition).clearAnimation();
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void rotateUpAnimRun(final View view, final int postion) {
        ObjectAnimator anim = ObjectAnimator//
                .ofFloat(view, MAIN_ANI_NAME, -180.0F, 0.0F)//
                .setDuration(ANI_DURTION);//
        anim.start();
        // final int i = (Integer) view.getTag();
        anim.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float cVal = Math.abs((Float) animation.getAnimatedValue() / 180);
                mArrowIvArray.get(postion).setAlpha(1 - cVal);
                mArrowRedIvArray.get(postion).setAlpha(cVal);
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void rotateDownAnimRun(final View view, final int postion) {
        ObjectAnimator anim = ObjectAnimator//
                .ofFloat(view, MAIN_ANI_NAME, 0.0F, -180.0F)//
                .setDuration(ANI_DURTION);//
        anim.start();
        // final int i = (Integer) view.getTag();
        anim.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float cVal = Math.abs((Float) animation.getAnimatedValue() / 180);
                mArrowIvArray.get(postion).setAlpha(1 - cVal);
                mArrowRedIvArray.get(postion).setAlpha(cVal);
            }
        });
    }


}
