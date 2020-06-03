package com.rainwood.oa.ui.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.Helper;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/2 10:39
 * @Desc: 帮助中心adapter
 */
public final class HelperAdapter extends RecyclerView.Adapter<HelperAdapter.ViewHolder> {

    private List<Helper> mHelperList;

    public void setHelperList(List<Helper> helperList) {
        mHelperList = helperList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_helper, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(mHelperList.get(position).getTitle());
        holder.content.setText(mHelperList.get(position).getContent());
        holder.screenSort.setText(mHelperList.get(position).getScreenNum() + " " + mHelperList.get(position).getSort());
        holder.time.setText(mHelperList.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return ListUtils.getSize(mHelperList);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @ViewInject(R.id.tv_title)
        private TextView title;
        @ViewInject(R.id.tv_content)
        private TextView content;
        @ViewInject(R.id.tv_screen_sort)
        private TextView screenSort;
        @ViewInject(R.id.tv_time)
        private TextView time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ViewBind.inject(this, itemView);
        }
    }
}
