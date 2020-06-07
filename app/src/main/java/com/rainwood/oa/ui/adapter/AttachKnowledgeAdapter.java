package com.rainwood.oa.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.KnowledgeAttach;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.List;

/**
 * @Author: sxs
 * @Time: 2020/6/7 16:21
 * @Desc: 知识管理-- 附件管理
 */
public final class AttachKnowledgeAdapter extends RecyclerView.Adapter<AttachKnowledgeAdapter.ViewHolder> {

    private List<KnowledgeAttach> mAttachList;

    public void setAttachList(List<KnowledgeAttach> attachList) {
        mAttachList = attachList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_knowledge_attach, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // TODO: 文件item
    }

    @Override
    public int getItemCount() {
        return ListUtils.getSize(mAttachList);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @ViewInject(R.id.tv_size)
        private TextView size;
        @ViewInject(R.id.tv_attach_name)
        private TextView attachName;
        @ViewInject(R.id.tv_target_name)
        private TextView attachTargetName;
        @ViewInject(R.id.tv_name_time)
        private TextView nameTime;
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
}
