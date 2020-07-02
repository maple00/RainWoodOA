package com.rainwood.oa.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.SelectedItem;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/27 13:24
 * @Desc: common GridView adapter
 */
public final class CommonGridAdapter extends BaseAdapter {

    private List<SelectedItem> mTextList;

    public void setTextList(List<SelectedItem> textList) {
        mTextList = textList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return ListUtils.getSize(mTextList);
    }

    @Override
    public SelectedItem getItem(int position) {
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
        holder.text.setBackgroundResource(getItem(position).isHasSelected()
                ? R.drawable.shape_condition_click_bg : R.drawable.shape_condition_unclick_bg);
        // 点击事件
        holder.text.setOnClickListener(v -> {
            mOnClickListener.onClickItem(getItem(position), position);
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
        void onClickItem(SelectedItem item, int position);
    }

    private OnClickListener mOnClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }
}
