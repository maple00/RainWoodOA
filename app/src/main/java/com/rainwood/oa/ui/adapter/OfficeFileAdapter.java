package com.rainwood.oa.ui.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.OfficeFile;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.List;

/**
 * @Author: sxs
 * @Time: 2020/6/7 14:35
 * @Desc: 办公文件adapter
 */
public final class OfficeFileAdapter extends RecyclerView.Adapter<OfficeFileAdapter.ViewHolder> {

    private List<OfficeFile> mFileList;

    public void setFileList(List<OfficeFile> fileList) {
        mFileList = fileList;
        notifyDataSetChanged();
    }

    public void addData(List<OfficeFile> data) {
        if (data == null || data.size() == 0) {
            return;
        }

        if (mFileList == null || mFileList.size() == 0) {
            setFileList(data);
        } else {
            mFileList.addAll(data);
            notifyItemRangeInserted(mFileList.size() - data.size(), data.size());
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_office_file, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.fileType.setText(mFileList.get(position).getType());
        holder.fileName.setText(mFileList.get(position).getName() + "." + mFileList.get(position).getFormat());
        holder.secretIV.setVisibility("是".equals(mFileList.get(position).getSecret()) ? View.VISIBLE : View.GONE);
        holder.fileTime.setText(mFileList.get(position).getUpdateTime());
        if (mFileList.get(position).getSrc().endsWith(".zip")) {
            holder.previewIV.setVisibility(View.GONE);
            holder.previewTV.setVisibility(View.GONE);
        } else {
            holder.previewIV.setVisibility(View.VISIBLE);
            holder.previewTV.setVisibility(View.VISIBLE);
        }
        // 点击事件
        // 下载
        holder.downloadIV.setOnClickListener(v -> mItemFile.onClickDownload(mFileList.get(position), position));
        holder.downloadTV.setOnClickListener(v -> mItemFile.onClickDownload(mFileList.get(position), position));
        // 预览
        holder.previewIV.setOnClickListener(v -> mItemFile.onClickPreview(mFileList.get(position), position));
        holder.previewTV.setOnClickListener(v -> mItemFile.onClickPreview(mFileList.get(position), position));
    }

    @Override
    public int getItemCount() {
        return ListUtils.getSize(mFileList);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @ViewInject(R.id.tv_file_type)
        private TextView fileType;
        @ViewInject(R.id.iv_secret)
        private ImageView secretIV;
        @ViewInject(R.id.tv_file_name)
        private TextView fileName;
        @ViewInject(R.id.tv_file_time)
        private TextView fileTime;
        @ViewInject(R.id.iv_download)
        private ImageView downloadIV;
        @ViewInject(R.id.tv_download)
        private TextView downloadTV;
        @ViewInject(R.id.iv_preview)
        private ImageView previewIV;
        @ViewInject(R.id.tv_preview)
        private TextView previewTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ViewBind.inject(this, itemView);
        }
    }

    public interface OnClickItemFile {
        /**
         * 下载
         *
         * @param file
         * @param position
         */
        void onClickDownload(OfficeFile file, int position);

        /**
         * 预览
         *
         * @param file
         * @param position
         */
        void onClickPreview(OfficeFile file, int position);
    }

    private OnClickItemFile mItemFile;

    public void setItemFile(OnClickItemFile itemFile) {
        mItemFile = itemFile;
    }
}
