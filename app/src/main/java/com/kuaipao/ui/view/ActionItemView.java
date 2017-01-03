package com.kuaipao.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.kuaipao.base.inject.From;
import com.kuaipao.base.inject.InjectUtils;
import com.kuaipao.manager.R;
import com.kuaipao.model.beans.XAction;
import com.kuaipao.model.beans.XActionItem;
import com.kuaipao.utils.LangUtils;
import com.kuaipao.utils.ViewUtils;

import java.util.ArrayList;

/**
 * Created by MVEN on 16/9/19.
 * <p>
 * email: magiwen@126.com.
 */
public class ActionItemView extends RelativeLayout implements View.OnClickListener {
    private static final int ITEM_HEIGHT = ViewUtils.rp(40);

    @From(R.id.tv_action_name)
    private EditText editName;

    @From(R.id.iv_add)
    private ImageView ivAdd;

    @From(R.id.iv_delete)
    private ImageView ivDelete;

    @From(R.id.ll_action_item_container)
    private LinearLayout llItemContainer;

    @From(R.id.ll_delete_action)
    private LinearLayout llDeleteAction;

    @From(R.id.ll_add_action)
    private LinearLayout llAddAction;

    private LinearLayout.LayoutParams itemParams;
    private OnActionClickListener mActionClickListener;

    public ActionItemView(Context context) {
        super(context);
        initViews();
    }

    public ActionItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public void setData(XAction action) {
        if (action == null) {
            return;
        }

        editName.setText(action.name);
        llItemContainer.removeAllViews();
        if (!LangUtils.isEmpty(action.items)) {
            for (int i = 0; i < action.items.size(); i++) {
                View view = createItemView(action.items.get(i));
                if (view != null) {
                    llItemContainer.addView(view, itemParams);
                }
            }
        }
    }

    public void setAddActionVisible(boolean isVisible) {
        llAddAction.setVisibility(isVisible ? VISIBLE : GONE);
    }

    public void setActionClickListener(OnActionClickListener actionClickListener) {
        mActionClickListener = actionClickListener;
    }

    private View createItemView(XActionItem xActionItem) {
        if (getContext() == null) {
            return null;
        }
        View view = inflate(getContext(), R.layout.action_count_item_layout, null);
        EditText edtWeight = (EditText) view.findViewById(R.id.edt_weight);
        EditText edtTimes = (EditText) view.findViewById(R.id.edt_times);
        EditText edtGroup = (EditText) view.findViewById(R.id.edt_group);

        if (xActionItem != null) {
            edtTimes.setText("" + xActionItem.weight);
            edtWeight.setText("" + xActionItem.weight);
            edtGroup.setText("" + xActionItem.group);
        }

        return view;
    }

    public XAction getData() {
        XAction action = new XAction();
        if (editName.getText() != null) {
            action.name = editName.getText().toString();
        }

        action.items = new ArrayList<>();
        for (int i = 0; i < llItemContainer.getChildCount(); i++) {
            View view = llItemContainer.getChildAt(i);
            EditText edtWeight = (EditText) view.findViewById(R.id.edt_weight);
            EditText edtTimes = (EditText) view.findViewById(R.id.edt_times);
            EditText edtGroup = (EditText) view.findViewById(R.id.edt_group);

            XActionItem item = new XActionItem();
            item.weight = LangUtils.parseInt(edtWeight.getText(), 0);
            item.times = LangUtils.parseInt(edtTimes.getText(), 0);
            item.group = LangUtils.parseInt(edtGroup.getText(), 0);
            if (item.weight == 0 && item.times == 0 && item.group == 0){
                continue;
            }
            action.items.add(item);
        }

        return action;
    }

    private void initViews() {
        inflate(getContext(), R.layout.ui_action_item_view, this);
        InjectUtils.autoInject(this);

        itemParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ITEM_HEIGHT);
        ivAdd.setOnClickListener(this);
        ivDelete.setOnClickListener(this);
        llAddAction.setOnClickListener(this);
        llDeleteAction.setOnClickListener(this);
    }

    public int getActionItemViewCount() {
        return llItemContainer.getChildCount();
    }

    public void addChildView(XActionItem item) {
        llItemContainer.addView(createItemView(item));
    }

    @Override
    public void onClick(View v) {
        if (v == ivAdd) {
            llItemContainer.addView(createItemView(null));
        } else if (v == ivDelete) {
            int count = llItemContainer.getChildCount();
            if (count > 1) {
                llItemContainer.removeView(llItemContainer.getChildAt(count - 1));
            }
        } else if (v == llDeleteAction) {
            if (mActionClickListener != null) {
                mActionClickListener.deleteAction(ActionItemView.this);
            }
        } else if (v == llAddAction) {
            if (mActionClickListener != null) {
                mActionClickListener.addAction();
            }
        }
    }


    public interface OnActionClickListener {
        void addAction();

        void deleteAction(ActionItemView actionItemView);
    }
}
