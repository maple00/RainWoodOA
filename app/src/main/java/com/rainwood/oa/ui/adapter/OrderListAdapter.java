package com.rainwood.oa.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.Order;
import com.rainwood.oa.ui.widget.MeasureGridView;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/20 10:27
 * @Desc: 订单列表
 */
public final class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {

    private List<Order> mList;
    private Context mContext;

    public void setList(List<Order> list) {
        mList = list;
        notifyDataSetChanged();
    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    private int count = 0;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_order_view, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.orderNo.setText("订单号：" + mList.get(position).getId());
        holder.status.setText(mList.get(position).getWorkFlow());
        holder.status.setTextColor("已完成".equals(mList.get(position).getWorkFlow())
                ? mContext.getColor(R.color.colorPrimary)
                : mContext.getColor(R.color.tipsColor));
        holder.orderName.setText(mList.get(position).getName());
        holder.money.setText("￥" + mList.get(position).getMoney());
        holder.chargeMan.setText(mList.get(position).getStaffName());
        holder.limitDay.setText(mList.get(position).getTimeLimit() + "天");
        holder.limitDate.setText(mList.get(position).getStartTime() + "-"
                + mList.get(position).getEndTime());
        // 订单的属性
        OrderNatureAdapter natureAdapter = new OrderNatureAdapter();
        natureAdapter.setList(mList.get(position).getNatureList());
        holder.orderNature.setAdapter(natureAdapter);
        holder.orderNature.setNumColumns(2);

        if (TextUtils.isEmpty(mList.get(position).getTimeLimit())
                || TextUtils.isEmpty(mList.get(position).getStartTime())
                || TextUtils.isEmpty(mList.get(position).getEndTime())) {
            holder.limit.setVisibility(View.GONE);
        } else {
            holder.limit.setVisibility(View.VISIBLE);
        }
        // 点击事件

    }

    @Override
    public int getItemCount() {
        return ListUtils.getSize(mList);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @ViewInject(R.id.tv_order_no)
        private TextView orderNo;
        @ViewInject(R.id.tv_status)
        private TextView status;
        @ViewInject(R.id.tv_order_name)
        private TextView orderName;
        @ViewInject(R.id.tv_order_money)
        private TextView money;
        @ViewInject(R.id.tv_charge_man)
        private TextView chargeMan;
        @ViewInject(R.id.mgv_order_nature)
        private MeasureGridView orderNature;
        @ViewInject(R.id.tv_limit_day)
        private TextView limitDay;
        @ViewInject(R.id.tv_limit_date)
        private TextView limitDate;
        @ViewInject(R.id.ll_limit)
        private LinearLayout limit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ViewBind.inject(this, itemView);
        }
    }
}
