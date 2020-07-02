package com.rainwood.oa.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.CustomArea;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/7/2 9:51
 * @Desc: 客户列表区域adapter
 */
public final class CustomAreaAdapter extends RecyclerView.Adapter<CustomAreaAdapter.ViewHolder> {

    private List<CustomArea> mAreaList = new ArrayList<>();
    private Context mContext;

    public void setAreaList(List<CustomArea> areaList) {
        mAreaList = areaList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_pop_custon_list_area, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mTextValue.setText(mAreaList.get(position).getName());
        holder.mTextValue.setTextColor(mAreaList.get(position).isSelected()
                ? mContext.getColor(R.color.colorPrimary) : mContext.getColor(R.color.fontColor));
        holder.mImageSelected.setVisibility(mAreaList.get(position).isSelected() ? View.VISIBLE : View.GONE);
        // 点击事件
        holder.itemArea.setOnClickListener(v -> {
            mAreaListener.onClickItem(mAreaList.get(position), position);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return ListUtils.getSize(mAreaList);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @ViewInject(R.id.rl_item_area)
        private RelativeLayout itemArea;
        @ViewInject(R.id.tv_value)
        private TextView mTextValue;
        @ViewInject(R.id.iv_selected)
        private ImageView mImageSelected;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ViewBind.inject(this, itemView);
        }
    }

    public interface OnClickItemAreaListener {
        /**
         * 点击item
         */
        void onClickItem(CustomArea area, int position);
    }

    private OnClickItemAreaListener mAreaListener;

    public void setAreaListener(OnClickItemAreaListener areaListener) {
        mAreaListener = areaListener;
    }
}
