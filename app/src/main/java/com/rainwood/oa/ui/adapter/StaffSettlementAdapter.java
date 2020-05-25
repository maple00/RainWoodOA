package com.rainwood.oa.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.StaffSettlement;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/25 18:17
 * @Desc: 用户结算账户adapter
 */
public final class StaffSettlementAdapter extends RecyclerView.Adapter<StaffSettlementAdapter.ViewHolder> {

    private List<StaffSettlement> mSettlementList;
    private Context mContext;

    public void setSettlementList(List<StaffSettlement> settlementList) {
        mSettlementList = settlementList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_staff_settlement, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.event.setText(TextUtils.isEmpty(mSettlementList.get(position).getEvent())
                ? "" : mSettlementList.get(position).getEvent());
        holder.voucher.setVisibility(TextUtils.isEmpty(mSettlementList.get(position).getVoucher())
                ? View.GONE : View.VISIBLE);
        holder.time.setText(mSettlementList.get(position).getTime());
        holder.money.setText(mSettlementList.get(position).getMoney());
        holder.money.setTextColor(mSettlementList.get(position).getMoney().startsWith("+")
                ? mContext.getColor(R.color.colorPrimary)
                : mContext.getColor(R.color.fontColor));
    }

    @Override
    public int getItemCount() {
        return ListUtils.getSize(mSettlementList);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @ViewInject(R.id.tv_event)
        private TextView event;
        @ViewInject(R.id.iv_voucher)
        private ImageView voucher;
        @ViewInject(R.id.tv_money)
        private TextView money;
        @ViewInject(R.id.tv_time)
        private TextView time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ViewBind.inject(this, itemView);
        }
    }
}