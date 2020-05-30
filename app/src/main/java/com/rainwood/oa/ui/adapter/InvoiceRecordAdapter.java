package com.rainwood.oa.ui.adapter;

import android.annotation.SuppressLint;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.InvoiceRecord;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.utils.FontSwitchUtil;

import java.util.List;

/**
 * @Author: sxs
 * @Time: 2020/5/30 20:09
 * @Desc: 开票记录adapter
 */
public final class InvoiceRecordAdapter extends BaseAdapter {

    private List<InvoiceRecord> mRecordList;

    public void setRecordList(List<InvoiceRecord> recordList) {
        mRecordList = recordList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return ListUtils.getSize(mRecordList);
    }

    @Override
    public InvoiceRecord getItem(int position) {
        return mRecordList.get(position);
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_invoice_records, parent, false);
            ViewBind.inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.type.setText(getItem(position).getType());
        holder.invoiceId.setText("发票编号" + getItem(position).getInvoiceNum());
        holder.status.setText("是".equals(getItem(position).getOpen()) ? "已开票" : "未开票");
        holder.status.setTextColor("是".equals(getItem(position).getOpen())
                ? parent.getContext().getColor(R.color.colorPrimary)
                : parent.getContext().getColor(R.color.labelColor));
        holder.invoiceObj.setText(getItem(position).getCompany());
        holder.time.setText(getItem(position).getOpenDate());
        holder.money.setText(Html.fromHtml("<font size='"
                + FontSwitchUtil.dip2px(parent.getContext(), 12f) + "' >￥</font>"
                + "<font size='" + FontSwitchUtil.dip2px(parent.getContext(), 16f) + "' >"
                + getItem(position).getMoney() + "</font>"));
        // 点击事件

        return convertView;
    }

    private static class ViewHolder {
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
    }
}
