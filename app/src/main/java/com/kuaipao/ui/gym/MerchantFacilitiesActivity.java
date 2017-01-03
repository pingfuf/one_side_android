package com.kuaipao.ui.gym;

import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.kuaipao.base.BaseActivity;
import com.kuaipao.utils.IOUtils;
import com.kuaipao.utils.LangUtils;
import com.kuaipao.utils.ViewUtils;
import com.kuaipao.manager.R;

public class MerchantFacilitiesActivity extends BaseActivity {

    private ListView lvFacilities;


    public static final String EXTRA_SELECTED_TYPES = "selected_types";

    public static final int[] FACILITY_NAME_RES_ID = new int[]{R.string.merchant_facilities_park,
            R.string.merchant_facilities_wifi, R.string.merchant_facilities_cabinet,
            R.string.merchant_facilities_no_cabinet, R.string.merchant_facilities_shower,
      /*R.string.merchant_facilities_dress, */R.string.merchant_facilities_child};

    public static final int[] FACILITY_LOGO_RES_ID = new int[]{R.drawable.park_icon,
            R.drawable.wifi_icon, R.drawable.lock_icon, R.drawable.unlock_icon,
            R.drawable.shower_icon, /*R.drawable.shirt_icon, */R.drawable.no_kit_icon, R.drawable.moren_icon};

//  public static final int[] FACILITY_TIP_RES_ID = new int[] {R.string.merchant_facilities_park_tip,
//      R.string.merchant_facilities_wifi_tip, R.string.merchant_facilities_cabinet_tip,
//      R.string.merchant_facilities_no_cabinet_tip, R.string.merchant_facilities_shower_tip,};

    public static int getLogoResID(String name) {
        int index = getIndex(name);
        if (index == -1) {
            return FACILITY_LOGO_RES_ID[FACILITY_LOGO_RES_ID.length - 1];
        }
        return FACILITY_LOGO_RES_ID[index];
    }

    public static int getIndex(String name) {
        int index = -1;
        for (int i = 0; i < FACILITY_NAME_RES_ID.length; i++) {
            if (name.equals(ViewUtils.getString(FACILITY_NAME_RES_ID[i]))) {
                index = i;
                break;
            }
        }
        return index;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_facilities);
        setTitle(getString(R.string.merchant_facilities_title), true);
        initView();
    }

    private void initView() {
        lvFacilities = (ListView) findViewById(R.id.lv_merchant_facilities);
    }
}
