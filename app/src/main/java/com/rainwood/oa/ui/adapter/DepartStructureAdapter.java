package com.rainwood.oa.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.DepartStructure;
import com.rainwood.oa.ui.widget.MeasureListView;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/3 16:43
 * @Desc: 部门组织结构adapter
 */
public final class DepartStructureAdapter extends RecyclerView.Adapter<DepartStructureAdapter.ViewHolder> {

    private List<DepartStructure> mStructureList;
    private Context mContext;

    public void setStructureList(List<DepartStructure> structureList) {
        mStructureList = structureList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_depart_structure, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.departName.setText(Html.fromHtml("<font color='" + mContext.getColor(R.color.fontColor)
                + "'> " + mStructureList.get(position).getName() + "</font>"
                + "<font color='" + mContext.getColor(R.color.labelColor) + "'> ("
                + mStructureList.get(position).getNum() + ")</font>"));
        holder.arrow.setImageDrawable(mStructureList.get(position).isSelected()
                ? mContext.getDrawable(R.drawable.ic_up_arrow)
                : mContext.getDrawable(R.drawable.ic_down_arrow));

        StaffListAdapter staffListAdapter = new StaffListAdapter();
        holder.departStaff.setAdapter(staffListAdapter);
        staffListAdapter.setStaffStructureList(mStructureList.get(position).getArray());
        holder.departStaff.setVisibility(mStructureList.get(position).isSelected() ? View.VISIBLE : View.GONE);
        // 点击事件
        holder.departSelector.setOnClickListener(v -> {
            mStructureList.get(position).setSelected(!mStructureList.get(position).isSelected());
            notifyItemChanged(position);
        });
        staffListAdapter.setStaffListener(mItemStaffListener);
    }

    @Override
    public int getItemCount() {
        return ListUtils.getSize(mStructureList);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @ViewInject(R.id.ll_depart)
        private LinearLayout departSelector;
        @ViewInject(R.id.tv_depart_name)
        private TextView departName;
        @ViewInject(R.id.iv_arrow)
        private ImageView arrow;
        @ViewInject(R.id.mlv_depart_staff)
        private MeasureListView departStaff;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ViewBind.inject(this, itemView);
        }
    }

    private StaffListAdapter.OnClickItemStaffListener mItemStaffListener;

    public void setItemStaffListener(StaffListAdapter.OnClickItemStaffListener itemStaffListener) {
        mItemStaffListener = itemStaffListener;
    }
}
