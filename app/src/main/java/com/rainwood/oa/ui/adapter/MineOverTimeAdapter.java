package com.rainwood.oa.ui.adapter;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.MineRecordTime;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/12 16:19
 * @Desc: 我的加班记录
 */
public final class MineOverTimeAdapter extends RecyclerView.Adapter<MineOverTimeAdapter.ViewHolder> {

    private List<MineRecordTime> mList;
    private Context mContext;

    public void setList(List<MineRecordTime> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_mine_over_time, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.type.setText(TextUtils.isEmpty(mList.get(position).getTime())
                ? "预计加班：" : "实际加班：");
        holder.time.setText(TextUtils.isEmpty(mList.get(position).getTime())
                ? mList.get(position).getExpectTime() : mList.get(position).getTime());
        holder.state.setText(mList.get(position).getWorkFlow());
        holder.state.setTextColor("已通过".equals(mList.get(position).getWorkFlow())
                || "成果确认".equals(mList.get(position).getWorkFlow())
                ? mContext.getColor(R.color.colorPrimary) : mContext.getColor(R.color.tipsColor));
        holder.content.setText(Html.fromHtml(mList.get(position).getText()));
        holder.delete.setVisibility("草稿".equals(mList.get(position).getWorkFlow()) ? View.VISIBLE : View.GONE);
        if ("草稿".equals(mList.get(position).getWorkFlow())) {
            holder.edit.setVisibility(View.VISIBLE);
        } else {
            if ("已通过".equals(mList.get(position).getWorkFlow())) {
                holder.edit.setVisibility(View.VISIBLE);
                holder.edit.setText("提交成果");
            } else {
                holder.edit.setVisibility(View.GONE);
            }
        }
        // 点击事件
        holder.itemOverTime.setOnClickListener(v -> mOnClickOverTime.onClickItem(mList.get(position), position));
        holder.delete.setOnClickListener(v -> mOnClickOverTime.onClickDelete(mList.get(position), position));
        holder.edit.setOnClickListener(v -> mOnClickOverTime.onClickEdit(mList.get(position), position, holder.edit.getText().toString().trim()));
    }

    @Override
    public int getItemCount() {
        return ListUtils.getSize(mList);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @ViewInject(R.id.ll_item_over_time)
        private LinearLayout itemOverTime;
        @ViewInject(R.id.tv_type)
        private TextView type;
        @ViewInject(R.id.tv_time)
        private TextView time;
        @ViewInject(R.id.tv_state)
        private TextView state;
        @ViewInject(R.id.tv_content)
        private TextView content;
        @ViewInject(R.id.tv_delete)
        private TextView delete;
        @ViewInject(R.id.tv_edit)
        private TextView edit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ViewBind.inject(this, itemView);
        }
    }

    public interface OnClickOverTime {
        /**
         * 查看详情
         *
         * @param record
         * @param position
         */
        void onClickItem(MineRecordTime record, int position);

        /**
         * 删除
         *
         * @param record
         * @param position
         */
        void onClickDelete(MineRecordTime record, int position);

        /**
         * 编辑或者提交成果
         *
         * @param record
         * @param position
         */
        void onClickEdit(MineRecordTime record, int position, String clickContent);
    }

    private OnClickOverTime mOnClickOverTime;

    public void setOnClickOverTime(OnClickOverTime onClickOverTime) {
        mOnClickOverTime = onClickOverTime;
    }
}
