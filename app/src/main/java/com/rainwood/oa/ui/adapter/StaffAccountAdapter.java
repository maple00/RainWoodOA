package com.rainwood.oa.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.StaffAccount;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/25 17:30
 * @Desc: 员工会计账户adapter
 */
public final class StaffAccountAdapter extends RecyclerView.Adapter<StaffAccountAdapter.ViewHolder> {

    private List<StaffAccount> mAccountList;
    private Context mContext;

    public void setAccountList(List<StaffAccount> accountList) {
        mAccountList = accountList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_staff_account, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.event.setText(TextUtils.isEmpty(mAccountList.get(position).getTitle()) ? "" : mAccountList.get(position).getTitle());
        holder.time.setText(mAccountList.get(position).getTime());
        holder.money.setText(mAccountList.get(position).getMoney());
        holder.money.setTextColor(mAccountList.get(position).getMoney().startsWith("+")
                ? mContext.getColor(R.color.colorPrimary)
                : mContext.getColor(R.color.fontColor));
        // 点击事件
        holder.itemAccount.setOnClickListener(v -> mItemAccount.onClickAccount(mAccountList.get(position)));
    }

    @Override
    public int getItemCount() {
        return ListUtils.getSize(mAccountList);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @ViewInject(R.id.rl_item_account)
        private RelativeLayout itemAccount;
        @ViewInject(R.id.tv_money)
        private TextView money;
        @ViewInject(R.id.tv_event)
        private TextView event;
        @ViewInject(R.id.tv_time)
        private TextView time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ViewBind.inject(this, itemView);
        }
    }

    public interface OnClickItemAccount{
        /**
         * 查看详情
         * @param account
         */
        void onClickAccount(StaffAccount account);
    }

    private OnClickItemAccount mItemAccount;

    public void setItemAccount(OnClickItemAccount itemAccount) {
        mItemAccount = itemAccount;
    }
}