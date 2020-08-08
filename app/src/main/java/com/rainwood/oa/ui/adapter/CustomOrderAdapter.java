package com.rainwood.oa.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.AdminOverTime;
import com.rainwood.oa.model.domain.CustomOrder;
import com.rainwood.oa.ui.widget.MeasureGridView;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/29 9:40
 * @Desc: 客户订单列表
 */
public final class CustomOrderAdapter extends RecyclerView.Adapter<CustomOrderAdapter.ViewHolder> {

    private List<CustomOrder> mCustomOrderList;
    private Context mContext;

    public void setCustomOrderList(List<CustomOrder> customOrderList) {
        mCustomOrderList = customOrderList;
        notifyDataSetChanged();
    }

    /**
     * 追加一些数据
     */
    public void addData(List<CustomOrder> data) {
        if (data == null || data.size() == 0) {
            return;
        }

        if (mCustomOrderList == null || mCustomOrderList.size() == 0) {
            setCustomOrderList(data);
        } else {
            mCustomOrderList.addAll(data);
            notifyItemRangeInserted(mCustomOrderList.size() - data.size(), data.size());
        }
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_custom_order, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.orderNo.setText("订单号：" + mCustomOrderList.get(position).getId());
        holder.orderStatus.setText(mCustomOrderList.get(position).getWorkFlow());
        holder.orderStatus.setTextColor(mCustomOrderList.get(position).getWorkFlow().equals("草稿")
                ? mContext.getColor(R.color.tipsColor) : mContext.getColor(R.color.colorPrimary));
        holder.orderName.setText(mCustomOrderList.get(position).getName());
        holder.orderMoney.setText("￥" + mCustomOrderList.get(position).getMoney());
        holder.orderDate.setText(mCustomOrderList.get(position).getCycle() + "天 | "
                + mCustomOrderList.get(position).getSignDay() + "-" + mCustomOrderList.get(position).getEndDay());
        // 订单中的属性，有则显示，无则隐藏
        SubOrderAdapter orderAdapter = new SubOrderAdapter();
        holder.orderValueList.setNumColumns(2);
        holder.orderValueList.setAdapter(orderAdapter);
        orderAdapter.setList(mCustomOrderList.get(position).getValueList());

        // 点击事件

    }

    @Override
    public int getItemCount() {
        return ListUtils.getSize(mCustomOrderList);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @ViewInject(R.id.ll_item_order)
        private LinearLayout itemOrder;
        @ViewInject(R.id.tv_order_no)
        private TextView orderNo;
        @ViewInject(R.id.tv_order_status)
        private TextView orderStatus;
        @ViewInject(R.id.tv_order_name)
        private TextView orderName;
        @ViewInject(R.id.tv_order_money)
        private TextView orderMoney;
        @ViewInject(R.id.mgv_order_values)
        private MeasureGridView orderValueList;
        @ViewInject(R.id.tv_order_date)
        private TextView orderDate;
        @ViewInject(R.id.btn_order_edit)
        private Button orderEdit;
        @ViewInject(R.id.btn_order_delete)
        private Button orderDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ViewBind.inject(this, itemView);
        }
    }
}
