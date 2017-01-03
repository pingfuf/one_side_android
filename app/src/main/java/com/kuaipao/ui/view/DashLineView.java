package com.kuaipao.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.View;

import com.kuaipao.utils.ViewUtils;
import com.kuaipao.manager.R;

/**
 * Created by ZhanTao on 11/12/15.
 */
public class DashLineView extends View {

    public DashLineView(Context context) {
        super(context);
    }

    public DashLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DashLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint();
        paint.setColor(getContext().getResources().getColor(R.color.dark_shadow));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(ViewUtils.rp(1));

        PathEffect pe = new DashPathEffect(new float[]{7, 7, 7, 7}, 1);
        paint.setPathEffect(pe);


        int startX = 0;
        int startY = getHeight() / 2;

        int endX = getWidth();
        int endY = getHeight() / 2;

        Path pathLine = new Path();
        pathLine.moveTo(startX, startY);
        pathLine.lineTo(endX, endY);
        canvas.drawPath(pathLine, paint);

        //canvas.drawLine(startX, startY, endX, endY, paint);
    }
}
