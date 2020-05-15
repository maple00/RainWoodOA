package com.rainwood.oa.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.ManagerMain;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/4/28 10:33
 * @Desc: 管理主界面adapter
 */
public final class ManagerMainAdapter extends RecyclerView.Adapter<ManagerMainAdapter.ViewHolder> {

    private List<ManagerMain> mData;


    public void setData(List<ManagerMain> data) {
        mData = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LogUtils.d(this, "onCreateViewHolder.....");
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_manager_main, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LogUtils.d(this, "onBindViewHolder....." + position);
        holder.title.setText(mData.get(position).getTitle());
        holder.image.setImageResource(mData.get(position).isHasSelected() ? R.drawable.ic_down_arrow : R.drawable.ic_up_arrow);
        holder.managerTop.setOnClickListener(v -> {
            mData.get(position).setHasSelected(!mData.get(position).isHasSelected());
            notifyItemChanged(position);
        });
        // 子项
        ItemModuleAdapter moduleAdapter = new ItemModuleAdapter();
        moduleAdapter.setList(mData.get(position).getIconAndFontList());
        holder.managerItem.setAdapter(moduleAdapter);
        if (!mData.get(position).isHasSelected()){   // 子项的隐藏显示
            holder.managerItem.setVisibility(View.GONE);
        }else {
            holder.managerItem.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @ViewInject(R.id.rl_manager_top)
        private RelativeLayout managerTop;
        @ViewInject(R.id.tv_title)
        private TextView title;
        @ViewInject(R.id.iv_img)
        private ImageView image;
        @ViewInject(R.id.gv_manager_item)
        private GridView managerItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ViewBind.inject(this, itemView);
        }
    }
}
