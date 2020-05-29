package com.rainwood.oa.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.AuditRecords;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/26 14:24
 * @Desc: 审核记录adapter
 */
public final class AuditRecordAdapter extends BaseAdapter {

    private List<AuditRecords> mAuditRecordsList;

    public void setAuditRecordsList(List<AuditRecords> auditRecordsList) {
        mAuditRecordsList = auditRecordsList;
    }

    @Override
    public int getCount() {
        return ListUtils.getSize(mAuditRecordsList);
    }

    @Override
    public AuditRecords getItem(int position) {
        return mAuditRecordsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_audit_records, parent, false);
            ViewBind.inject(holder, convertView);
            convertView.setTag(holder);
        }else {
           holder = (ViewHolder) convertView.getTag();
        }
        holder.record.setText(getItem(position).getContent());
        holder.time.setText(getItem(position).getTime());
        return convertView;
    }

    private static class ViewHolder{
        @ViewInject(R.id.tv_record)
        private TextView record;
        @ViewInject(R.id.tv_time)
        private TextView time;
    }
}
