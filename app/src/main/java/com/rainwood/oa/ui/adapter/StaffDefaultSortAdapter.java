package com.rainwood.oa.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.SelectedItem;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/24 10:15
 * @Desc:
 */
public final class StaffDefaultSortAdapter extends BaseAdapter {

    private List<SelectedItem> mItemList;

    public void setItemList(List<SelectedItem> itemList) {
        mItemList = itemList;
    }

    @Override
    public int getCount() {
        return ListUtils.getSize(mItemList);
    }

    @Override
    public SelectedItem getItem(int position) {
        return mItemList.get(position);
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.pop_grid_list_checked, parent, false);
            convertView.setTag(holder);
            ViewBind.inject(holder, convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(getItem(position).getName());
        holder.name.setTextColor(getItem(position).isHasSelected()
                ? parent.getContext().getColor(R.color.colorPrimary)
                : parent.getContext().getColor(R.color.fontColor));
        holder.choose.setVisibility(getItem(position).isHasSelected() ? View.VISIBLE : View.GONE);
        // 点击事件
        holder.itemDefaultSort.setOnClickListener(v -> {
            mOnClickListener.onClickItem(getItem(position), position);
            notifyDataSetChanged();
        });
        return convertView;
    }

    private static class ViewHolder {
        @ViewInject(R.id.rl_item_default_sort)
        private RelativeLayout itemDefaultSort;
        @ViewInject(R.id.tv_name)
        private TextView name;
        @ViewInject(R.id.iv_choose)
        private ImageView choose;
    }

    public interface OnClickListener {
        /**
         * 点击item
         *
         * @param selectedItem
         * @param position
         */
        void onClickItem(SelectedItem selectedItem, int position);
    }

    private OnClickListener mOnClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }
}
