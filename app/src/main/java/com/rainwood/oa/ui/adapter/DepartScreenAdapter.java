package com.rainwood.oa.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.Depart;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/22 11:12
 * @Desc: 所属部门条件筛选
 */
public final class DepartScreenAdapter extends RecyclerView.Adapter<DepartScreenAdapter.ViewHolder> {

    private Context mContext;
    private List<Depart> mList;

    public void setList(List<Depart> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_pop_role_condition, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.moduleName.setText(TextUtils.isEmpty(mList.get(position).getName()) ? "" : mList.get(position).getName());
        if (mList.get(position).isHasSelected()) {
            holder.arrow.setVisibility(View.VISIBLE);
            holder.moduleName.setTextColor(mContext.getColor(R.color.colorPrimary));
        } else {
            holder.arrow.setVisibility(View.GONE);
            holder.moduleName.setTextColor(mContext.getColor(R.color.fontColor));
        }

        // 点击事件
        holder.itemCondition.setOnClickListener(v -> {
            mRoleCondition.onClickItem(mList.get(position), position);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return ListUtils.getSize(mList);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @ViewInject(R.id.ll_item_condition)
        private LinearLayout itemCondition;
        @ViewInject(R.id.tv_name)
        private TextView moduleName;
        @ViewInject(R.id.iv_arrow)
        private ImageView arrow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ViewBind.inject(this, itemView);
        }
    }

    public interface OnClickItemDepartCondition {
        /**
         * 点击item
         *
         * @param condition
         * @param position
         */
        void onClickItem(Depart condition, int position);
    }

    private OnClickItemDepartCondition mRoleCondition;

    public void setRoleCondition(OnClickItemDepartCondition roleCondition) {
        mRoleCondition = roleCondition;
    }
}
