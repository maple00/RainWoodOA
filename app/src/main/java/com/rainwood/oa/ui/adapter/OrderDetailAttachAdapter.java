package com.rainwood.oa.ui.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.OrderDetailAttachment;
import com.rainwood.oa.ui.widget.GroupIconText;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/4 10:01
 * @Desc: 订单详情附件列表adapter
 */
public final class OrderDetailAttachAdapter extends BaseAdapter {

    private List<OrderDetailAttachment> mAttachmentList;

    public void setAttachmentList(List<OrderDetailAttachment> attachmentList) {
        mAttachmentList = attachmentList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return ListUtils.getSize(mAttachmentList);
    }

    @Override
    public OrderDetailAttachment getItem(int position) {
        return mAttachmentList.get(position);
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_attachment, parent, false);
            ViewBind.inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.checked.setVisibility(View.GONE);
        holder.attachName.setText(getItem(position).getName());
        holder.nameTime.setText(getItem(position).getStaffName() + " " + getItem(position).getTime());
        // 点击事件
        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.d("sxs", "订单附件下载");
            }
        });
        holder.preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.d("sxs", "订单附件预览");
            }
        });
        return convertView;
    }

    private static class ViewHolder {
        @ViewInject(R.id.cb_checked)
        private CheckBox checked;
        @ViewInject(R.id.tv_attach_name)
        private TextView attachName;
        @ViewInject(R.id.tv_name_time)
        private TextView nameTime;
        @ViewInject(R.id.git_download)
        private GroupIconText download;
        @ViewInject(R.id.git_preview)
        private GroupIconText preview;
    }
}
