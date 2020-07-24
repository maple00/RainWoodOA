package com.rainwood.oa.ui.adapter;

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
import com.rainwood.oa.model.domain.MineRecords;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/12 11:22
 * @Desc: 我的补卡记录
 */
public final class MineRecordsAdapter extends RecyclerView.Adapter<MineRecordsAdapter.ViewHolder> {

    private Context mContext;
    private List<MineRecords> mReissueList;

    public void setReissueList(List<MineRecords> reissueList) {
        mReissueList = reissueList;
        notifyDataSetChanged();
    }

    /**
     * 追加一些数据
     */
    public void addData(List<MineRecords> data) {
        if (data == null || data.size() == 0) {
            return;
        }

        if (mReissueList == null || mReissueList.size() == 0) {
            setReissueList(data);
        } else {
            mReissueList.addAll(data);
            notifyItemRangeInserted(mReissueList.size() - data.size(), data.size());
        }
        notifyDataSetChanged();
    }

    public void removeItem(int position){
        mReissueList.remove(position);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_mine_reissue, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.state.setText(mReissueList.get(position).getWorkFlow());
        holder.time.setText(TextUtils.isEmpty(mReissueList.get(position).getSignTime())
                ? mReissueList.get(position).getTime()
                : mReissueList.get(position).getSignTime());
        holder.content.setText(mReissueList.get(position).getText());
        holder.state.setTextColor("已通过".equals(mReissueList.get(position).getWorkFlow())
                ? mContext.getColor(R.color.colorPrimary) : mContext.getColor(R.color.tipsColor));
        holder.edit.setVisibility("草稿".equals(mReissueList.get(position).getWorkFlow()) ? View.VISIBLE : View.GONE);
        holder.delete.setVisibility("草稿".equals(mReissueList.get(position).getWorkFlow()) ? View.VISIBLE : View.GONE);
        // 点击事件
        holder.itemReissue.setOnClickListener(v -> mItemReissue.onClickItem(mReissueList.get(position), position));
        holder.edit.setOnClickListener(v -> mItemReissue.onClickEdit(mReissueList.get(position), position));
        holder.delete.setOnClickListener(v -> mItemReissue.onClickDelete(mReissueList.get(position), position));
    }

    @Override
    public int getItemCount() {
        return ListUtils.getSize(mReissueList);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @ViewInject(R.id.ll_item_reissue)
        private LinearLayout itemReissue;
        @ViewInject(R.id.tv_name)
        private TextView name;
        @ViewInject(R.id.tv_state)
        private TextView state;
        @ViewInject(R.id.tv_time)
        private TextView time;
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

    public interface OnClickItemReissue {
        /**
         * 查看详情
         *
         * @param reissue
         * @param position
         */
        void onClickItem(MineRecords reissue, int position);

        /**
         * 编辑
         *
         * @param reissue
         * @param position
         */
        void onClickEdit(MineRecords reissue, int position);

        /**
         * 删除
         *
         * @param reissue
         * @param position
         */
        void onClickDelete(MineRecords reissue, int position);
    }

    public OnClickItemReissue mItemReissue;

    public void setItemReissue(OnClickItemReissue itemReissue) {
        mItemReissue = itemReissue;
    }
}
