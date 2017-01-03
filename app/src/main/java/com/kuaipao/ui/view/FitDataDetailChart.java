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


public class FitDataDetailChart extends View {

    private int nChartValueMax = -1;
    private float[][] chartData;
//    private int[] histogramTagStringIDs;

    // ColorInt
    private int[] fillColors;

    // ColorInt
    private int textColor;

    private Paint fillPaints[];
    private Paint textPaint;
    private Paint textEmptyPaint;

    private float percent;

    private int lineColor;
    private Paint linePaint;

    private float textHeight;
    private int textHeightBottom;

    private int[] weekTagsResID;

    public FitDataDetailChart(Context context) {
        super(context);
        init(context, null);
    }

    public FitDataDetailChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FitDataDetailChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FitDataDetailChart(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(final Context context, AttributeSet attrs) {

//        LogUtils.d("2525 Chart init context=%s", context);
//        if (context == null || attrs == null) {
//            return;
//        }

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
                R.color.class_type_hickey, R.color.class_type_swimming, R.color.class_type_yoga,
                R.color.class_type_dance, R.color.class_type_bike, R.color.class_type_wushu
        };//0-'全部', 1-'器械健身', 2-游泳, 3-'瑜伽', 4-'舞蹈', 5-'单车', 6-'武术'

        weekTagsResID = new int[]{
                R.string.week_tag_1, R.string.week_tag_2, R.string.week_tag_3,
                R.string.week_tag_4, R.string.week_tag_5, R.string.week_tag_6, R.string.week_tag_7
        };

        fillPaints = new Paint[fillColors.length];
        for (int i = 0; i < fillColors.length; i++) {
            Paint singleFillPaint = new Paint();
            singleFillPaint.setColor(getResources().getColor(fillColors[i]));
            singleFillPaint.setAntiAlias(true);
            fillPaints[i] = singleFillPaint;
        }

        textColor = getResources().getColor(R.color.text_color_gray);

        if (textPaint == null) {
            textPaint = new Paint();
            textPaint.setColor(textColor);
            textPaint.setTextAlign(Paint.Align.CENTER);
            textPaint.setTextSize(ViewUtils.rp(12));
            textPaint.setAntiAlias(true);
        }

        if (textEmptyPaint == null) {
            textEmptyPaint = new Paint();
            textEmptyPaint.setColor(getResources().getColor(R.color.hint_gray));
            textEmptyPaint.setTextAlign(Paint.Align.CENTER);
            textEmptyPaint.setTextSize(ViewUtils.rp(12));
            textEmptyPaint.setAntiAlias(true);
        }


        lineColor = getResources().getColor(R.color.line_gray);
        if (linePaint == null) {
            linePaint = new Paint();
            linePaint.setColor(lineColor);
            linePaint.setAntiAlias(true);
        }


        Rect tmp = new Rect();
        textPaint.getTextBounds("8", 0, 1, tmp);
        textHeight = tmp.height();

        textHeightBottom = ViewUtils.rp(30);
    }

    public void setData(int nValuesMax, float[][] values) {
        this.nChartValueMax = nValuesMax;
        this.chartData = values;
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


        final float height = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
        final float width = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();

        if (chartData == null)
            return;

        if (canvas != null && textEmptyPaint != null && nChartValueMax == 0) {
            canvas.drawText("暂无数据", width / 2, height / 2, textEmptyPaint);
        }

        float drawPercent = percent;

        float diagramHeight = height - textHeightBottom;

        int columnCount = chartData.length;


        float eachColumnPaddingScale = 0.3f;

        if (columnCount == 7) {
            eachColumnPaddingScale = 1.0f;
        }

        float eachColumnWidth = width / ((eachColumnPaddingScale + 1) * columnCount + eachColumnPaddingScale);
        float eachColumnPadding = eachColumnPaddingScale * eachColumnWidth;

        for (int i = 0; i < columnCount; i++) {

            float sum = 0;
            for (int j = 0; j < chartData[i].length; j++) {
                sum += chartData[i][j];
            }

//            RectF[] rectFs = new RectF[chartData[i].length];
            float columnHeight = drawPercent * sum / nChartValueMax * diagramHeight;

            float lastRectTop = diagramHeight;
            for (int j = 0; j < chartData[i].length; j++) {
                RectF rectF = new RectF();
                rectF.left = eachColumnPadding + (eachColumnWidth + eachColumnPadding) * i;
                rectF.right = rectF.left + eachColumnWidth;
                rectF.bottom = lastRectTop;
                rectF.top = rectF.bottom - columnHeight * chartData[i][j] / sum;
                lastRectTop = rectF.top;
//                rectFs[i] = rectF;
                canvas.drawRect(rectF, fillPaints[j]);
            }


//            if(histogramValues[i] > 0)
//                canvas.drawText(LangUtils.parseString(histogramValues[i]), rectF.centerX(), rectF.top - paddingBottom, textPaint);

//            final android.graphics.Rect TextBounds = new android.graphics.Rect();
//            String text = getResources().getString(histogramTagStringIDs[i]);
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
//            paddingBottom = ViewUtils.rp(10);
//            for(int j = 0; j < text.length(); j++){
//                canvas.drawText(text.substring(j, j + 1), rectF.centerX(),
//                        textHeight + rectF.bottom + paddingBottom + j * (textHeight + ViewUtils.rp(2)), textPaint);
//            }
        }

        float paddingBottom = ViewUtils.rp(4);
        if (columnCount == 7) {
            for (int j = 0; j < columnCount; j++) {
                RectF rectF = new RectF();
                rectF.left = eachColumnPadding + (eachColumnWidth + eachColumnPadding) * j;
                rectF.right = rectF.left + eachColumnWidth;
                rectF.top = diagramHeight + paddingBottom;
                rectF.bottom = rectF.top + textHeight;
                canvas.drawText(getResources().getString(weekTagsResID[j]), rectF.centerX(), rectF.bottom, textPaint);
            }
        } else {
            for (int j = 0; j < columnCount; j++) {
                if (j == 0 || j == columnCount - 1 || j == 14) {
                    RectF rectF = new RectF();
                    rectF.left = eachColumnPadding + (eachColumnWidth + eachColumnPadding) * j;
                    rectF.right = rectF.left + eachColumnWidth;
                    rectF.top = diagramHeight + paddingBottom;
                    rectF.bottom = rectF.top + textHeight;
                    canvas.drawText(LangUtils.parseString(j + 1), rectF.centerX(), rectF.bottom, textPaint);
                }
            }
        }
        canvas.drawLine(eachColumnPadding, diagramHeight, width - eachColumnPadding, diagramHeight, linePaint);
    }
}
