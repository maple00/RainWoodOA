package com.rainwood.oa.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.AdminOverTime;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/26 9:17
 * @Desc: 加班记录adapter
 */
public final class AdminOvertimeAdapter extends RecyclerView.Adapter<AdminOvertimeAdapter.ViewHolder> {

    private List<AdminOverTime> mOvertimeRecordList;
    private Context mContext;

    public void setOvertimeRecordList(List<AdminOverTime> overtimeRecordList) {
        mOvertimeRecordList = overtimeRecordList;
        notifyDataSetChanged();
    }

    /**
     * 追加一些数据
     */
    public void addData(List<AdminOverTime> data) {
        if (data == null || data.size() == 0) {
            return;
        }

        if (mOvertimeRecordList == null || mOvertimeRecordList.size() == 0) {
            setOvertimeRecordList(data);
        } else {
            mOvertimeRecordList.addAll(data);
            notifyItemRangeInserted(mOvertimeRecordList.size() - data.size(), data.size());
        }
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
        holder.name.setText(mOvertimeRecordList.get(position).getStaffName());
        holder.status.setText(mOvertimeRecordList.get(position).getWorkFlow());
        holder.status.setTextColor(("已通过".equals(mOvertimeRecordList.get(position).getWorkFlow())
                || "成果确认".equals(mOvertimeRecordList.get(position).getWorkFlow()))
                ? mContext.getColor(R.color.colorPrimary) : mContext.getColor(R.color.tipsColor));
        holder.preTime.setText(TextUtils.isEmpty(mOvertimeRecordList.get(position).getTime())
                ? "预计时间：" + mOvertimeRecordList.get(position).getExpectTime()
                : "实际时间：" + mOvertimeRecordList.get(position).getTime());
        holder.content.setText(mOvertimeRecordList.get(position).getText());
        // 点击事件
        holder.itemOvertime.setOnClickListener(v -> mOvertimeListener.onClickOvertime(mOvertimeRecordList.get(position)));
    }

    @Override
    public int getItemCount() {
        return ListUtils.getSize(mOvertimeRecordList);
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

    public interface OnClickItemOvertimeListener {
        /**
         * 查看详情
         *
         * @param overtimeRecord
         */
        void onClickOvertime(AdminOverTime overtimeRecord);
    }

    private OnClickItemOvertimeListener mOvertimeListener;

    public void setItemOvertime(OnClickItemOvertimeListener itemOvertime) {
        mOvertimeListener = itemOvertime;
    }
}
