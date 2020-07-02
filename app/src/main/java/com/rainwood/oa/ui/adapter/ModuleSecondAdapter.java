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
import com.rainwood.oa.model.domain.IconAndFont;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/22 11:12
 * @Desc: 模块二级条件筛选（所属小组）
 */
public final class ModuleSecondAdapter extends RecyclerView.Adapter<ModuleSecondAdapter.ViewHolder> {

    private Context mContext;
    private List<IconAndFont> mList;

    public void setList(List<IconAndFont> list) {
        mList = list;
//         mList.clear();
//         mList.addAll(list);
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
        holder.moduleName.setText(mList.get(position).getName());
        holder.arrow.setVisibility(View.GONE);
        holder.moduleName.setTextColor(mList.get(position).isSelected()
                ? mContext.getColor(R.color.colorPrimary) :
                mContext.getColor(R.color.fontColor));
        // 点击事件
        holder.itemCondition.setOnClickListener(v -> {
            mDepartGroupCondition.onClickSecondItem(mList.get(position), position);
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

    public interface OnClickSecondRoleCondition {
        /**
         * 点击次级module
         *
         * @param secondCondition
         * @param position
         */
        void onClickSecondItem(IconAndFont secondCondition, int position);
    }

    private OnClickSecondRoleCondition mDepartGroupCondition;

    public void setSecondRoleCondition(OnClickSecondRoleCondition secondRoleCondition) {
        mDepartGroupCondition = secondRoleCondition;
    }
}
