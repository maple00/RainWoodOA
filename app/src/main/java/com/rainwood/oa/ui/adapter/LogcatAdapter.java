package com.rainwood.oa.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.SystemLogcat;
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

    private List<SystemLogcat> mLogcatList;

    public void setLogcatList(List<SystemLogcat> logcatList) {
        mLogcatList = logcatList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_system_logcat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(mLogcatList.get(position).getName());
        holder.logcat.setText(mLogcatList.get(position).getLogcat());
        holder.origin.setText(mLogcatList.get(position).getOrigin());
        holder.time.setText(mLogcatList.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return ListUtils.getSize(mLogcatList);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

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
}
