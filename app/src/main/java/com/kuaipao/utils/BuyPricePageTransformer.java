package com.kuaipao.utils;


import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by MVEN on 16/4/21.
 */
public class BuyPricePageTransformer implements ViewPager.PageTransformer {
    private static final float MIN_SCALE = 0.75f;
    private static final float MIN_ALPHA = 0.5f;

    @Override
    public void transformPage(View page, float position) {
        int height = page.getHeight();
        int width = page.getWidth();
        if (position < -1) {
            page.setAlpha(1);
        } else if (position <= 1) { // (0,1]
            // Fade the page out.  

            // Counteract the default slide transition  
//            page.setTranslationX(width * -position);

            // Scale the page down (between MIN_SCALE and 1)  
            float scaleFactor = MIN_SCALE
                    + (1 - MIN_SCALE) * (1 - Math.abs(position));
            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor);

        } else { // (1,+Infinity]  
            // This page is way off-screen to the right.  
        }

    }
}
