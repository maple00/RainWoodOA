package com.rainwood.oa.ui.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.LeaveOutRecord;
import com.rainwood.oa.model.domain.ReceivableRecord;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/29 18:04
 * @Desc: 回款记录adapter
 */
public final class ReceivableRecordAdapter extends RecyclerView.Adapter<ReceivableRecordAdapter.ViewHolder> {

    private List<ReceivableRecord> mRecordList;

    public void setRecordList(List<ReceivableRecord> recordList) {
        mRecordList = recordList;
        notifyDataSetChanged();
    }

    public void addData(List<ReceivableRecord> data) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_receivable_record, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.receivableMoney.setText("￥" + mRecordList.get(position).getMoney());
        holder.tips.setText(mRecordList.get(position).getText());
        holder.time.setText(mRecordList.get(position).getPayDate());
        // 点击事件
        holder.itemReceivable.setOnClickListener(v -> mClickReceivable.onClickItemDetail(mRecordList.get(position), position));
    }

    @Override
    public int getItemCount() {
        return ListUtils.getSize(mRecordList);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @ViewInject(R.id.ll_item_receivable)
        private LinearLayout itemReceivable;
        @ViewInject(R.id.tv_receivable_money)
        private TextView receivableMoney;
        @ViewInject(R.id.tv_money)
        private TextView money;
        @ViewInject(R.id.tv_tips)
        private TextView tips;
        @ViewInject(R.id.tv_time)
        private TextView time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ViewBind.inject(this, itemView);
        }
    }

    public interface OnClickReceivable {
        /**
         * 查看详情
         *
         * @param record
         * @param position
         */
        void onClickItemDetail(ReceivableRecord record, int position);
    }

    private OnClickReceivable mClickReceivable;

    public void setClickReceivable(OnClickReceivable clickReceivable) {
        mClickReceivable = clickReceivable;
    }
}
