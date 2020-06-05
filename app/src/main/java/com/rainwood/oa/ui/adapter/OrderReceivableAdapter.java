package com.rainwood.oa.ui.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.OrderReceivable;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/4 10:51
 * @Desc: 订单详情中的回款记录
 */
public final class OrderReceivableAdapter extends BaseAdapter {

    private List<OrderReceivable> mList;

    public void setList(List<OrderReceivable> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return ListUtils.getSize(mList);
    }

    @Override
    public OrderReceivable getItem(int position) {
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_receivable_record, parent, false);
            ViewBind.inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.money.setText(getItem(position).getMoney());
        holder.getMoneyRl.setVisibility(TextUtils.isEmpty(getItem(position).getGetMoney()) ? View.GONE : View.VISIBLE);
        holder.tips.setText(getItem(position).getText());
        holder.time.setText(getItem(position).getPayDate());
        return convertView;
    }


    private static class ViewHolder {
        @ViewInject(R.id.tv_receivable_money)
        private TextView money;
        @ViewInject(R.id.tv_money)
        private TextView getMoney;
        @ViewInject(R.id.tv_tips)
        private TextView tips;
        @ViewInject(R.id.tv_time)
        private TextView time;
        @ViewInject(R.id.rl_get_money)
        private RelativeLayout getMoneyRl;
    }
}
