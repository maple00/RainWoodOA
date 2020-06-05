package com.rainwood.oa.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.FontAndFont;
import com.rainwood.oa.ui.widget.GroupTextText;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/4 9:42
 * @Desc: 订单详情中的订单信息中的主体信息
 */
public final class OrderDataCostAdapter extends BaseAdapter {

    private List<FontAndFont> mList;

    public void setList(List<FontAndFont> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return ListUtils.getSize(mList);
    }

    @Override
    public FontAndFont getItem(int position) {
        return mList.get(position);
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_data_cost, parent, false);
            ViewBind.inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.title.setText(getItem(position).getTitle());
        holder.money.setText(getItem(position).getDesc());
        return convertView;
    }

    private static class ViewHolder {
        @ViewInject(R.id.tv_title)
        private TextView title;
        @ViewInject(R.id.tv_money)
        private TextView money;
    }
}
