package com.rainwood.oa.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.SubRoleXPermission;
import com.rainwood.oa.ui.widget.MeasureGridView;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/21 15:44
 * @Desc: 权限详情-- 某个模块下的所有权限
 */
public final class RolePermissionAdapter extends BaseAdapter {

    private List<SubRoleXPermission> mPermissionList;

    public void setPermissionList(List<SubRoleXPermission> permissionList) {
        mPermissionList = permissionList;
    }

    @Override
    public int getCount() {
        return ListUtils.getSize(mPermissionList);
    }

    @Override
    public SubRoleXPermission getItem(int position) {
        return mPermissionList.get(position);
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_item_permission_detail, parent, false);
            ViewBind.inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.permissionChecked.setChecked(mPermissionList.get(position).isChecked());
        holder.permissionName.setText(mPermissionList.get(position).getXPermission());
        // 具体的权限
        DetailPermissionAdapter permissionAdapter = new DetailPermissionAdapter();
        holder.detailPermission.setAdapter(permissionAdapter);
        holder.detailPermission.setNumColumns(2);
        permissionAdapter.setXPermissionList(getItem(position).getDetailXPermissions());
        return convertView;
    }


    private static class ViewHolder {
        @ViewInject(R.id.cb_checked)
        private CheckBox permissionChecked;
        @ViewInject(R.id.tv_permission_name)
        private TextView permissionName;
        @ViewInject(R.id.mgv_detail_permission)
        private MeasureGridView detailPermission;
    }
}
