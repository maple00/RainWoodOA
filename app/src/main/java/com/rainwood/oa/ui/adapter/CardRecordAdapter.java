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
import com.rainwood.oa.model.domain.CardRecord;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/26 9:17
 * @Desc: 补卡记录adapter
 */
public final class CardRecordAdapter extends RecyclerView.Adapter<CardRecordAdapter.ViewHolder> {

    private List<CardRecord> mCardRecordList;

    public void setLeaveOutRecordList(List<CardRecord> cardRecordList) {
        mCardRecordList = cardRecordList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_record, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(mCardRecordList.get(position).getStaffName());
        holder.status.setText(mCardRecordList.get(position).getWorkFlow());
        holder.preTime.setText(mCardRecordList.get(position).getSignTime());
        holder.content.setText(mCardRecordList.get(position).getText());
        // 点击事件
        holder.itemCardRecord.setOnClickListener(v -> mCardRecordListener.onClickGoOut(mCardRecordList.get(position)));
    }

    @Override
    public int getItemCount() {
        return ListUtils.getSize(mCardRecordList);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @ViewInject(R.id.ll_item_card_record)
        private LinearLayout itemCardRecord;
        @ViewInject(R.id.tv_name)
        private TextView name;
        @ViewInject(R.id.tv_status)
        private TextView status;
        @ViewInject(R.id.tv_pre_time)
        private TextView preTime;
        @ViewInject(R.id.tv_content)
        private TextView content;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ViewBind.inject(this, itemView);
        }
    }

    public interface OnClickItemGoOutListener {
        /**
         * 查看详情
         *
         * @param cardRecord
         */
        void onClickGoOut(CardRecord cardRecord);
    }

    private OnClickItemGoOutListener mCardRecordListener;

    public void setItemGoOut(OnClickItemGoOutListener itemCardRecord) {
        mCardRecordListener = itemCardRecord;
    }
}
