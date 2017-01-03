package com.kuaipao.ui.view;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.kuaipao.ui.adapter.ExpandListItemAdapter;
import com.kuaipao.model.BusinessDistrict;
import com.kuaipao.utils.LangUtils;
import com.kuaipao.utils.LogUtils;
import com.kuaipao.manager.R;


public class ExpandBusinessDistrictView extends LinearLayout {

    private ListView lvLeft;
    private ListView lvRight;
    private ArrayList<String> mLeftList = new ArrayList<String>();
    private ArrayList<BusinessDistrict> mRightList = new ArrayList<BusinessDistrict>();
    private ArrayList<String> mRightTitleList = new ArrayList<String>();
    private SparseArray<ArrayList<BusinessDistrict>> mLeftIndexOfRightList =
            new SparseArray<ArrayList<BusinessDistrict>>();
    private ExpandListItemAdapter mAdapterRight;
    private ExpandListItemAdapter mAdapterLeft;
    private OnSelectListener mOnSelectListener;
    private int mLeftChoosePosition = 0;
    private int mLeftClickPosition = 0;
    private int mRightChoosePosition = 0;
    private String mShowTitle;
//  private String[] mBdDistrictOrderArray;

    public ExpandBusinessDistrictView(Context context) {
        super(context);
        init(context);
    }

    public ExpandBusinessDistrictView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void setData(HashMap<String, ArrayList<BusinessDistrict>> mapBDs,
                        ArrayList<BusinessDistrict> hotBDs, ArrayList<String> districtsArray) {
        mLeftList.clear();
        mLeftChoosePosition = mLeftClickPosition = 0;
        mLeftList.add(getResources().getString(R.string.bd_all));
        if (districtsArray.size() > 0) {
            for (String item : districtsArray/*mBdDistrictOrderArray*/) {
                if (mapBDs.keySet().contains(item)) {
                    mLeftList.add(item);
                }
            }
        } else
            mLeftList.addAll(mapBDs.keySet());

        ArrayList<BusinessDistrict> firstBDList = new ArrayList<BusinessDistrict>();
        BusinessDistrict firstBD =
                new BusinessDistrict(getResources().getString(R.string.bd_around), -1);
        firstBDList.add(firstBD);

        if (!mapBDs.isEmpty() && !LangUtils.isEmpty(hotBDs)) {
            LogUtils.d(">>>> hotBDs=%s", hotBDs);
            for (BusinessDistrict hotBD : hotBDs) {
                firstBDList.add(hotBD);
            }
        }

        mLeftIndexOfRightList.clear();
        mLeftIndexOfRightList.put(0, firstBDList);
        for (int i = 1; i < mLeftList.size(); i++) {
            ArrayList<BusinessDistrict> listBD = new ArrayList<BusinessDistrict>();
            BusinessDistrict bd = new BusinessDistrict(getResources().getString(R.string.bd_all_tip), -1);
            listBD.add(bd);
            listBD.addAll(mapBDs.get(mLeftList.get(i)));
            mLeftIndexOfRightList.put(i, listBD);
        }

        mRightChoosePosition = 0;
        mRightList.clear();
        mRightTitleList.clear();
        if (mLeftClickPosition < mLeftIndexOfRightList.size())
            mRightList.addAll(mLeftIndexOfRightList.get(0));
        for (BusinessDistrict bd : mRightList) {
            mRightTitleList.add(bd.getBusinessName());
        }

        mShowTitle = getResources().getString(R.string.business_zone);

        mAdapterLeft.setSelectedPosition(mLeftChoosePosition);
        mAdapterRight.setSelectedPosition(mRightChoosePosition);
    }

    private void init(Context context) {
//    mBdDistrictOrderArray = getResources().getStringArray(R.array.bd_district_order);
        mShowTitle = getResources().getString(R.string.business_zone);
        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.expand_bd_view, this, true);
        lvLeft = (ListView) findViewById(R.id.lv_left);
        lvRight = (ListView) findViewById(R.id.lv_right);
//    setBackgroundResource(R.color.layout_background);
        lvLeft.setBackgroundColor(getResources().getColor(R.color.layout_background));
        lvRight.setBackgroundColor(getResources().getColor(R.color.layout_background));

