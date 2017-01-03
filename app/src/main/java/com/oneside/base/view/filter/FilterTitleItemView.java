package com.oneside.base.view.filter;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oneside.base.inject.From;
import com.oneside.base.inject.InjectUtils;
import com.oneside.R;

/**
 * FilterTitleItemView
 *
 * User: XXoneside(www.xxoneside.com)
 * Date: 2016-05-10
 * Time: 14:04
 * Author: pingfu
 * FIXME
 */
class FilterTitleItemView extends RelativeLayout {
    private static final String ANI_ROTATION = "rotation";
    private static final int ANI_DURATION = 450;

    @From(R.id.tv_choose_title)
    private TextView tvTitle;

    @From(R.id.iv_choose_icon_red)
    private ImageView ivChosenRed;

    @From(R.id.iv_choose_icon)
    private ImageView ivUnChosen;

    private boolean isChecked;

    public FilterTitleItemView(Context context) {
        super(context);
        inflate(context, R.layout.filter_title_item, this);
        InjectUtils.autoInject(this);
    }

    /**
     * 设置数据
     *
     * @param title
     */
    public void setData(String title) {
        setTag(title);
        tvTitle.setText(title);
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    /**
     * 标题栏选中事件
     *
     * @param isChecked 是否被选中
     */
    public void onItemClick(boolean isChecked) {
        this.isChecked = isChecked;
        if(isChecked) {
            tvTitle.setTextColor(getResources().getColor(R.color.papaya_primary_color));
            rotateDownAnimRun();
        } else {
            tvTitle.setTextColor(getResources().getColor(R.color.text_color_deep_gray));
            rotateUpAnimRun();
        }
    }

    /**
     * 重置title界面
     */
    public void resetTitleItem() {
        tvTitle.setTextColor(getResources().getColor(R.color.text_color_deep_gray));
        if(isChecked) {
            rotateUpAnimRun();
        }
    }

    private void rotateUpAnimRun() {
        ObjectAnimator anim = ObjectAnimator.ofFloat(ivUnChosen, ANI_ROTATION, -180.0F, 0.0F)
                .setDuration(ANI_DURATION);
        anim.start();

        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float cVal = Math.abs((Float) animation.getAnimatedValue() / 180);
                ivUnChosen.setAlpha(1 - cVal);
                ivChosenRed.setAlpha(cVal);
            }
        });
    }

    private void rotateDownAnimRun() {
        ObjectAnimator anim = ObjectAnimator.ofFloat(ivChosenRed, ANI_ROTATION, 0.0F, -180.0F)
                .setDuration(ANI_DURATION);
        anim.start();

        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float cVal = Math.abs((Float) animation.getAnimatedValue() / 180);
                ivUnChosen.setAlpha(1 - cVal);
                ivChosenRed.setAlpha(cVal);
            }
        });
    }
}
