package com.rainwood.oa.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.SubRoleXPermission;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.oa.utils.SpacesItemDecoration;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.utils.FontSwitchUtil;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/21 15:44
 * @Desc: 权限详情-- 某个模块下的所有权限
 */
public final class RolePermissionAdapter extends RecyclerView.Adapter<RolePermissionAdapter.ViewHolder> {

    private List<SubRoleXPermission> mPermissionList;
    private Context mContext;

    public void setPermissionList(List<SubRoleXPermission> permissionList) {
        mPermissionList = permissionList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.sub_item_permission_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.permissionChecked.setChecked(mPermissionList.get(position).isChecked());
        holder.permissionName.setText(mPermissionList.get(position).getMenuTwo());
        // 具体的权限
        holder.detailPermission.setLayoutManager(new GridLayoutManager(mContext, 2));
        holder.detailPermission.addItemDecoration(new SpacesItemDecoration(0, 0, 0,
                FontSwitchUtil.dip2px(mContext, 14f)));
        DetailPermissionAdapter permissionAdapter = new DetailPermissionAdapter();
        holder.detailPermission.setAdapter(permissionAdapter);
        permissionAdapter.setXPermissionList(mPermissionList.get(position).getArray());
    }

    @Override
    public int getItemCount() {
        return ListUtils.getSize(mPermissionList);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @ViewInject(R.id.cb_checked)
        private CheckBox permissionChecked;
        @ViewInject(R.id.tv_permission_name)
        private TextView permissionName;
        @ViewInject(R.id.rv_detail_permission)
        private RecyclerView detailPermission;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ViewBind.inject(this, itemView);
        }
    }
}
