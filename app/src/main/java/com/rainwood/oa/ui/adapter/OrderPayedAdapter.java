package com.rainwood.oa.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.OrderPayed;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/4 11:01
 * @Desc: 订单详情已付费用adapter
 */
public final class OrderPayedAdapter extends BaseAdapter {

    private List<OrderPayed> mPayedList;

    public void setPayedList(List<OrderPayed> payedList) {
        mPayedList = payedList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return ListUtils.getSize(mPayedList);
    }

    @Override
    public OrderPayed getItem(int position) {
        return mPayedList.get(position);
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_payed, parent, false);
            ViewBind.inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.type.setText(getItem(position).getType());
        holder.money.setText(getItem(position).getMoney());
        holder.content.setText(getItem(position).getText());
        holder.time.setText(getItem(position).getPayDate());
        holder.line.setVisibility((position == ListUtils.getSize(mPayedList) - 1) ? View.GONE : View.VISIBLE);
        return convertView;
    }

    private static class ViewHolder {
        @ViewInject(R.id.tv_type)
        private TextView type;
        @ViewInject(R.id.tv_money)
        private TextView money;
        @ViewInject(R.id.tv_content)
        private TextView content;
        @ViewInject(R.id.tv_time)
        private TextView time;
        @ViewInject(R.id.horizontal_line)
        private View line;
    }
}
