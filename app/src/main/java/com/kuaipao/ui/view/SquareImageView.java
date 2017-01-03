package com.kuaipao.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class SquareImageView extends ImageView {

    public SquareImageView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub

    }

    public SquareImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub

    }

    int squareDim = 1000000000;

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

//
//    int h = this.getMeasuredHeight();
//    int w = this.getMeasuredWidth();
//    int curSquareDim = Math.min(w, h);
//
//    if (curSquareDim < squareDim) {
//      squareDim = curSquareDim;
//    }
//
////    Log.d("MyApp", "h " + h + "w " + w + "squareDim " + squareDim);
//
//    setMeasuredDimension(squareDim, squareDim);

        int width = getMeasuredWidth();
        setMeasuredDimension(width, width);

    }

}
