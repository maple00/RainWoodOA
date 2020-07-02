package com.rainwood.oa.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.CustomScreenAll;
import com.rainwood.oa.ui.widget.MeasureGridView;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/7/2 11:06
 * @Desc: 客户列表全部筛洗
 */
public final class CustomListFilterAdapter extends RecyclerView.Adapter<CustomListFilterAdapter.ViewHolder> {

    private List<CustomScreenAll> mScreenAllList;

    public void setScreenAllList(List<CustomScreenAll> screenAllList) {
        mScreenAllList = screenAllList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pop_custom_screen_all, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mTextTitle.setText(mScreenAllList.get(position).getTitle());
        CustomScreenItemAdapter screenItemAdapter = new CustomScreenItemAdapter();
        holder.value.setAdapter(screenItemAdapter);
        holder.value.setNumColumns(4);
        screenItemAdapter.setTextList(mScreenAllList.get(position).getArray(), mScreenAllList.get(position), position);
        screenItemAdapter.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount() {
        return ListUtils.getSize(mScreenAllList);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @ViewInject(R.id.tv_title)
        private TextView mTextTitle;
        @ViewInject(R.id.mgv_value)
        private MeasureGridView value;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ViewBind.inject(this, itemView);
        }
    }

    private CustomScreenItemAdapter.OnClickListener mOnClickListener;

    public void setOnClickListener(CustomScreenItemAdapter.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }
}
