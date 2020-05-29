package com.rainwood.oa.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.LeaveOutRecord;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/26 9:17
 * @Desc: 外出记录adapter
 */
public final class LeaveOutAdapter extends RecyclerView.Adapter<LeaveOutAdapter.ViewHolder> {

    private List<LeaveOutRecord> mLeaveOutRecordList;
    private Context mContext;

    public void setLeaveOutRecordList(List<LeaveOutRecord> leaveOutRecordList) {
        mLeaveOutRecordList = leaveOutRecordList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_overtime_record, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(mLeaveOutRecordList.get(position).getStaffName());
        holder.status.setText(mLeaveOutRecordList.get(position).getWorkFlow());
        holder.status.setTextColor(("已通过".equals(mLeaveOutRecordList.get(position).getWorkFlow())
                || "成果确认".equals(mLeaveOutRecordList.get(position).getWorkFlow()))
                ? mContext.getColor(R.color.colorPrimary) : mContext.getColor(R.color.tipsColor));
        holder.preTime.setText(("成果确认".equals(mLeaveOutRecordList.get(position).getWorkFlow())
                ? "实际外出：" : "预计外出：")
                + mLeaveOutRecordList.get(position).getTime());
        holder.content.setText(mLeaveOutRecordList.get(position).getText());
        // 点击事件
        holder.itemOvertime.setOnClickListener(v -> mOvertimeListener.onClickGoOut(mLeaveOutRecordList.get(position)));
    }

    @Override
    public int getItemCount() {
        return ListUtils.getSize(mLeaveOutRecordList);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @ViewInject(R.id.ll_item_overtime)
        private LinearLayout itemOvertime;
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
         * @param leaveOutRecord
         */
        void onClickGoOut(LeaveOutRecord leaveOutRecord);
    }

    private OnClickItemGoOutListener mOvertimeListener;

    public void setItemGoOut(OnClickItemGoOutListener itemOvertime) {
        mOvertimeListener = itemOvertime;
    }
}
