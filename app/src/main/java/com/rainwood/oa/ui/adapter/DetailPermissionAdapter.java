package com.rainwood.oa.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.RoleDetailXPermission;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/21 16:00
 * @Desc: 具体的详细权限
 */
public final class DetailPermissionAdapter extends BaseAdapter {

    private List<RoleDetailXPermission> mXPermissionList;

    public void setXPermissionList(List<RoleDetailXPermission> XPermissionList) {
        mXPermissionList = XPermissionList;
    }

    @Override
    public int getCount() {
        return ListUtils.getSize(mXPermissionList);
    }

    @Override
    public RoleDetailXPermission getItem(int position) {
        return mXPermissionList.get(position);
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grand_detail_permission,
                    parent, false);
            ViewBind.inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.permission.setChecked(getItem(position).isChecked());
        holder.permissionName.setText(getItem(position).getDetailX());
        return convertView;
    }

    private static class ViewHolder {
        @ViewInject(R.id.cb_checked)
        private CheckBox permission;
        @ViewInject(R.id.tv_permission_name)
        private TextView permissionName;
    }
}
