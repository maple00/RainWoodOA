package com.rainwood.oa.ui.adapter;

import android.text.TextUtils;
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
import com.rainwood.oa.utils.LogUtils;
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
        notifyDataSetChanged();
    }

    /**
     * 追加
     *
     * @param loadList
     */
    public void addData(List<Custom> loadList) {
        //添加之前拿到原来的size
        int olderSize = ListUtils.getSize(mList);
        mList.addAll(loadList);
        // 局部更新
        notifyItemChanged(olderSize, ListUtils.getSize(loadList));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_custom_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.companyName.setText(mList.get(position).getCompanyName());
        holder.status.setText(TextUtils.isEmpty(mList.get(position).getWorkFlow()) ? "" : mList.get(position).getWorkFlow());
        // 负责人 -- 自定义组合控件
        holder.customHead.setValue(TextUtils.isEmpty(mList.get(position).getStaff()) ? "暂无负责人" : mList.get(position).getStaff());
        // 协作人
        holder.customOrigin.setValue(TextUtils.isEmpty(mList.get(position).getEdit()) ? "暂无协作人" : mList.get(position).getEdit());
        // 联系人
        holder.customTrade.setValue(TextUtils.isEmpty(mList.get(position).getContactName()) ? "暂无联系人" : mList.get(position).getContactName());
        // 联系人电话
        holder.customBudget.setValue(TextUtils.isEmpty(mList.get(position).getContactTel()) ? "暂无联系人电话" : mList.get(position).getContactTel());
        holder.demand.setText(TextUtils.isEmpty(mList.get(position).getText()) ? "暂无需求" : mList.get(position).getText());
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
        @ViewInject(R.id.tv_company_name)
        private TextView companyName;
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
