package com.kuaipao.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.amap.api.maps2d.AMapUtils;
import com.amap.api.services.core.PoiItem;
import com.kuaipao.model.LocationCoordinate2D;
import com.kuaipao.manager.CardLocationManager;
import com.kuaipao.utils.LangUtils;
import com.kuaipao.manager.R;

public class AddressSearchAdapter extends BaseAdapter {

    private List<PoiItem> mList = null;
    private Context mCon;
    private String searchString;

    public AddressSearchAdapter(Context context, ArrayList<PoiItem> list) {
        if (mList == null) {
            mList = new ArrayList<PoiItem>();
        }
        if (LangUtils.isNotEmpty(list)) {
            mList.addAll(list);
        }
        mCon = context;
    }

    public void setSearchStr(String str) {
        searchString = str;
        notifyDataSetChanged();
    }

    public void setList(ArrayList<PoiItem> list) {
        if (mList == null) {
            mList = new ArrayList<PoiItem>();
        }
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (LangUtils.isEmpty(mList))
            return 0;
        else
            return mList.size();
    }

    @Override
    public PoiItem getItem(int position) {
        if (LangUtils.isEmpty(mList))
            return null;
        else
            return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mCon, R.layout.ui_address_search_item, null);
            holder.poiAddress = (TextView) convertView.findViewById(R.id.search_address_details);
            holder.poiName = (TextView) convertView.findViewById(R.id.search_address_main);
            holder.distance = (TextView) convertView.findViewById(R.id.distance_to_location);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        String tmpStr = mList.get(position).getTitle();
        if (LangUtils.isNotEmpty(searchString) && LangUtils.isNotEmpty(tmpStr)
                && tmpStr.contains(searchString)) {
            SpannableString spanStr = new SpannableString(tmpStr);
            spanStr.setSpan(
                    new ForegroundColorSpan(mCon.getResources().getColor(R.color.papaya_primary_color)),
                    tmpStr.indexOf(searchString), tmpStr.indexOf(searchString) + searchString.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.poiName.setText(spanStr);
        } else
            holder.poiName.setText(tmpStr);
        holder.poiAddress.setText(mList.get(position).getSnippet());
        if (CardLocationManager.getInstance().getLocation() != null) {
            float betweenDistance =
                    AMapUtils.calculateLineDistance(
                            LocationCoordinate2D.toMapData(CardLocationManager.getInstance().getLocation()),
                            LocationCoordinate2D.toMapData(new LocationCoordinate2D(mList.get(position)
                                    .getLatLonPoint().getLatitude(), mList.get(position).getLatLonPoint()
                                    .getLongitude())));
            if (betweenDistance < 1000) {
                holder.distance.setText(String.format(
                        mCon.getResources().getString(R.string.distance_less_to), 1));
            } else
                holder.distance.setText(String.format(
                        mCon.getResources().getString(R.string.distance_equal_with),
                        (int) betweenDistance / 1000));
        } else
            holder.distance.setText("");
        return convertView;
    }

    private class ViewHolder {
        private TextView poiName;
        private TextView poiAddress;
        private TextView distance;
    }
}
