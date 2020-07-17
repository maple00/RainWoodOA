package com.rainwood.oa.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.AdminOverTime;
import com.rainwood.oa.model.domain.FinancialInvoiceRecord;
import com.rainwood.oa.model.domain.InvoiceRecord;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.utils.FontSwitchUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: sxs
 * @Time: 2020/5/30 20:09
 * @Desc: 财务管理----开票记录adapter
 */
public final class FinancialInvoiceRecordAdapter extends RecyclerView.Adapter<FinancialInvoiceRecordAdapter.ViewHolder> {

    private List<FinancialInvoiceRecord> mRecordList;
    private Context mContext;

    public void setRecordList(List<FinancialInvoiceRecord> recordList) {
        mRecordList = recordList;
        notifyDataSetChanged();
    }

    /**
     * 追加一些数据
     */
    public void addData(List<FinancialInvoiceRecord> data) {
        if (data == null || data.size() == 0) {
            return;
        }
        if (mRecordList == null || mRecordList.size() == 0) {
            setRecordList(data);
        } else {
            mRecordList.addAll(data);
            notifyItemRangeInserted(mRecordList.size() - data.size(), data.size());
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_invoice_records, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.type.setText(mRecordList.get(position).getType());
        holder.invoiceId.setText("发票编号" + mRecordList.get(position).getInvoiceNum());
        holder.status.setText("是".equals(mRecordList.get(position).getOpen()) ? "已开票" : "未开票");
        holder.status.setTextColor("是".equals(mRecordList.get(position).getOpen())
                ? mContext.getColor(R.color.colorPrimary)
                : mContext.getColor(R.color.labelColor));
        holder.invoiceObj.setText(mRecordList.get(position).getCompanyName());
        holder.time.setText(mRecordList.get(position).getOpenDate());
        holder.money.setText(Html.fromHtml("<font size='"
                + FontSwitchUtil.dip2px(mContext, 12f) + "' >￥</font>"
                + "<font size='" + FontSwitchUtil.dip2px(mContext, 16f) + "' >"
                + mRecordList.get(position).getMoney() + "</font>"));
    }

    @Override
    public int getItemCount() {
        return ListUtils.getSize(mRecordList);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @ViewInject(R.id.ll_item_invoice_record)
        private LinearLayout itemInvoiceRecord;
        @ViewInject(R.id.tv_type)
        private TextView type;
        @ViewInject(R.id.tv_invoice_id)
        private TextView invoiceId;
        @ViewInject(R.id.tv_status)
        private TextView status;
        @ViewInject(R.id.tv_invoice_obj)
        private TextView invoiceObj;
        @ViewInject(R.id.tv_invoice_money)
        private TextView money;
        @ViewInject(R.id.tv_invoice_time)
        private TextView time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ViewBind.inject(this, itemView);
        }
    }
}
