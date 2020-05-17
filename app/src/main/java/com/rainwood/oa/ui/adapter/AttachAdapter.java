package com.rainwood.oa.ui.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.Attachment;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.List;

/**
 * @Author: sxs
 * @Time: 2020/5/17 11:03
 * @Desc: 客户附件adapter
 */
public final class AttachAdapter extends RecyclerView.Adapter<AttachAdapter.ViewHolder> {

    private List<Attachment> mList;
    // 控制全选按钮
    private boolean checkBoxShow = false;
    private int selectedCount = 0;

    public void setList(List<Attachment> list) {
        mList = list;
    }

    public void setCheckBoxShow(boolean checkBoxShow) {
        this.checkBoxShow = checkBoxShow;
        // 设置全选内容
        selectedCount = (checkBoxShow ? ListUtils.getSize(mList) : 0);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attachment, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.attachName.setText(mList.get(position).getAttachmentName());
        holder.nameTime.setText(mList.get(position).getCustomName() + "\t" + mList.get(position).getTime());
        // 全选控制显示
        holder.mCheckBox.setVisibility(checkBoxShow ? View.VISIBLE : View.GONE);
        holder.mCheckBox.setChecked(mList.get(position).isSelected());
        // item选中回调
        holder.mCheckBox.setOnClickListener(v -> {
            boolean hasChecked = holder.mCheckBox.isChecked();
            if (hasChecked) {
                selectedCount++;
            } else {
                selectedCount--;
            }
            mClickCheckBox.onSelectedSwitch(hasChecked, position, selectedCount);
        });
    }

    @Override
    public int getItemCount() {
        return ListUtils.getSize(mList);
    }

    public interface OnClickCheckBox {
        /**
         * @param position      被选中的item
         * @param selectedCount 被选中的总数
         * @param status        被选择的状态
         */
        void onSelectedSwitch(boolean status, int position, int selectedCount);
    }

    private OnClickCheckBox mClickCheckBox;

    public void setClickCheckBox(OnClickCheckBox clickCheckBox) {
        mClickCheckBox = clickCheckBox;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @ViewInject(R.id.iv_attach_logo)
        private ImageView attachLogo;
        @ViewInject(R.id.tv_attach_name)
        private TextView attachName;
        @ViewInject(R.id.tv_name_time)
        private TextView nameTime;
        @ViewInject(R.id.cb_checked)
        private CheckBox mCheckBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ViewBind.inject(this, itemView);
        }

        @OnClick({R.id.iv_download, R.id.tv_download, R.id.iv_preview, R.id.tv_preview})
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.iv_download:
                case R.id.tv_download:
                    // 下载
                    LogUtils.d("sxs", "下载");
                    break;
                case R.id.iv_preview:
                case R.id.tv_preview:
                    // 预览
                    LogUtils.d("sxs", "预览");
                    break;
            }
        }
    }
}
