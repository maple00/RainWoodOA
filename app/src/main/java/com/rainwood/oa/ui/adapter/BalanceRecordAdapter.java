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
import com.rainwood.oa.model.domain.AdminOverTime;
import com.rainwood.oa.model.domain.BalanceRecord;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/28 10:49
 * @Desc: 收支记录adapter
 */
public final class BalanceRecordAdapter extends RecyclerView.Adapter<BalanceRecordAdapter.ViewHolder> {

    private Context mContext;
    private List<BalanceRecord> mRecordList;

    public void setRecordList(List<BalanceRecord> recordList) {
        mRecordList = recordList;
        notifyDataSetChanged();
    }
    /**
     * 追加一些数据
     */
    public void addData(List<BalanceRecord> data) {
        if (data == null || data.size() == 0) {
            return;
        }

        if (mRecordList == null || mRecordList.size() == 0) {
            setRecordList(data);
        } else {
            mRecordList.addAll(data);
            notifyItemRangeInserted(mRecordList.size() - data.size(), data.size());
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_balance_record, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mTextName.setText(mRecordList.get(position).getTarget());
        holder.voucher.setVisibility("是".equals(mRecordList.get(position).getIco()) ? View.VISIBLE : View.GONE);
        holder.type.setText(mRecordList.get(position).getType());
        holder.content.setText(mRecordList.get(position).getText());
        holder.time.setText(mRecordList.get(position).getPayDate());
        holder.money.setText("支出".equals(mRecordList.get(position).getDirection())
                ? "-" + mRecordList.get(position).getMoney() : "+" + mRecordList.get(position).getMoney());
        holder.money.setTextColor("支出".equals(mRecordList.get(position).getDirection())
                ? mContext.getColor(R.color.fontColor) : mContext.getColor(R.color.colorPrimary));
        // 点击事件
        holder.itemBalance.setOnClickListener(v -> mOnClickItemListener.onClickItem(mRecordList.get(position), position));
    }

    @Override
    public int getItemCount() {
        return ListUtils.getSize(mRecordList);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @ViewInject(R.id.ll_item_balance)
        private LinearLayout itemBalance;
        @ViewInject(R.id.tv_name)
        private TextView mTextName;
        @ViewInject(R.id.iv_voucher)
        private ImageView voucher;
        @ViewInject(R.id.tv_money)
        private TextView money;
        @ViewInject(R.id.tv_type)
        private TextView type;
        @ViewInject(R.id.tv_content)
        private TextView content;
        @ViewInject(R.id.tv_time)
        private TextView time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ViewBind.inject(this, itemView);
        }
    }

    public interface OnClickItemListener {
        /**
         * 查看详情
         *
         * @param balanceRecord
         * @param position
         */
        void onClickItem(BalanceRecord balanceRecord, int position);
    }

    private OnClickItemListener mOnClickItemListener;

    public void setOnClickItemListener(OnClickItemListener onClickItemListener) {
        mOnClickItemListener = onClickItemListener;
    }
}
