package com.oneside.ui.view;

import static com.oneside.utils.ViewUtils.find;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.oneside.model.CityPriceItem;
import com.oneside.utils.LangUtils;
import com.oneside.utils.ViewUtils;
import com.oneside.R;

public class CityChooseViewItem extends ScrollView {

    private LinearLayout mViews;
    private Context mContext;
    private String mSelectCity;
    private String mSelectCombo;
    private List<View> mViewContents;
    private boolean mStartLineVisible = true;

    public CityChooseViewItem(Context context) {
        super(context);
        mContext = context;
        initViews(context, null);
    }

    public CityChooseViewItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context, attrs);
    }

    private void initViews(Context context, AttributeSet attrs) {
        this.setFillViewport(true);
        this.setPadding(0, ViewUtils.rp(20), 0, ViewUtils.rp(20));
        this.setClipToPadding(false);
        this.setOverScrollMode(OVER_SCROLL_NEVER);
        this.setBackgroundResource(R.color.setting_background_gray);
        mContext = context;
        mViews = new LinearLayout(mContext);
        mViews.setOrientation(LinearLayout.VERTICAL);
        mViewContents = new ArrayList<View>();
        addView(mViews);

    }

    // fake data , need to delete
    public void setData(ArrayList<CityPriceItem> array) {
        if (LangUtils.isNotEmpty(array)) {
            for (int i = 0; i < array.size(); i++) {
                View view = createView(array.get(i));
                if (view == null)
                    continue;
                mViews.addView(view);
            }
        }
    }

    private View createView(CityPriceItem cityPriceItem) {
        String[] title = {cityPriceItem.getTitle(), cityPriceItem.getDesc()};
        List<String> cities = cityPriceItem.getCities();
        return createView(title, cities);
    }

    public void setDefaultCity(String city) {
        updateViewState(city);
    }

    /**
     * @param title[0] is number title[1] is reminder
     * @param cityList
     * @return
     */
    private View createView(String[] title, List<String> cityList) {
        if (LangUtils.isEmpty(cityList))
            return null;

        RelativeLayout itemContent =
                (RelativeLayout) View.inflate(mContext, R.layout.ui_city_choose_view, null);
        LinearLayout cityContent = find(itemContent, R.id.city_choose_layout);
        TextView titleContent = find(itemContent, R.id.city_combo_title);
        TextView verLine = find(itemContent, R.id.city_vertical_line);
        View startLine = find(itemContent, R.id.city_horizontal_start_line);
        if (!mStartLineVisible) {
            startLine.setVisibility(View.GONE);
        }
        titleContent.setText(formatTitleStyle(title));

        for (int p = 0; p < cityList.size(); p++) {
            View view = View.inflate(mContext, R.layout.ui_city_choose_view_item, null);
            TextView cityName = find(view, R.id.city_name);
            cityName.setText(cityList.get(p));
            view.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    updateViewState(v);
                }
            });
            cityContent.addView(view);
            mViewContents.add(view);
        }
        int height = getCityContentheight(cityContent) - ViewUtils.rp(1) * 3 / 4;
        titleContent.setHeight(height);
        verLine.setHeight(height);
        mStartLineVisible = false;
        return itemContent;
    }

    protected void updateViewState(View v) {
        for (View tmp : mViewContents) {
            ImageView iv = find(tmp, R.id.city_chose_state_img);
            iv.setBackgroundResource(R.drawable.ic_gym_normal);
            if (tmp.equals(v)) {
                setSelectCombo(((TextView) find((ViewGroup) tmp.getParent().getParent(),
                        R.id.city_combo_title)).getText().toString());
                setSelectCity(((TextView) find(tmp, R.id.city_name)).getText().toString());
                iv.setBackgroundResource(R.drawable.ic_city_selected);
            }
        }
    }

    protected void updateViewState(String str) {
        if (str != null) {
            for (View tmp : mViewContents) {
                ImageView iv = find(tmp, R.id.city_chose_state_img);
                TextView tv = find(tmp, R.id.city_name);
                iv.setBackgroundResource(R.drawable.ic_gym_normal);
                String text = tv.getText().toString();
                if (str.equals(text)) {
                    // TODO
                    setSelectCombo(((TextView) find((ViewGroup) tmp.getParent().getParent(),
                            R.id.city_combo_title)).getText().toString());
                    setSelectCity(((TextView) find(tmp, R.id.city_name)).getText().toString());
                    iv.setBackgroundResource(R.drawable.ic_city_selected);
                }
            }
            mSelectCity = str;
        }
    }

    private SpannableString formatTitleStyle(String[] title) {
        String tmpStr = title[0] + "\n" + title[1];
        SpannableString spanStr = new SpannableString(tmpStr);
        spanStr.setSpan(
                new ForegroundColorSpan(mContext.getResources().getColor(R.color.text_color_gray)),
                tmpStr.indexOf(title[1]), tmpStr.indexOf(title[1]) + title[1].length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanStr.setSpan(new AbsoluteSizeSpan(ViewUtils.rp(12)), tmpStr.indexOf(title[1]),
                tmpStr.indexOf(title[1]) + title[1].length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return spanStr;
    }

    private int getCityContentheight(View cityContent) {
        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

        int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

        cityContent.measure(width, height);

        int heigh = cityContent.getMeasuredHeight();

        return heigh;
    }


    private void setSelectCity(String city) {
        mSelectCity = city;
    }

    private void setSelectCombo(String num) {
        mSelectCombo = num;
    }

    public String getSelectCity() {
        return mSelectCity;
    }

    public String getSelectCombo() {
        return mSelectCombo;
    }
}
