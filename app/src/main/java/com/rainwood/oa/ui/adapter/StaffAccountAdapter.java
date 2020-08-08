package com.rainwood.oa.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
        notifyDataSetChanged();
    }

    /**
     * 追加一些数据
     *
     * @param data
     */
    public void addData(List<StaffAccount> data) {
        if (data == null || data.size() == 0) {
            return;
        }
        if (mAccountList == null || mAccountList.size() == 0) {
            setAccountList(data);
        } else {
            mAccountList.addAll(data);
            notifyItemRangeInserted(mAccountList.size() - data.size(), data.size());
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_staff_account, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.event.setText(TextUtils.isEmpty(mAccountList.get(position).getText()) ? "" : mAccountList.get(position).getText());
        holder.time.setText(mAccountList.get(position).getTime());
        holder.money.setText("收入".equals(mAccountList.get(position).getDirection())
                ? "金额：+" + mAccountList.get(position).getMoney()
                : "金额：-" + mAccountList.get(position).getMoney());
        holder.money.setTextColor("收入".equals(mAccountList.get(position).getDirection())
                ? mContext.getColor(R.color.colorPrimary)
                : mContext.getColor(R.color.red05));
        // TODO: 文字+图片---图片紧跟文字之后
        holder.voucher.setVisibility((mAccountList.get(position).getIco().endsWith("jpg")
                || mAccountList.get(position).getIco().endsWith("jpg"))
                ? View.VISIBLE : View.GONE);
        // 结余
        holder.mTextBalance.setText("余额：￥" + mAccountList.get(position).getBalance());

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
        @ViewInject(R.id.tv_balance)
        private TextView mTextBalance;
        @ViewInject(R.id.iv_voucher)
        private ImageView voucher;
        @ViewInject(R.id.tv_event)
        private TextView event;
        @ViewInject(R.id.tv_time)
        private TextView time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ViewBind.inject(this, itemView);
        }
    }

    public interface OnClickItemAccount {
        /**
         * 查看详情
         *
         * @param account
         */
        void onClickAccount(StaffAccount account);
    }

    private OnClickItemAccount mItemAccount;

    public void setItemAccount(OnClickItemAccount itemAccount) {
        mItemAccount = itemAccount;
    }
}
