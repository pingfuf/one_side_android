package com.kuaipao.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.kuaipao.manager.CardManager;

/**
 * Created by magi on 16/1/7.
 */
public class OvalView extends View {
    private Paint paint;
    private Context context = CardManager.getApplicationContext();
    private int colorP;

    public OvalView(Context context) {
        this(context, null);
    }

    public OvalView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);

    }

    public void setColor(int color) {
        colorP = color;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int center = getWidth() / 2;
//        Bitmap output = Bitmap.createBitmap(getWidth(), getWidth(), Bitmap.Config.ARGB_8888);
//        canvas = new Canvas(output);
//        canvas.drawARGB(0, 0, 0, 0);
//        final Rect rect = new Rect(0, 0, getWidth(), getWidth());
//        paint.setColor(Color.WHITE);
//        canvas.drawRect(rect, paint);
        paint.setColor(colorP);
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));
        canvas.drawCircle(center, center, center, paint);
        super.onDraw(canvas);
    }
}
