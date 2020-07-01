package com.rainwood.oa.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.LeaveRecord;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/27 9:05
 * @Desc: 请假记录
 */
public final class LeaveAdapter extends RecyclerView.Adapter<LeaveAdapter.ViewHolder> {

    private List<LeaveRecord> mLeaveList = new ArrayList<>();
    private Context mContext;
    private boolean loaded = false;

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public void setLeaveList(List<LeaveRecord> leaveList) {
        if (loaded) {
            mLeaveList.clear();
        }
        mLeaveList.addAll(leaveList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_leave_record, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(mLeaveList.get(position).getStaffName());
        holder.content.setText(mLeaveList.get(position).getText());
        holder.time.setText(mLeaveList.get(position).getTime());
        holder.status.setText(mLeaveList.get(position).getWorkFlow());
        holder.status.setTextColor("已通过".equals(mLeaveList.get(position).getWorkFlow())
                ? mContext.getColor(R.color.colorPrimary) : mContext.getColor(R.color.tipsColor));
        // 点击事件
        holder.itemLeave.setOnClickListener(v -> mClickItemLeave.onClickLeave(mLeaveList.get(position)));
    }

    @Override
    public int getItemCount() {
        return ListUtils.getSize(mLeaveList);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @ViewInject(R.id.ll_item_leave)
        private LinearLayout itemLeave;
        @ViewInject(R.id.tv_name)
        private TextView name;
        @ViewInject(R.id.tv_status)
        private TextView status;
        @ViewInject(R.id.tv_content)
        private TextView content;
        @ViewInject(R.id.tv_time)
        private TextView time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ViewBind.inject(this, itemView);
        }
    }

    public interface OnClickItemLeave {
        /**
         * 查看详情
         *
         * @param leaveRecord
         */
        void onClickLeave(LeaveRecord leaveRecord);
    }

    private OnClickItemLeave mClickItemLeave;

    public void setClickItemLeave(OnClickItemLeave clickItemLeave) {
        mClickItemLeave = clickItemLeave;
    }
}
