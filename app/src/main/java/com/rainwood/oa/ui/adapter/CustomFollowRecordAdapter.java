package com.rainwood.oa.ui.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.CustomFollowRecord;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/29 13:26
 * @Desc: 客户跟进记录adapter
 */
public final class CustomFollowRecordAdapter extends RecyclerView.Adapter<CustomFollowRecordAdapter.ViewHolder> {

    private List<CustomFollowRecord> mRecordList;

    /**
     * 列表是否显示cb
     */
    private boolean showSelector = false;

    public void setRecordList(List<CustomFollowRecord> recordList) {
        mRecordList = recordList;
        notifyDataSetChanged();
    }

    public void setShowSelector(boolean showSelector) {
        this.showSelector = showSelector;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_custom_follow_record, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.content.setText(mRecordList.get(position).getText());
        holder.nameTime.setText(mRecordList.get(position).getStaffName() + " " + mRecordList.get(position).getTime());
        holder.selector.setVisibility(showSelector ? View.VISIBLE : View.GONE);
        holder.selector.setChecked(mRecordList.get(position).isSelected());
        // 点击事件
        holder.selector.setOnClickListener(v -> {
            // LogUtils.d("sxs", "点击了--------" + position);
            boolean checked = holder.selector.isChecked();
            mClickChecked.onClickCheck(mRecordList.get(position), checked);
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return ListUtils.getSize(mRecordList);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @ViewInject(R.id.rl_item_follow)
        private RelativeLayout itemFollow;
        @ViewInject(R.id.cb_checked)
        private CheckBox selector;
        @ViewInject(R.id.tv_content)
        private TextView content;
        @ViewInject(R.id.tv_name_time)
        private TextView nameTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ViewBind.inject(this, itemView);
        }
    }

    public interface OnClickChecked {

        /**
         * 选中监听
         *
         * @param record
         */
        void onClickCheck(CustomFollowRecord record, boolean selected);
    }

    private OnClickChecked mClickChecked;

    public void setClickChecked(OnClickChecked clickChecked) {
        mClickChecked = clickChecked;
    }
}
