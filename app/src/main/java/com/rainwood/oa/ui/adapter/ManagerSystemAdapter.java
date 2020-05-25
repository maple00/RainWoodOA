package com.rainwood.oa.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.ManagerSystem;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/25 18:55
 * @Desc: 管理制度adapter
 */
public final class ManagerSystemAdapter extends RecyclerView.Adapter<ManagerSystemAdapter.ViewHolder> {

    private List<ManagerSystem> mSystemList;

    public void setSystemList(List<ManagerSystem> systemList) {
        mSystemList = systemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_manager_system, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(mSystemList.get(position).getTitle());
        holder.content.setText(mSystemList.get(position).getContent());
        // 点击事件
        holder.itemSystem.setOnClickListener(v -> mClickSystem.onClickItem(mSystemList.get(position)));
    }

    @Override
    public int getItemCount() {
        return ListUtils.getSize(mSystemList);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @ViewInject(R.id.ll_item_system)
        private LinearLayout itemSystem;
        @ViewInject(R.id.tv_title)
        private TextView title;
        @ViewInject(R.id.tv_content)
        private TextView content;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ViewBind.inject(this, itemView);
        }
    }

    public interface OnClickSystem{
        /**
         * 查看详情
         * @param item
         */
        void onClickItem(ManagerSystem item);
    }

    public OnClickSystem mClickSystem;

    public void setClickSystem(OnClickSystem clickSystem) {
        mClickSystem = clickSystem;
    }
}
