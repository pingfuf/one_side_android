/**
 * Copyright (c) 2015 LingoChamp Inc.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.oneside.ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.oneside.R;


public class HistoryDiagramHorizontal extends View {

    // ColorInt
    private int fillColor;

    // ColorInt
    private int backgroundColor;

    private Paint fillPaint;
    private Paint backgroundPaint;

    private float percent;

    public HistoryDiagramHorizontal(Context context) {
        super(context);
        init(context, null);
    }

    public HistoryDiagramHorizontal(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public HistoryDiagramHorizontal(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HistoryDiagramHorizontal(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(final Context context, AttributeSet attrs) {
        if (context == null || attrs == null) {
            return;
        }


        TypedArray typedArray = null;
        try {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.HistoryDiagramHorizontal);
            percent = typedArray.getFloat(R.styleable.HistoryDiagramHorizontal_hdh_percent, 0);
            fillColor = typedArray.getColor(R.styleable.HistoryDiagramHorizontal_hdh_color, 0);
            backgroundColor = typedArray.getColor(R.styleable.HistoryDiagramHorizontal_hdh_default_color, 0);
        } finally {
            if (typedArray != null) {
                typedArray.recycle();
            }
        }

        fillPaint = new Paint();
        fillPaint.setColor(fillColor);
        fillPaint.setAntiAlias(true);

        backgroundPaint = new Paint();
        backgroundPaint.setColor(backgroundColor);
        backgroundPaint.setAntiAlias(true);

    }

    public float getPercent() {
        return this.percent;
    }

    /**
     *
     * @param percent FloatRange(from = 0.0, to = 1.0)
     */
    public void setPercent(final float percent) {
        this.percent = percent;
        invalidate();
    }

    private final RectF rectF = new RectF();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float drawPercent = percent;

        canvas.save();
        final int height = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
        final int width = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();

        final int fillWidth = (int) (drawPercent * width);
        final float radius = height / 2.0f;


        rectF.left = 0;
        rectF.top = 0;
        rectF.right = width;
        rectF.bottom = height;

        // draw background
        if (backgroundColor != 0) {
            canvas.drawRoundRect(rectF, radius, radius, backgroundPaint);
        }


        // draw fill
        if (fillColor != 0) {
            rectF.right = fillWidth;
            canvas.drawRoundRect(rectF, radius, radius, fillPaint);
        }

        canvas.restore();
    }
}
