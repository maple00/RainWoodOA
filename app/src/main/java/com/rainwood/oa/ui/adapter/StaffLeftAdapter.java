package com.rainwood.oa.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.StaffDepart;
import com.rainwood.oa.ui.widget.MeasureListView;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/22 10:52
 * @Desc: 员工管理  左边部门-职位adapter
 */
public final class StaffLeftAdapter extends RecyclerView.Adapter<StaffLeftAdapter.ViewHolder> {

    private List<StaffDepart> mDepartList;
    private Context mContext;
    // 再次点击临时变量 --- 默认展开
    private boolean tempSelected = true;

    public void setDepartList(List<StaffDepart> departList) {
        mDepartList = departList;
        notifyDataSetChanged();
    }

    public void setTempSelected(boolean tempSelected) {
        this.tempSelected = tempSelected;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_staff_depart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.depart.setText(mDepartList.get(position).getName());
        // 如果被选中-- 临时变量记录
        if (mDepartList.get(position).isSelected()) {
            holder.selectedFlag.setVisibility(View.VISIBLE);
            holder.selected.setVisibility(View.VISIBLE);
            holder.depart.setTextColor(mContext.getColor(R.color.colorPrimary));
            if (!tempSelected) {
                // 如果是再点一次-- 折叠当前
                holder.selected.setImageResource(R.drawable.ic_up_arrow_green);
            } else {
                // 展开当前
                holder.selected.setImageResource(R.drawable.ic_down_arrow_green);
            }
            tempSelected = !tempSelected;
        } else {
            // 其他部门
            holder.depart.setTextColor(mContext.getColor(R.color.fontColor));
            holder.selectedFlag.setVisibility(View.GONE);
            holder.selected.setVisibility(View.GONE);
        }
        // 子项
        StaffPostAdapter postAdapter = new StaffPostAdapter();
        holder.postListView.setAdapter(postAdapter);
        postAdapter.setClickPost(position, mPostClick);
        postAdapter.setPostList(mDepartList.get(position).getArray());
        // 折叠、展开
        holder.postListView.setVisibility((mDepartList.get(position).isSelected() && !tempSelected)
                ? View.VISIBLE : View.GONE);
        // 点击事件
        holder.itemstaff.setOnClickListener(v -> {
            mstaffDepart.onClickDepart(position);
            notifyDataSetChanged();
        });
    }

    private StaffPostAdapter.OnClickPost mPostClick;

    public void setPostClick(StaffPostAdapter.OnClickPost postClick) {
        mPostClick = postClick;
    }

    @Override
    public int getItemCount() {
        return ListUtils.getSize(mDepartList);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @ViewInject(R.id.ll_item_staff)
        private LinearLayout itemstaff;
        @ViewInject(R.id.selected_flag)
        private TextView selectedFlag;
        @ViewInject(R.id.tv_depart)
        private TextView depart;
        @ViewInject(R.id.iv_selected)
        private ImageView selected;
        @ViewInject(R.id.mlv_post)
        private MeasureListView postListView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ViewBind.inject(this, itemView);
        }
    }

    public interface OnClickstaffDepart {
        /**
         * 点击某一个item
         *
         * @param position
         */
        void onClickDepart(int position);
    }

    private OnClickstaffDepart mstaffDepart;

    public void setstaffDepart(OnClickstaffDepart staffDepart) {
        mstaffDepart = staffDepart;
    }
}
