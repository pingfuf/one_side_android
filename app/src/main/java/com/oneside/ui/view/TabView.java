package com.oneside.ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.oneside.R;

import java.util.ArrayList;
import java.util.List;

public class TabView extends LinearLayout {
    private int mChildSize;
    private List<TabItemView> mTabItemViews;
    private OnItemIconTextSelectListener mListener;
    private OnItemClick itemCLickListener;

    private int mTextSize = 12;
    private int mTextColorSelect/* = 0xff45c01a*/;
    private int mTextColorNormal/* = 0xff777777*/;
    private int mPadding = 10;
    private int imageMargin = 3;
    private int mSelectedPosition = 0;

    public TabView(Context context) {
        this(context, null);
    }

    public TabView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    // api >= 11
    public TabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setBackgroundColor(context.getResources().getColor(R.color.layout_background));
        mTextColorSelect = getResources().getColor(R.color.orange);
        mTextColorNormal = getResources().getColor(R.color.text_color_deep_gray);
        // TypedArray typedArray = getResources().obtainAttributes(attrs, R.styleable.TabView);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TabView);
        int N = typedArray.getIndexCount();
        for (int i = 0; i < N; i++) {
            switch (typedArray.getIndex(i)) {
                case R.styleable.TabView_text_size:
                    mTextSize = (int) typedArray.getDimension(i,
                            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                                    mTextSize, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.TabView_text_normal_color:
                    mTextColorNormal = typedArray.getColor(i, mTextColorNormal);
                    break;
                case R.styleable.TabView_text_select_color:
                    mTextColorSelect = typedArray.getColor(i, mTextColorSelect);
                    break;
                case R.styleable.TabView_item_padding:
                    mPadding = (int) typedArray.getDimension(i,
                            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                                    mPadding, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.TabView_image_text_margin:
                    imageMargin = (int) typedArray.getDimension(i,
                            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                                    imageMargin, getResources().getDisplayMetrics()));
                    break;
            }
        }

        setGravity(Gravity.CENTER_VERTICAL);
        typedArray.recycle();
        mTabItemViews = new ArrayList<TabItemView>();
    }

    public void addItemView(int resId, int res2Id, String text) {
        final TabItemView tabItemView = new TabItemView(getContext());
        int i = getChildCount();
        LayoutParams params = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        tabItemView.setPadding(mPadding, mPadding, mPadding, mPadding);
        tabItemView.setData(resId, res2Id, text);
        tabItemView.setTag(i);
        tabItemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) tabItemView.getTag();
                if(position != mSelectedPosition) {
                    mTabItemViews.get(mSelectedPosition).setSelectedState(false);
                    mSelectedPosition = position;
                    mTabItemViews.get(mSelectedPosition).setSelectedState(true);

                    if(itemCLickListener != null) {
                        itemCLickListener.onItemClick(position);
                    }
                }
            }
        });
        mTabItemViews.add(tabItemView);

        addView(tabItemView, params);
    }

    public void setSelectPosition(int position) {
        mSelectedPosition = position;
        mTabItemViews.get(mSelectedPosition).setSelectedState(true);
    }

    public void setOnItemClickListener(OnItemClick onItemClickListener) {
        this.itemCLickListener = onItemClickListener;
    }

    public interface OnItemClick {
        void onItemClick(int position);
    }

    public interface OnItemIconTextSelectListener {
        int[] onIconSelect(int position);

        String onTextSelect(int position);
    }
}
