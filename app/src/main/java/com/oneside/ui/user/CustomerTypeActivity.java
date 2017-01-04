package com.oneside.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.oneside.base.BaseActivity;
import com.oneside.base.inject.From;
import com.oneside.base.inject.XAnnotation;
import com.oneside.base.model.BasePageParam;
import com.oneside.ui.view.NoScrollGridView;
import com.oneside.utils.LangUtils;
import com.oneside.utils.ViewUtils;
import com.oneside.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 接待客户类型界面
 * <p/>
 * Created by pingfu
 */
public class CustomerTypeActivity extends BaseActivity implements CustomerTypeAdapter.OnItemClickHandler {

    @From(R.id.gv_items)
    private NoScrollGridView gvItems;

    private TextView tvRight;
    private List<String> mTypeItems;
    private CustomerTypeAdapter mAdapter;

    private String mType;
    private int mTypeCode;

    @XAnnotation
    private BasePageParam mPageParam;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_customer_type);
        tvRight = ViewUtils.createTitleBarRightTextView(this, "确定");
        setTitle("访客类型", true, tvRight);

        initUI();
    }

    private void initUI() {
        mTypeItems = getTestData();
        mAdapter = new CustomerTypeAdapter(this, mTypeItems);
        if (mPageParam.gymId >= 0) {
            if (mPageParam.gymId == 0) {
                mType = mTypeItems.get(mTypeItems.size() - 1);
                mTypeCode = 0;
                mAdapter.setSelectedPosition(mTypeItems.size() - 1);
            } else {
                mTypeCode = (int) mPageParam.gymId;
                mType = mTypeItems.get(mTypeCode - 1);
                mAdapter.setSelectedPosition((int) mPageParam.gymId - 1);
            }
        }
        mAdapter.setClickHandler(this);
        gvItems.setAdapter(mAdapter);
        tvRight.setOnClickListener(this);
    }

    private List<String> getTestData() {
        List<String> items = new ArrayList<>();
        items.add("自己走进来");
        items.add("打电话问询");
        items.add("预约进来的");
        items.add("会员带朋友");
        items.add("到外派单");
        items.add("市场活动");
        items.add("企业团体");
        items.add("单日单次卡");
        items.add("其他");
        return items;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        if (v == tvRight) {
            if (LangUtils.isEmpty(mType)) {
                ViewUtils.showToast("请选择类型", Toast.LENGTH_LONG);
            } else {
                Intent intent = new Intent();
                intent.putExtra("type", mType);
                intent.putExtra("typeCode", mTypeCode);
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
        mAdapter.setSelectedPosition(position);
        mType = mTypeItems.get(position);
        mTypeCode = (position + 1) % mTypeItems.size();
    }
}
