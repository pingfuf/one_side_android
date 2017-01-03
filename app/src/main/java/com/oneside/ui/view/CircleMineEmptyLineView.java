package com.oneside.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.oneside.utils.ViewUtils;
import com.oneside.R;

/**
 * Created by ZhanTao on 11/12/15.
 */
public class CircleMineEmptyLineView extends View {

    public CircleMineEmptyLineView(Context context) {
        super(context);
    }

    public CircleMineEmptyLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleMineEmptyLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint();
        paint.setColor(getContext().getResources().getColor(R.color.dark_shadow));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(ViewUtils.rp(1));

        PathEffect pe = new DashPathEffect(new float[]{5, 5, 5, 5}, 1);
        paint.setPathEffect(pe);


        int startX = getWidth() / 2;
        int startY = 0;

        int cornerRadius = ViewUtils.rp(8);
        int endX = getWidth() / 2;
        int endY = getHeight() / 2 - cornerRadius;

        Path pathLine = new Path();
        pathLine.moveTo(startX, startY);
        pathLine.lineTo(endX, endY);
//        canvas.drawPath(pathLine, paint);

        RectF rectF = new RectF(endX - 2 * cornerRadius, endY - cornerRadius, endX, endY + cornerRadius);
        pathLine.arcTo(rectF, 0, 90);
//        canvas.drawArc(rectF, 0, 90, true, paint);
//        canvas.drawPath(pathLine, paint);

        startX = endX - cornerRadius;
        startY = endY + cornerRadius;
        endX = getWidth() / 8 + cornerRadius;
        endY = startY;

        pathLine.moveTo(startX, startY);
        pathLine.lineTo(endX, endY);
//        canvas.drawPath(pathLine, paint);

        rectF = new RectF(endX - cornerRadius, endY, endX + cornerRadius, endY + 2 * cornerRadius);
//        canvas.drawArc(rectF, 0, -45, true, paint);
//        pathLine.moveTo(endX - cornerRadius, endY + cornerRadius);
        pathLine.arcTo(rectF, 270, -90);
//        canvas.drawPath(pathLine, paint);

        startX = endX - cornerRadius;
        startY = endY + cornerRadius;
        endX = startX;
        endY = getHeight();

        pathLine.moveTo(startX, startY);
        pathLine.lineTo(endX, endY);
        canvas.drawPath(pathLine, paint);

        int arrow = ViewUtils.rp(6);
        startX = endX;
        startY = endY;
        endX = startX - arrow;
        endY = startY - arrow;

        paint.setPathEffect(null);
        canvas.drawLine(startX, startY, endX, endY, paint);
//        pathLine = new Path();
//        pathLine.moveTo();
//        pathLine.lineTo(endX, endY);
//        canvas.drawPath(pathLine, paint);

        endX = startX + arrow;
        endY = startY - arrow;
        canvas.drawLine(startX, startY, endX, endY, paint);
//        pathLine.moveTo(startX, startY);
//        pathLine.lineTo(endX, endY);
//        canvas.drawPath(pathLine, paint);
    }
}
