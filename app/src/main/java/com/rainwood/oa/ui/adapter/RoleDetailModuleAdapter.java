package com.rainwood.oa.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.SubRoleXModule;
import com.rainwood.oa.ui.widget.MeasureListView;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/21 15:29
 * @Desc: 权限详情---角色权限模块adapter ---- 0
 */
public final class RoleDetailModuleAdapter extends RecyclerView.Adapter<RoleDetailModuleAdapter.ViewHolder> {

    private List<SubRoleXModule> mPermissionList;

    public void setPermissionList(List<SubRoleXModule> permissionList) {
        mPermissionList = permissionList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_role_permission_detail_module, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.moduleName.setText(mPermissionList.get(position).getModuleX());
        holder.selected.setImageResource(mPermissionList.get(position).isHasSelected() ? R.drawable.ic_down_arrow : R.drawable.ic_up_arrow);
        // 选模块 -- 点击折叠
        holder.selectedModule.setOnClickListener(v -> {
            mPermissionList.get(position).setHasSelected(!mPermissionList.get(position).isHasSelected());
            notifyItemChanged(position);
        });
        // 子项-- 所有权限列表
        RolePermissionAdapter permissionAdapter = new RolePermissionAdapter();
        holder.permissionList.setAdapter(permissionAdapter);
        permissionAdapter.setPermissionList(mPermissionList.get(position).getPermissions());
        // 子项隐藏
        if (!mPermissionList.get(position).isHasSelected()){   // 子项的隐藏显示
            holder.permissionList.setVisibility(View.GONE);
        }else {
            holder.permissionList.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return ListUtils.getSize(mPermissionList);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @ViewInject(R.id.rl_module)
        private RelativeLayout selectedModule;
        @ViewInject(R.id.tv_module_name)
        private TextView moduleName;
        @ViewInject(R.id.iv_selected)
        private ImageView selected;
        @ViewInject(R.id.mlv_permission_list)
        private MeasureListView permissionList;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ViewBind.inject(this, itemView);
        }
    }
}
