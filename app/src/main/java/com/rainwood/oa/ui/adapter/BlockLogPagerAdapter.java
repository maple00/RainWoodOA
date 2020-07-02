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
import com.rainwood.oa.model.domain.BlockLog;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/17 16:52
 * @Desc: 待办事项pager Adapter
 */
public final class BlockLogPagerAdapter extends RecyclerView.Adapter<BlockLogPagerAdapter.ViewHolder> {

    private List<BlockLog> mBlockLogList = new ArrayList<>();
    private Context mContext;

    public void setBlockLogList(List<BlockLog> blockLogList) {
        mBlockLogList.clear();
        mBlockLogList.addAll(blockLogList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_block_log_pager, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.content.setText(mBlockLogList.get(position).getText());
        holder.time.setText(mBlockLogList.get(position).getTime());
        if ("已完成".equals(mBlockLogList.get(position).getWorkFlow())) {
            holder.content.setTextColor(mContext.getColor(R.color.labelColor));
            holder.time.setTextColor(mContext.getColor(R.color.labelColor));
        } else {
            holder.content.setTextColor(mContext.getColor(R.color.fontColor));
            holder.time.setTextColor(mContext.getColor(R.color.fontColor));
        }
        // 点击事件
        holder.itemBlock.setOnClickListener(v -> mBlockListener.onClickItem(mBlockLogList.get(position), position));
    }

    @Override
    public int getItemCount() {
        return ListUtils.getSize(mBlockLogList);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @ViewInject(R.id.ll_item_block_log)
        private LinearLayout itemBlock;
        @ViewInject(R.id.tv_content)
        private TextView content;
        @ViewInject(R.id.tv_time)
        private TextView time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ViewBind.inject(this, itemView);
        }
    }

    public interface OnClickItemBlockListener {
        /**
         * 查看详情
         *
         * @param blockLog
         * @param position
         */
        void onClickItem(BlockLog blockLog, int position);
    }

    private OnClickItemBlockListener mBlockListener;

    public void setBlockListener(OnClickItemBlockListener blockListener) {
        mBlockListener = blockListener;
    }
}
