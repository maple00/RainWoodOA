package com.rainwood.oa.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.RolePermission;
import com.rainwood.oa.model.domain.SubRoleXModule;
import com.rainwood.oa.ui.widget.TextFlowLayout;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/21 13:23
 * @Desc: 角色管理
 */
public final class RoleManagerAdapter extends RecyclerView.Adapter<RoleManagerAdapter.ViewHolder> {

    private List<RolePermission> mPermissionList;

    public void setPermissionList(List<RolePermission> permissionList) {
        mPermissionList = permissionList;
        notifyDataSetChanged();
    }

    public void addData(List<RolePermission> data) {
        if (data == null || data.size() == 0) {
            return;
        }

        if (mPermissionList == null || mPermissionList.size() == 0) {
            setPermissionList(data);
        } else {
            mPermissionList.addAll(data);
            notifyItemRangeInserted(mPermissionList.size() - data.size(), data.size());
        }
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_role_permission_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.roleName.setText(mPermissionList.get(position).getName());
        holder.roleDesc.setText(mPermissionList.get(position).getText());
        // 流式布局
        List<String> xRoleName = new ArrayList<>();
        for (SubRoleXModule roleXModule : mPermissionList.get(position).getPower()) {
            xRoleName.add(roleXModule.getName());
        }
        holder.mTextFlowLayout.setTextList(xRoleName);
        // 点击事件
        holder.roleItem.setOnClickListener(v -> mClickRoleItem.onClick(mPermissionList.get(position)));
        holder.mTextFlowLayout.setOnFlowTextItemClickListener(text -> mClickRoleItem.onClick(mPermissionList.get(position)));
    }

    @Override
    public int getItemCount() {
        return ListUtils.getSize(mPermissionList);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @ViewInject(R.id.ll_role_item)
        private LinearLayout roleItem;
        @ViewInject(R.id.tv_role)
        private TextView roleName;
        @ViewInject(R.id.tv_role_desc)
        private TextView roleDesc;
        @ViewInject(R.id.tfl_text)
        private TextFlowLayout mTextFlowLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ViewBind.inject(this, itemView);
        }
    }

    public interface OnClickRoleItem {
        /**
         * 查看角色详情
         *
         * @param rolePermission
         */
        void onClick(RolePermission rolePermission);
    }

    private OnClickRoleItem mClickRoleItem;

    public void setClickRoleItem(OnClickRoleItem clickRoleItem) {
        mClickRoleItem = clickRoleItem;
    }
}
