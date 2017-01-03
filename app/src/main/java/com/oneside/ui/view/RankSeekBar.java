package com.oneside.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.oneside.utils.SysUtils;
import com.oneside.utils.ViewUtils;
import com.oneside.R;

/**
 * Created by vashisthg on 01/04/14.
 */
public class RankSeekBar extends View {
    private static final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static final int DEFAULT_RANGE_COLOR = Color.argb(0xff, 0xff, 0x97, 0x6e);
    private static final int DEFAULT_BACKGROUND_COLOR = Color.argb(0xff, 0xee, 0xee, 0xee);//"alpha_20_text_color_gray">#33999999
    private static final int DEFAULT_CIRCLE_CENTER_COLOR = Color.WHITE;
    private static final float DEFAULT_MIN_VALUE = 0f;
    private static final float DEFAULT_MAX_VALUE = +100f;
    private static final float DEFAULT_LINE_HEIGHT = ViewUtils.rp(20);
    private static final float DEFAULT_CIRCLE_DIAMETER = ViewUtils.rp(35);

    private static final int DEFAULT_VIEW_HEIGHT = ViewUtils.rp(50);
    private static final int DEFAULT_VIEW_WIDTH = SysUtils.WIDTH - ViewUtils.rp(30);


    private float absoluteMinValue;
    private float absoluteMaxValue;
    private final int defaultRangeColor;
    private final int defaultBackgroundColor;
    private final int defaultCircleCenterColor;

    private final float lineHeight;
    private final float circleDiameter;
    private final float defaultPadding;

    private float mLeftX, mRightX;

    private float currentProgress;

    public RankSeekBar(Context context) {
        this(context, null);
    }

    public RankSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RankSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // Attribute initialization
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RankSeekBar,
                defStyleAttr, 0);


        this.absoluteMinValue = DEFAULT_MIN_VALUE; //a.getFloat(R.styleable.StartPointSeekBar_minValue, DEFAULT_MIN_VALUE);
        this.absoluteMaxValue = DEFAULT_MAX_VALUE;//a.getFloat(R.styleable.StartPointSeekBar_maxValue, DEFAULT_MAX_VALUE);

        this.defaultBackgroundColor = a.getColor(R.styleable.RankSeekBar_rsb_bkg_color, DEFAULT_BACKGROUND_COLOR);
        this.defaultRangeColor = a.getColor(R.styleable.RankSeekBar_rsb_range_color, DEFAULT_RANGE_COLOR);
        this.defaultCircleCenterColor = DEFAULT_CIRCLE_CENTER_COLOR;

        lineHeight = a.getDimension(R.styleable.RankSeekBar_rsb_line_height, DEFAULT_LINE_HEIGHT);//0.3f * thumbHalfHeight;
        circleDiameter = a.getDimension(R.styleable.RankSeekBar_rsb_circle_diameter, DEFAULT_CIRCLE_DIAMETER);
        defaultPadding = (circleDiameter - lineHeight) / 2;
        a.recycle();

//        setFocusable(true);
//        setFocusableInTouchMode(true);
    }


    @Override
    protected synchronized void onMeasure(int widthMeasureSpec,
                                          int heightMeasureSpec) {
        int width = DEFAULT_VIEW_WIDTH;
        if (MeasureSpec.UNSPECIFIED != MeasureSpec.getMode(widthMeasureSpec)) {
            width = MeasureSpec.getSize(widthMeasureSpec);
        }
        int height = DEFAULT_VIEW_HEIGHT;
        if (MeasureSpec.UNSPECIFIED != MeasureSpec.getMode(heightMeasureSpec)) {
            height = Math.min(height, MeasureSpec.getSize(heightMeasureSpec));
        }
//        LogUtils.d("2525 onMeasure->width:" + width);
//        LogUtils.d("2525 onMeasure->height:" + height);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mLeftX = this.getLeft() + getPaddingLeft() + defaultPadding;
        mRightX = this.getRight() - getPaddingRight() - defaultPadding;
//        LogUtils.d("2525 onSizeChanged->mLeftX:" + mLeftX);
//        LogUtils.d("2525 onSizeChanged->mRightX:" + mRightX);
    }


    /**
     * Sets value of seekbar to the given value
     *
     * @param value The new value to set
     */
    public void setProgress(float value, float maxValue) {
        if (value <= maxValue && maxValue >= 0) {
            this.absoluteMaxValue = maxValue;
        }
        if (value > absoluteMaxValue || value < absoluteMinValue) {
            throw new IllegalArgumentException("Value should be in the middle of max and min value");
        }
        this.currentProgress = value;
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // draw seek bar background line
        final RectF rect = new RectF(mLeftX,
                0.5f * (getHeight() - lineHeight), mRightX,
                0.5f * (getHeight() + lineHeight));
        paint.setColor(defaultBackgroundColor);
        canvas.drawRoundRect(rect, 0.5f * lineHeight, 0.5f * lineHeight, paint);


//        LogUtils.d("2525 onDraw->1 rect.right:" + rect.right + "; rect.left=" + rect.left);
//        LogUtils.d("2525 onDraw->1 currentProgress:" + currentProgress + "; MinValue=" + absoluteMinValue + "; MaxValue=" + absoluteMaxValue);
        // draw seek bar active range line
        float circleCenterX;
        if (currentProgress == absoluteMinValue) {
            circleCenterX = rect.left + 0.5f * lineHeight;
        } else if (currentProgress == absoluteMaxValue) {
            circleCenterX = rect.right - 0.5f * lineHeight;
        } else {
            circleCenterX = rect.left +
                    (currentProgress - absoluteMinValue) / (absoluteMaxValue - absoluteMinValue) * (rect.right - rect.left);
        }

        rect.right = circleCenterX;
//        LogUtils.d("2525 onDraw->2 rect.right:" + rect.right + "; rect.left=" + rect.left);
        paint.setColor(defaultRangeColor);
        canvas.drawRoundRect(rect, 0.5f * lineHeight, 0.5f * lineHeight, paint);

        float circleCenterY = 0.5f * getHeight();
        canvas.drawCircle(circleCenterX, circleCenterY, 0.5f * circleDiameter, paint);

        paint.setColor(defaultCircleCenterColor);
        canvas.drawCircle(circleCenterX, circleCenterY, 0.5f * lineHeight, paint);
    }

}
