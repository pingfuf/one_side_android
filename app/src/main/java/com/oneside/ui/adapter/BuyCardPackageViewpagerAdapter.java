package com.oneside.ui.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oneside.model.BuyCardPackage;
import com.oneside.utils.LangUtils;
import com.oneside.R;

import java.util.ArrayList;
import java.util.List;

import static com.oneside.utils.ViewUtils.find;

/**
 * Created by MVEN on 16/4/22.
 */
public class BuyCardPackageViewpagerAdapter extends PagerAdapter {

    private Context context;
    private List<View> views;
    private List<BuyCardPackage> packages;

    public BuyCardPackageViewpagerAdapter(Context context, List<BuyCardPackage> packages) {
        this.context = context;
        if (packages == null)
            packages = new ArrayList<>();
        this.packages = new ArrayList<>();
        if (views == null)
            views = new ArrayList<>();
        if (packages != null) {
            this.packages.addAll(packages);
        }

    }

    public void setLists(List<BuyCardPackage> packages) {
        this.packages.clear();
        if (this.packages == null)
            this.packages = new ArrayList<>();
        this.packages.addAll(packages);
        notifyDataSetChanged();
    }

    private void initViews() {
        views.clear();
        if (LangUtils.isEmpty(packages))
            return;
        int len = packages.size();
        for (int i = 0; i < len; i++) {
            BuyCardPackage buyCardPackage = packages.get(i);
            View view = View.inflate(context, R.layout.buy_card_package_viewpager_item, null);

            RelativeLayout titleLayout = find(view, R.id.buy_card_package_viewpager_title);

            View lineRight = find(view, R.id.horizontal_line_right);
            View lineMid = find(view, R.id.horizontal_line_mid);
            View lineLeft = find(view, R.id.horizontal_line_left);

            TextView name = find(view, R.id.buy_card_package_name);
            TextView expire = find(view, R.id.buy_card_package_expire_time);
            TextView desc0 = find(view, R.id.buy_card_package_desc0);
            TextView desc1 = find(view, R.id.buy_card_package_desc1);
            TextView price = find(view, R.id.buy_card_package_price_per_month);
            name.setText(buyCardPackage.getTitle());
            expire.setText(context.getString(R.string.buy_card_expire_time_format, buyCardPackage.getNumOfMonth() * 31));
            price.setText(context.getString(R.string.buy_card_price_formatter, buyCardPackage.getPrice() / 100));

            switch (buyCardPackage.getNumOfMonth()) {
                case 1:
                    titleLayout.setBackgroundResource(R.drawable.corners_13dp_solid_buy_package_month_title);
                    lineRight.setBackgroundResource(R.drawable.corners_buy_package_2dp_month_line_deep);
                    lineMid.setBackgroundResource(R.drawable.corners_buy_package_2dp_month_line_deep);
                    lineLeft.setBackgroundResource(R.drawable.corners_buy_package_2dp_month_line_deep);
                    break;
                case 3:
                    titleLayout.setBackgroundResource(R.drawable.corners_13dp_solid_buy_package_season_title);
                    lineRight.setBackgroundResource(R.drawable.corners_buy_package_2dp_season_line_deep);
                    lineMid.setBackgroundResource(R.drawable.corners_buy_package_2dp_season_line_deep);
                    lineLeft.setBackgroundResource(R.drawable.corners_buy_package_2dp_season_line_deep);
                    break;
                case 6:
                    titleLayout.setBackgroundResource(R.drawable.corners_13dp_solid_buy_package_half_year_title);
                    lineRight.setBackgroundResource(R.drawable.corners_buy_package_2dp_half_year_line_deep);
                    lineMid.setBackgroundResource(R.drawable.corners_buy_package_2dp_half_year_line_deep);
                    lineLeft.setBackgroundResource(R.drawable.corners_buy_package_2dp_half_year_line_deep);
                    break;
                default:
                    break;
            }
            views.add(view);

            ArrayList<String> descArray = buyCardPackage.getDesc();
            if (LangUtils.isEmpty(descArray))
                continue;
            if (LangUtils.isNotEmpty(descArray.get(0))) {
                desc0.setVisibility(View.VISIBLE);
                desc0.setText(descArray.get(0));
            } else {
                desc0.setVisibility(View.GONE);
            }
            if (descArray.size() == 1) {
                desc1.setVisibility(View.GONE);
                continue;
            }
            if (LangUtils.isNotEmpty(descArray.get(1))) {
                desc1.setVisibility(View.VISIBLE);
                desc1.setText(descArray.get(1));
            } else {
                desc1.setVisibility(View.GONE);
            }

        }
    }

    @Override
    public int getCount() {
        if (packages == null)
            return 0;
        else
            return packages.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        initViews();

        if (LangUtils.isEmpty(views))
            return null;
        if (position < views.size()) {
            container.addView(views.get(position));
            return views.get(position);
        } else
            return views.get(0);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (position < views.size())
            container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}
