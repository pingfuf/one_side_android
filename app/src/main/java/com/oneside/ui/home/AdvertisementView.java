package com.oneside.ui.home;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.oneside.base.adapter.XViewPagerAdapter;
import com.oneside.base.view.XImageView;
import com.oneside.R;
import com.oneside.model.beans.Banner;
import com.oneside.utils.LangUtils;
import com.oneside.utils.LogUtils;
import com.oneside.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 广告栏
 * <p/>
 * Created by pingfu on 16-9-1.
 */
public class AdvertisementView extends RelativeLayout {
    private ViewPager mViewPager;
    private LinearLayout llIndication;
    private XViewPagerAdapter mAdapter;
    private List<View> mViews;
    private int mSelection;
    private IAdvertisementChosenHandler mAdvertisementChosenHandler;
    private boolean isMoving;
    private long loopTime = 5 * 1000;

    public AdvertisementView(Context context) {
        super(context);
        init();
    }

    public AdvertisementView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.ui_advertisement, this);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        llIndication = (LinearLayout) findViewById(R.id.ll_indication);

        mViews = new ArrayList<>();
        mAdapter = new XViewPagerAdapter(mViews);
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeIndication(mSelection, R.color.text_color_gray_66);
                mSelection = position;
                changeIndication(mSelection, R.color.white);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void changeIndication(int mSelection, int colorId) {
        int i = 0;
        if(mAdapter.getSize() != 0) {
            i = mSelection % mAdapter.getSize();
        }
        if (i < llIndication.getChildCount() && i >= 0) {
            llIndication.getChildAt(i).setBackgroundColor(getResources().getColor(colorId));
        }
    }

    public void setData(List<Banner> imageUrls) {
        if (LangUtils.isEmpty(imageUrls)) {
            return;
        }

        mViews.clear();
        for (int i = 0; i < imageUrls.size(); i++) {
            final Banner banner = imageUrls.get(i);
            final ImageView imageView = createImageView(banner);
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mAdvertisementChosenHandler != null) {
                        mAdvertisementChosenHandler.onAdvertiseItemChosen(banner);
                    }
                }
            });

            mViews.add(imageView);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewUtils.rp(16), ViewUtils.rp(3));
            if (i > 0) {
                params.setMargins(ViewUtils.rp(5), 0, 0, 0);
            }
            llIndication.addView(createIndicationView(), params);
        }

        mAdapter.notifyDataSetChanged();
        changeIndication(0, R.color.white);
        startLoop(true);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(!isMoving) {
                mHandler.sendEmptyMessage(0);
            }
        }
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0) {
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                mHandler.postDelayed(runnable, loopTime);
            }
        }
    };

    public void startLoop(boolean isReset) {
        if(mAdapter == null || mAdapter.getSize() < 2) {
            return;
        }

        if(isReset) {
            mViewPager.setCurrentItem(10000 * mViews.size());
        }
        mHandler.postDelayed(runnable, loopTime);
    }

    public void setAdvertisementChosenHandler(IAdvertisementChosenHandler advertisementChosenHandler) {
        mAdvertisementChosenHandler = advertisementChosenHandler;
    }

    private ImageView createImageView(Banner banner) {
        String url = null;
        XImageView imageView = new XImageView(getContext());
        ViewPager.LayoutParams params = new ViewPager.LayoutParams();
        imageView.setLayoutParams(params);
        if (banner != null) {
            url = banner.imgUrl;
        }
        imageView.setImageUri(url);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        return imageView;
    }

    private View createIndicationView() {
        View view = new View(getContext());
        view.setBackgroundColor(getResources().getColor(R.color.black));

        return view;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                isMoving = true;
                mHandler.removeCallbacks(runnable);
                break;
            default:
                isMoving = false;
                startLoop(false);
                break;
        }
        return super.onTouchEvent(event);
    }

    public void clear() {
        mHandler.removeCallbacks(runnable);
        runnable = null;
        mHandler = null;
    }

    /**
     * 点击广告某一条的点击事件
     */
    public interface IAdvertisementChosenHandler {
        void onAdvertiseItemChosen(Banner banner);
    }
}
