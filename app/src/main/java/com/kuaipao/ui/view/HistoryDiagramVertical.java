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
package com.kuaipao.ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.kuaipao.utils.LangUtils;
import com.kuaipao.utils.ViewUtils;
import com.kuaipao.manager.R;


public class HistoryDiagramVertical extends View {

    private int nHistogramValueMax;
    private int[] histogramValues;
    private int[] histogramTagStringIDs;

    // ColorInt
    private int[] fillColors;

    // ColorInt
    private int textColor;

    private Paint fillPaints[];
    private Paint textPaint;

    private float percent;

    private float textHeight;
    private int textHeightBottom;

    public HistoryDiagramVertical(Context context) {
        super(context);
        init(context, null);
    }

    public HistoryDiagramVertical(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public HistoryDiagramVertical(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HistoryDiagramVertical(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(final Context context, AttributeSet attrs) {
        if (context == null || attrs == null) {
            return;
        }


//        TypedArray typedArray = null;
//        try {
//            typedArray = context.obtainStyledAttributes(attrs, R.styleable.HistoryDiagramHorizontal);
//            percent = typedArray.getFloat(R.styleable.HistoryDiagramHorizontal_hdh_percent, 0);
//            fillColor = typedArray.getColor(R.styleable.HistoryDiagramHorizontal_hdh_color, 0);
//            backgroundColor = typedArray.getColor(R.styleable.HistoryDiagramHorizontal_hdh_default_color, 0);
//        } finally {
//            if (typedArray != null) {
//                typedArray.recycle();
//            }
//        }
        fillColors = new int[]{
                R.color.class_type_hickey, R.color.class_type_bike, R.color.class_type_yoga,
                R.color.class_type_swimming, R.color.class_type_dance, R.color.class_type_wushu
        };

        histogramTagStringIDs = new int[]{
                R.string.gym_type_qi_xie, R.string.gym_type_dan_che_1, R.string.gym_type_yu_jia,
                R.string.gym_type_you_yong, R.string.gym_type_wu_dao, R.string.gym_type_wu_shu
        };

        fillPaints = new Paint[fillColors.length];
        for (int i = 0; i < fillColors.length; i++) {
            Paint singleFillPaint = new Paint();
            singleFillPaint.setColor(getResources().getColor(fillColors[i]));
            singleFillPaint.setAntiAlias(true);
            fillPaints[i] = singleFillPaint;
        }

        textColor = getResources().getColor(R.color.text_color_gray);
        textPaint = new Paint();
        textPaint.setColor(textColor);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(ViewUtils.rp(14));
        textPaint.setAntiAlias(true);

        Rect tmp = new Rect();
        textPaint.getTextBounds(getResources().getString(R.string.gym_type_qi_xie), 0, 1, tmp);
        textHeight = tmp.height();

        textHeightBottom = ViewUtils.rp(80);
    }

    public void setData(int nValuesMax, int[] values) {
        this.nHistogramValueMax = nValuesMax;
        this.histogramValues = values;
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


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float drawPercent = percent;

        if (histogramValues == null || histogramValues.length < fillColors.length)
            return;

        final int height = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
        final int width = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();

        int diagramHeight = height - textHeightBottom;

        int totalCount = fillColors.length;
//        RectF[] rectFs = new RectF[totalCount];
        float eachRowWidth = width / (2 * totalCount + 1);

        for (int i = 0; i < totalCount; i++) {
            RectF rectF = new RectF();
            rectF.left = eachRowWidth * (2 * i + 1);
            rectF.right = eachRowWidth * 2 * (i + 1);
            rectF.bottom = diagramHeight;
            rectF.top = drawPercent * (nHistogramValueMax - histogramValues[i]) / nHistogramValueMax * diagramHeight;
//            rectFs[i] = rectF;

            canvas.drawRect(rectF, fillPaints[i]);


            float paddingBottom = ViewUtils.rp(4);
            if (histogramValues[i] > 0)
                canvas.drawText(LangUtils.parseString(histogramValues[i]), rectF.centerX(), rectF.top - paddingBottom, textPaint);

//            final android.graphics.Rect TextBounds = new android.graphics.Rect();
            String text = getResources().getString(histogramTagStringIDs[i]);
//            textPaint.getTextBounds(text, 0, text.length(), TextBounds);
//            canvas.drawText(
//                    text, rectF.centerX(), /* depend on UsePaint to align horizontally */
//                    rectF.bottom + paddingBottom - (TextBounds.bottom + TextBounds.top) / 2.0f, textPaint
//                    );
//            Path textPath = new Path();
//            textPath.moveTo(rectF.centerX(), rectF.bottom + paddingBottom);
//            textPath.lineTo(rectF.centerX(), height);
//            canvas.drawTextOnPath(text, textPath, 0, 0, textPaint);
//            canvas.drawText(text, rectF.centerX(), rectF.bottom + paddingBottom, textPaint);

//            LogUtils.d("2525 textHeight = %s", textHeight);
            paddingBottom = ViewUtils.rp(10);
            for (int j = 0; j < text.length(); j++) {
                canvas.drawText(text.substring(j, j + 1), rectF.centerX(),
                        textHeight + rectF.bottom + paddingBottom + j * (textHeight + ViewUtils.rp(2)), textPaint);
            }
        }
    }
}