        mAdapterLeft =
                new ExpandListItemAdapter(context, mLeftList, R.drawable.ic_select_list_bg,
                        R.drawable.expand_left_item_normal_bg, R.color.text_color_gray_black,
                        R.color.text_color_deep_gray);
        mAdapterLeft.setSelectedPositionNoNotify(mLeftChoosePosition);
        lvLeft.setAdapter(mAdapterLeft);
        mAdapterLeft.setOnItemClickListener(new ExpandListItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position < mLeftIndexOfRightList.size()) {
                    mRightList.clear();
                    mRightList.addAll(mLeftIndexOfRightList.get(position));
                    mRightTitleList.clear();
                    for (BusinessDistrict bd : mRightList) {
                        mRightTitleList.add(bd.getBusinessName());
                    }
                    mLeftClickPosition = position;
                    if (mLeftClickPosition != mLeftChoosePosition) {
                        mAdapterRight.setSelectedPositionNoNotify(-1);
                    } else
                        mAdapterRight.setSelectedPositionNoNotify(mRightChoosePosition);
                    mAdapterRight.notifyDataSetChanged();
                }
            }
        });
        if (mLeftClickPosition < mLeftIndexOfRightList.size()) {
            mRightList.addAll(mLeftIndexOfRightList.get(mLeftClickPosition));
            for (BusinessDistrict bd : mRightList) {
                mRightTitleList.add(bd.getBusinessName());
            }
        }
        mAdapterRight =
                new ExpandListItemAdapter(context, mRightTitleList,
                        R.drawable.expand_right_item_selected_bg, R.drawable.expand_right_item_normal_bg,
                        R.color.papaya_primary_color, R.color.text_color_gray_black);
        mAdapterRight.setSelectedPositionNoNotify(mRightChoosePosition);
        lvRight.setAdapter(mAdapterRight);
        mAdapterRight.setOnItemClickListener(new ExpandListItemAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, final int position) {
                mRightChoosePosition = position;
                mLeftChoosePosition = mLeftClickPosition;
                BusinessDistrict bd = mRightList.get(position);
                mShowTitle = bd.getBusinessName();
                String district = mLeftList.get(mLeftClickPosition);
                if (mShowTitle.equals(getResources().getString(R.string.bd_all_tip))) {
                    mShowTitle =
                            mLeftClickPosition == 0 ? getResources().getString(R.string.business_zone) : district;
                } else if (mShowTitle.equals(getResources().getString(R.string.bd_others_tip))) {
                    mShowTitle = district + "/" + mShowTitle;
                }
                if (mOnSelectListener != null) {
                    mOnSelectListener.selectItem(bd.getId(), district);
                }

            }
        });
        if (mRightChoosePosition < mRightList.size())
            mShowTitle = mRightList.get(mRightChoosePosition).getBusinessName();
        setListViewSelect();

    }


    public void defaultChooseListView() {
        mAdapterLeft.setSelectedPosition(mLeftChoosePosition);
        if (mLeftChoosePosition < mLeftIndexOfRightList.size()) {
            mRightList.clear();
            mRightList.addAll(mLeftIndexOfRightList.get(mLeftChoosePosition));
            mRightTitleList.clear();
            for (BusinessDistrict bd : mRightList) {
                mRightTitleList.add(bd.getBusinessName());
            }
            mLeftClickPosition = mLeftChoosePosition;
            if (mLeftClickPosition != mLeftChoosePosition) {
                mAdapterRight.setSelectedPositionNoNotify(-1);
            } else
                mAdapterRight.setSelectedPositionNoNotify(mRightChoosePosition);
            mAdapterRight.notifyDataSetChanged();
        }
        setListViewSelect();
    }

    public void setListViewSelect() {
        lvLeft.setSelection(mLeftChoosePosition);
        lvRight.setSelection(mRightChoosePosition);
    }


    public String getShowText() {
        return mShowTitle;
    }

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        mOnSelectListener = onSelectListener;
    }

    public boolean isBdDataEmpty() {
        return LangUtils.isEmpty(mLeftList);
    }

    public interface OnSelectListener {
        public void selectItem(long nBdId, String strDistrict);
    }
}
