package com.rainwood.oa.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.CustomScreenAll;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/27 13:24
 * @Desc: common GridView adapter
 */
public final class CustomScreenItemAdapter extends BaseAdapter {

    private List<CustomScreenAll.InnerCustomScreen> mTextList;
    private int parentPos;
    private CustomScreenAll mCustomScreenAll;

    public void setTextList(List<CustomScreenAll.InnerCustomScreen> textList, CustomScreenAll screenAll, int parentPos) {
        mTextList = textList;
        this.parentPos = parentPos;
        mCustomScreenAll = screenAll;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return ListUtils.getSize(mTextList);
    }

    @Override
    public CustomScreenAll.InnerCustomScreen getItem(int position) {
        return mTextList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text_view, parent, false);
            ViewBind.inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.text.setText(getItem(position).getName());
        holder.text.setBackgroundResource(getItem(position).isSelected()
                ? R.drawable.shape_condition_click_bg : R.drawable.shape_condition_unclick_bg);
        holder.text.setTextColor(getItem(position).isSelected()
                ? parent.getContext().getColor(R.color.colorPrimary)
                : parent.getContext().getColor(R.color.fontColor));
        // 点击事件
        holder.text.setOnClickListener(v -> {
            mOnClickListener.onClickItem(getItem(position), position, mCustomScreenAll, parentPos);
            notifyDataSetChanged();
        });
        return convertView;
    }

    private static class ViewHolder {
        @ViewInject(R.id.tv_text)
        private TextView text;
    }

    public interface OnClickListener {
        /**
         * 选中item
         *
         * @param item
         * @param position
         */
        void onClickItem(CustomScreenAll.InnerCustomScreen item, int position, CustomScreenAll screenAll, int parentPos);
    }

    private OnClickListener mOnClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }
}
