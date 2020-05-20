package com.rainwood.oa.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.Custom;
import com.rainwood.oa.ui.widget.GroupIconText;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/18 13:58
 * @Desc: 客户列表adapter
 */
public final class CustomListAdapter extends RecyclerView.Adapter<CustomListAdapter.ViewHolder> {

    private List<Custom> mList;

    public void setList(List<Custom> list) {
        mList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_custom_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.status.setText(mList.get(position).getStatus());
        holder.itemName.setText(mList.get(position).getCompany());
        // 负责人 -- 自定义组合控件
        holder.customHead.setValue(mList.get(position).getName());
        holder.customOrigin.setValue(mList.get(position).getOrigin());
        holder.customTrade.setValue(mList.get(position).getTrade());
        holder.customBudget.setValue(R.drawable.ic_money_logo + "" + mList.get(position).getBudget());
        holder.demand.setText(mList.get(position).getDemand());
        // 点击事件，-- 查看客户详情
        holder.customItem.setOnClickListener(v -> mItemClickListener.onItemClick(mList.get(position)));
    }

    @Override
    public int getItemCount() {
        return ListUtils.getSize(mList);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @ViewInject(R.id.ll_custom_item)
        private LinearLayout customItem;
        @ViewInject(R.id.tv_status)
        private TextView status;
        @ViewInject(R.id.tv_item_name)
        private TextView itemName;
        @ViewInject(R.id.git_custom_head)
        private GroupIconText customHead;
        @ViewInject(R.id.git_origin)
        private GroupIconText customOrigin;
        @ViewInject(R.id.git_trade)
        private GroupIconText customTrade;
        @ViewInject(R.id.git_budget)
        private GroupIconText customBudget;
        @ViewInject(R.id.tv_demand)
        private TextView demand;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ViewBind.inject(this, itemView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Custom custom);
    }

    private OnItemClickListener mItemClickListener;

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }
}
