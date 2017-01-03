package com.kuaipao.ui.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kuaipao.utils.LangUtils;
import com.kuaipao.manager.R;

public class ExpandListItemAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private List<String> mListData;
    private int mSelectedPos = -1;
    private String mSelectedText = "";
    private int mNormalTextColor;
    private int mSelectedTextColor;
    private int mNormalDrawbleID;
    private int mSelectedDrawbleID;
    private OnClickListener onClickListener;
    private OnItemClickListener mOnItemClickListener;

    public ExpandListItemAdapter(Context context, List<String> listData, int sID, int nID,
                                 int sTextColorID, int nTextColorID) {
        super(context, R.string.app_name, listData);
        mContext = context;
        mListData = listData;
        mSelectedDrawbleID = sID;
        mNormalDrawbleID = nID;
        mSelectedTextColor = context.getResources().getColor(sTextColorID);
        mNormalTextColor = context.getResources().getColor(nTextColorID);
        init();
    }

    private void init() {
        onClickListener = new OnClickListener() {

            @Override
            public void onClick(View view) {
                int selectPos = (Integer) view.getTag();

                mSelectedPos = (Integer) view.getTag();
                setSelectedPosition(mSelectedPos);
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, mSelectedPos);
                }
            }
        };
    }

    /**
     * 设置选中的position,并通知列表刷新
     */
    public void setSelectedPosition(int pos) {
        mSelectedPos = pos;
        if (mListData != null && pos < mListData.size() && pos >= 0) {
            mSelectedText = mListData.get(pos);
            notifyDataSetChanged();
        } else
            mSelectedText = "";
    }

    /**
     * 设置选中的position,但不通知刷新
     */
    public void setSelectedPositionNoNotify(int pos) {
        mSelectedPos = pos;
        if (mListData != null && pos < mListData.size() && pos >= 0) {
            mSelectedText = mListData.get(pos);
        } else
            mSelectedText = "";
    }

    /**
     * 获取选中的position
     */
    public int getSelectedPosition() {
        if (mListData != null && mSelectedPos < mListData.size()) {
            return mSelectedPos;
        }

        return -1;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView != null)
            convertView = null;

        convertView = View.inflate(mContext, R.layout.expand_list_item, null);// fix samsung bug
        convertView.setTag(position);

        TextView tvName = (TextView) convertView.findViewById(R.id.tv_expand_name);

        tvName.setText(mListData.get(position));

        if (mSelectedText != null && mSelectedText.equals(mListData.get(position))) {
            convertView.setBackgroundResource(mSelectedDrawbleID);// 设置选中的背景图片
            tvName.setTextColor(mSelectedTextColor);
        } else {
            convertView.setBackgroundResource(mNormalDrawbleID);// 设置未选中状态背景图片
            tvName.setTextColor(mNormalTextColor);
        }

        if (LangUtils.isNotEmpty(mListData.get(position))) {
            convertView.setOnClickListener(onClickListener);
        } else {
            convertView.setOnClickListener(null);
        }

        return convertView;
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        mOnItemClickListener = l;
    }

    /**
     * 重新定义菜单选项单击接口
     */
    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

}
