package com.oneside.ui.customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.oneside.base.BaseActivity;
import com.oneside.base.inject.From;
import com.oneside.base.inject.XAnnotation;
import com.oneside.base.model.BasePageParam;
import com.oneside.R;
import com.oneside.ui.view.NoScrollGridView;
import com.oneside.utils.LangUtils;
import com.oneside.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

import static com.oneside.utils.ViewUtils.find;
import static com.oneside.utils.WebUtils.getJsonArray;
import static com.oneside.utils.WebUtils.getJsonInt;
import static com.oneside.utils.WebUtils.getJsonObject;

/**
 * 接待客户来源界面
 * <p/>
 * Created by pingfu
 */
public class CustomerSourceActivity extends BaseActivity implements CustomerTypeAdapter.OnItemClickHandler {

    @From(R.id.gv_items)
    private NoScrollGridView gvItems;

    private TextView tvRight;
    private List<String> mTypeItems;
    private CustomerTypeAdapter mAdapter;
    private String mSource;
    private int mSourceCode;

    @XAnnotation
    private BasePageParam mPageParam;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_customer_type);
        tvRight = ViewUtils.createTitleBarRightTextView(this, "确定");
        setTitle("信息来源", true, tvRight);
        initUI();
    }

    private void initUI() {
        mTypeItems = getTestData();
        mAdapter = new CustomerTypeAdapter(this, mTypeItems);
        if (mPageParam.gymId >= 0) {
            if (mPageParam.gymId == 0) {
                mSourceCode = 0;
                mSource = mTypeItems.get(mTypeItems.size() - 1);
                mAdapter.setSelectedPosition(mTypeItems.size() - 1);
            } else {
                mSourceCode = (int) mPageParam.gymId;
                mSource = mTypeItems.get(mSourceCode - 1);
                mAdapter.setSelectedPosition((int) mPageParam.gymId - 1);
            }
        }
        mAdapter.setClickHandler(this);
        gvItems.setAdapter(mAdapter);

        tvRight.setOnClickListener(this);
    }

    private List<String> getTestData() {
        List<String> items = new ArrayList<>();
        items.add("朋友介绍");
        items.add("网络");
        items.add("广告");
        items.add("招牌");
        items.add("MC邀约");
        items.add("教练");
        items.add("传单");
        items.add("市场活动");
        items.add("企业合作");
        items.add("其他");

        return items;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        if (v == tvRight) {
            if (LangUtils.isEmpty(mSource)) {
                ViewUtils.showToast("请选择来源", Toast.LENGTH_LONG);
            } else {
                Intent intent = new Intent();
                intent.putExtra("source", mSource);
                intent.putExtra("sourceCode", mSourceCode);
                setResult(RESULT_OK, intent);
                onBackPressed();
            }
        }
    }

    @Override
    public void onBackPressed() {


        super.onBackPressed();
    }

    @Override
    public void onItemClick(int position) {
        mSource = mAdapter.getItem(position);
        mSourceCode = (position + 1) % mAdapter.getCount();
        mAdapter.setSelectedPosition(position);
    }
}
