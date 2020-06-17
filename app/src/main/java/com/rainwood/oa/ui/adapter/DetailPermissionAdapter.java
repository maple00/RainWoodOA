package com.rainwood.oa.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
public final class DetailPermissionAdapter extends RecyclerView.Adapter<DetailPermissionAdapter.ViewHolder> {

    private List<RoleDetailXPermission> mXPermissionList;

    public void setXPermissionList(List<RoleDetailXPermission> XPermissionList) {
        mXPermissionList = XPermissionList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grand_detail_permission,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.permission.setChecked("yes".equals(mXPermissionList.get(position).getHook()));
        holder.permissionName.setText(mXPermissionList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return ListUtils.getSize(mXPermissionList);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @ViewInject(R.id.cb_checked)
        private CheckBox permission;
        @ViewInject(R.id.tv_permission_name)
        private TextView permissionName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ViewBind.inject(this, itemView);
        }
    }
}
