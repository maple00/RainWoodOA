package com.rainwood.oa.ui.adapter;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.OrderTask;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/4 10:30
 * @Desc: 订单详情中的任务分配adapter
 */
public final class OrderTaskAdapter extends BaseAdapter {

    private List<OrderTask> mTaskList;

    public void setTaskList(List<OrderTask> taskList) {
        mTaskList = taskList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return ListUtils.getSize(mTaskList);
    }

    @Override
    public OrderTask getItem(int position) {
        return mTaskList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_task, parent, false);
            ViewBind.inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.taskName.setText(getItem(position).getName() + "-" + getItem(position).getStaffName());
        holder.rate.setText(getItem(position).getRate());
        holder.money.setText(getItem(position).getMoney());
        holder.payed.setText(getItem(position).getPay());
        holder.balance.setText(getItem(position).getBalance());
        holder.content.setText(getItem(position).getSummary());
        holder.content.setVisibility(TextUtils.isEmpty(getItem(position).getSummary()) ? View.GONE : View.VISIBLE);
        return convertView;
    }

    private static class ViewHolder {
        @ViewInject(R.id.tv_task_name)
        private TextView taskName;
        @ViewInject(R.id.tv_rate)
        private TextView rate;
        @ViewInject(R.id.tv_money)
        private TextView money;
        @ViewInject(R.id.tv_payed)
        private TextView payed;
        @ViewInject(R.id.tv_balance)
        private TextView balance;
        @ViewInject(R.id.tv_content)
        private TextView content;
    }
}
