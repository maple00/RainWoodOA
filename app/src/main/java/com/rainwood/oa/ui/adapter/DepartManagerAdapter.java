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
import com.rainwood.oa.model.domain.Depart;
import com.rainwood.oa.ui.widget.MeasureListView;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/21 17:10
 * @Desc: 部门管理adapter
 */
public final class DepartManagerAdapter extends RecyclerView.Adapter<DepartManagerAdapter.ViewHolder> {

    private List<Depart> mDepartList = new ArrayList<>();

    public void setDepartList(List<Depart> departList) {
        mDepartList.clear();
        mDepartList.addAll(departList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_depart_manager, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.depart.setText(mDepartList.get(position).getName());
        holder.selectedImg.setImageResource(mDepartList.get(position).isHasSelected() ? R.drawable.ic_down_arrow : R.drawable.ic_up_arrow);
        // 设置隐藏
        holder.managerTop.setOnClickListener(v -> {
            mDepartList.get(position).setHasSelected(!mDepartList.get(position).isHasSelected());
            notifyItemChanged(position);
        });
        // 项目组
        ProjectGroupsAdapter groupsAdapter = new ProjectGroupsAdapter();
        holder.groups.setAdapter(groupsAdapter);
        groupsAdapter.setGroupList(mDepartList.get(position).getArray());
        groupsAdapter.setClickGroup(position, mOnClickGroup);
        // 设置隐藏
        if (!mDepartList.get(position).isHasSelected()) {
            holder.groups.setVisibility(View.GONE);
        } else {
            holder.groups.setVisibility(View.VISIBLE);
        }
    }

    private ProjectGroupsAdapter.OnClickGroup mOnClickGroup;

    public void setOnClickGroup(ProjectGroupsAdapter.OnClickGroup onClickGroup) {
        mOnClickGroup = onClickGroup;
    }

    @Override
    public int getItemCount() {
        return ListUtils.getSize(mDepartList);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @ViewInject(R.id.rl_manager_top)
        private RelativeLayout managerTop;
        @ViewInject(R.id.tv_title)
        private TextView depart;
        @ViewInject(R.id.iv_img)
        private ImageView selectedImg;
        @ViewInject(R.id.mlv_groups)
        private MeasureListView groups;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ViewBind.inject(this, itemView);
        }
    }
}
