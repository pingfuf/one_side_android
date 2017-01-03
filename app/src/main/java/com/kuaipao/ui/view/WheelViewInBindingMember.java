package com.kuaipao.ui.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kuaipao.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

public class WheelViewInBindingMember extends ScrollView {

    private Context context;
    private LinearLayout views;
    private int itemHeight = ViewUtils.rp(40);
    private int textSizeNormal;//sp
    private int textSizeSelected;//sp


    // String[] items;
    private List<String> items;
    private static final int OFF_SET_DEFAULT = 1;
    int offset = OFF_SET_DEFAULT; // 偏移量（需要在最前面和最后面补全）
    int displayItemCount; // 每页显示的数量
    int selectedIndex = 1;

    private int scrollDirection = -1;
    private static final int SCROLL_DIRECTION_UP = 0;
    private static final int SCROLL_DIRECTION_DOWN = 1;

    Paint paint;
    int viewWidth;
    int initialY;

    Runnable scrollerTask;
    int newCheck = 50;
    /**
     * 获取选中区域的边界
     */
    int[] selectedAreaBorder;

    public WheelViewInBindingMember(Context context) {
        super(context);
        init(context);
    }

    public WheelViewInBindingMember(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WheelViewInBindingMember(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private List<String> getItems() {
        return items;
    }

    public void setItems(List<String> list) {
        if (null == items) {
            items = new ArrayList<String>();
        }
        items.clear();
        items.addAll(list);

        // 前面和后面补全
        for (int i = 0; i < offset; i++) {
            items.add(0, "");
            items.add("");
        }

        initData();

    }


    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }


    private void init(Context context) {
        this.context = context;
        textSizeNormal = 16;
        textSizeSelected = 16;

        this.setVerticalScrollBarEnabled(false);
        this.setOverScrollMode(OVER_SCROLL_NEVER);
        views = new LinearLayout(context);
        views.setOrientation(LinearLayout.VERTICAL);
        this.addView(views);

        scrollerTask = new Runnable() {

            public void run() {

                int newY = getScrollY();
                if (initialY - newY == 0) {
                    final int remainder = initialY % itemHeight;
                    final int divided = initialY / itemHeight;
                    if (remainder == 0) {
                        selectedIndex = divided + offset;

                        onSeletedCallBack();
                    } else {
                        if (remainder > itemHeight / 2) {
                            WheelViewInBindingMember.this.post(new Runnable() {
                                @Override
                                public void run() {
                                    WheelViewInBindingMember.this.smoothScrollTo(0, initialY - remainder + itemHeight);
                                    selectedIndex = divided + offset + 1;
                                    onSeletedCallBack();
                                }
                            });
                        } else {
                            WheelViewInBindingMember.this.post(new Runnable() {
                                @Override
                                public void run() {
                                    WheelViewInBindingMember.this.smoothScrollTo(0, initialY - remainder);
                                    selectedIndex = divided + offset;
                                    onSeletedCallBack();
                                }
                            });
                        }

                    }

                } else {
                    initialY = getScrollY();
                    WheelViewInBindingMember.this.postDelayed(scrollerTask, newCheck);
                }
            }
        };
    }

    public void startScrollerTask() {

        initialY = getScrollY();
        this.postDelayed(scrollerTask, newCheck);
    }

    private void initData() {
        displayItemCount = offset * 2 + 1;

        for (String item : items) {
            views.addView(createView(item));
        }

        refreshItemView(0);
    }

    private TextView createView(String item) {
        TextView tv = new TextView(context);
        tv.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemHeight));
        tv.setSingleLine(true);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSizeNormal);
        tv.setText(item);
        tv.setGravity(Gravity.CENTER);
        views.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemHeight
                * displayItemCount));
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) this.getLayoutParams();
        this.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, itemHeight * displayItemCount));
        return tv;
    }


    private int getViewMeasuredHeight(final TextView tv) {
        int width = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);

        int height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);

        tv.measure(width, height);

        int heigh = tv.getMeasuredHeight();

        return heigh;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);


        refreshItemView(t);

        if (t > oldt) {
            scrollDirection = SCROLL_DIRECTION_DOWN;
        } else {
            scrollDirection = SCROLL_DIRECTION_UP;

        }

    }

    private void refreshItemView(int y) {
        int position = y / itemHeight + offset;
        int remainder = y % itemHeight;
        int divided = y / itemHeight;

        int childSize = views.getChildCount();
        for (int i = 0; i < childSize; i++) {
            TextView itemView = (TextView) views.getChildAt(i);
            if (null == itemView) {
                return;
            }
            if (position == i) {
                itemView.setTextColor(Color.parseColor("#ff333333"));
                itemView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSizeSelected);
            } else {
                itemView.setTextColor(Color.parseColor("#ffbbbbbb"));
                itemView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSizeNormal);
            }
        }
    }


    private int[] obtainSelectedAreaBorder() {
        if (null == selectedAreaBorder) {
            selectedAreaBorder = new int[2];
            selectedAreaBorder[0] = itemHeight * offset;
            selectedAreaBorder[1] = itemHeight * (offset + 1);
        }
        return selectedAreaBorder;
    }


    @Override
    public void setBackgroundDrawable(Drawable background) {

        if (viewWidth == 0) {
            viewWidth = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();
        }

        if (null == paint) {
            paint = new Paint();
            paint.setColor(Color.parseColor("#ff333333"));
            paint.setStrokeWidth(ViewUtils.rp(1));
        }

        background = new Drawable() {
            @Override
            public void draw(Canvas canvas) {
                Paint p = new Paint();
                LinearGradient lg =
                        new LinearGradient(viewWidth * 1 / 6, obtainSelectedAreaBorder()[0] + 4,
                                viewWidth * 5 / 6, obtainSelectedAreaBorder()[0] + 4, new int[]{
                                Color.parseColor("#fffbfbfc"), Color.parseColor("#ffc8c9c8"),
                                Color.parseColor("#fffbfbfc")}, new float[]{0, 0.5f, 1.0f},
                                Shader.TileMode.MIRROR);
                p.setShader(lg);
                // TODO
                canvas.drawRect(viewWidth * 1 / 6, obtainSelectedAreaBorder()[0] - 4, viewWidth * 5 / 6,
                        obtainSelectedAreaBorder()[0], p);

                canvas.drawRect(viewWidth * 1 / 6, obtainSelectedAreaBorder()[1] - 4, viewWidth * 5 / 6,
                        obtainSelectedAreaBorder()[1], p);
            }

            @Override
            public void setAlpha(int alpha) {

            }

            @Override
            public void setColorFilter(ColorFilter cf) {

            }

            @Override
            public int getOpacity() {
                return 0;
            }
        };

        super.setBackgroundDrawable(background);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        setBackgroundDrawable(null);
    }

    /**
     * 选中回调
     */
    private void onSeletedCallBack() {
        if (null != onWheelViewListener) {
            onWheelViewListener.onSelected(selectedIndex, items.get(selectedIndex));
        }

    }

    public void setSeletion(int position) {
        final int p = position;
        selectedIndex = p + offset;
        this.post(new Runnable() {
            @Override
            public void run() {
                WheelViewInBindingMember.this.smoothScrollTo(0, p * itemHeight);
            }
        });

    }

    public String getSeletedItem() {
        return items.get(selectedIndex);
    }

    public int getSeletedIndex() {
        return selectedIndex - offset;
    }

    @Override
    public void fling(int velocityY) {
        super.fling(velocityY / 3);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {

            startScrollerTask();
        }
        return super.onTouchEvent(ev);
    }

    private OnWheelViewListener onWheelViewListener;

    public OnWheelViewListener getOnWheelViewListener() {
        return onWheelViewListener;
    }

    public void setOnWheelViewListener(OnWheelViewListener onWheelViewListener) {
        this.onWheelViewListener = onWheelViewListener;
    }

    public interface OnWheelViewListener {
        void onSelected(int selectedIndex, String item);
    }

}
