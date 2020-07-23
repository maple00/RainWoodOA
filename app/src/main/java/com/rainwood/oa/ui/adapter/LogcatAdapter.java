package com.rainwood.oa.ui.adapter;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.Logcat;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/28 9:53
 * @Desc: 系统日志adapter
 */
public final class LogcatAdapter extends RecyclerView.Adapter<LogcatAdapter.ViewHolder> {

    private List<Logcat> mLogcatList;

    public void setLogcatList(List<Logcat> logcatList) {
        mLogcatList = logcatList;
        notifyDataSetChanged();
    }

    /**
     * 追加一些数据
     */
    public void addData(List<Logcat> data) {
        if (data == null || data.size() == 0) {
            return;
        }

        if (mLogcatList == null || mLogcatList.size() == 0) {
            setLogcatList(data);
        } else {
            mLogcatList.addAll(data);
            notifyItemRangeInserted(mLogcatList.size() - data.size(), data.size());
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_system_logcat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(mLogcatList.get(position).getStaffName());
        holder.logcat.setText(Html.fromHtml(mLogcatList.get(position).getText()));
        holder.origin.setText(mLogcatList.get(position).getType());
        holder.time.setText(mLogcatList.get(position).getTime());
        // 点击事件
        holder.itemLogcat.setOnClickListener(v -> mClickLogcat.onClickLogcat(mLogcatList.get(position), position));
    }

    @Override
    public int getItemCount() {
        return ListUtils.getSize(mLogcatList);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @ViewInject(R.id.ll_item_logcat)
        private LinearLayout itemLogcat;
        @ViewInject(R.id.tv_name)
        private TextView name;
        @ViewInject(R.id.tv_logcat)
        private TextView logcat;
        @ViewInject(R.id.tv_origin)
        private TextView origin;
        @ViewInject(R.id.tv_time)
        private TextView time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ViewBind.inject(this, itemView);
        }
    }

    public interface OnClickLogcat {
        /**
         * 查看详情
         *
         * @param logcat
         * @param position
         */
        void onClickLogcat(Logcat logcat, int position);
    }

    private OnClickLogcat mClickLogcat;

    public void setClickLogcat(OnClickLogcat clickLogcat) {
        mClickLogcat = clickLogcat;
    }
}
