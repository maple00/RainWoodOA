package com.rainwood.oa.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
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

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: sxs
 * @Time: 2020/6/7 16:21
 * @Desc: 知识管理-- 附件管理
 */
public final class AttachKnowledgeAdapter extends RecyclerView.Adapter<AttachKnowledgeAdapter.ViewHolder> {

    private List<KnowledgeAttach> mAttachList = new ArrayList<>();
    private Context mContext;
    private boolean loaded;

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public void setAttachList(List<KnowledgeAttach> attachList) {
        if (loaded){
            mAttachList.clear();
        }
        mAttachList.addAll(attachList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_knowledge_attach, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final String secretSrc = "<img src=\"" + R.drawable.ic_secret + "\"/>";
        String secret = mAttachList.get(position).getSecret();
        // 获取图片资源
        final Html.ImageGetter imageGetter = new Html.ImageGetter() {
            public Drawable getDrawable(String source) {
                Drawable drawable = null;
                int rId = Integer.parseInt(source);
                drawable = mContext.getResources().getDrawable(rId);
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                return drawable;
            }
        };
        holder.attachName.setText(Html.fromHtml(mAttachList.get(position).getName()
                + " " + (secret.equals("是") ? secretSrc : ""), imageGetter, null));

        holder.size.setText(mAttachList.get(position).getSize());
        holder.attachTargetName.setText(Html.fromHtml("<font color=\"" + mContext.getColor(R.color.fontColor)
                + "\">" + mAttachList.get(position).getTarget() + "·" + "</font>"
                + "<font color=\"" + mContext.getColor(R.color.blue05) + "\">" + mAttachList.get(position).getTargetName() + "</font>"));
        holder.nameTime.setText(mAttachList.get(position).getStaffName() + " " + mAttachList.get(position).getTime());
        if (mAttachList.get(position).getSrc().endsWith(".zip")) {
            holder.previewTV.setVisibility(View.GONE);
            holder.previewIV.setVisibility(View.GONE);
        } else {
            holder.previewTV.setVisibility(View.VISIBLE);
            holder.previewIV.setVisibility(View.VISIBLE);
        }
        // 点击事件
        holder.attachTargetName.setOnClickListener(v -> mClickAttach.onClickTarget(mAttachList.get(position), position));

        holder.downloadIV.setOnClickListener(v -> mClickAttach.onClickDownload(mAttachList.get(position), position));
        holder.downloadTV.setOnClickListener(v -> mClickAttach.onClickDownload(mAttachList.get(position), position));

        holder.previewIV.setOnClickListener(v -> mClickAttach.onClickPreview(mAttachList.get(position), position));
        holder.previewTV.setOnClickListener(v -> mClickAttach.onClickPreview(mAttachList.get(position), position));
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

    public interface OnClickKnowledgeAttach {
        /**
         * 点击页面跳转
         *
         * @param attach
         * @param position
         */
        void onClickTarget(KnowledgeAttach attach, int position);

        /**
         * 下载
         *
         * @param attach
         * @param position
         */
        void onClickDownload(KnowledgeAttach attach, int position);

        /**
         * 预览
         *
         * @param attach
         * @param position
         */
        void onClickPreview(KnowledgeAttach attach, int position);
    }

    private OnClickKnowledgeAttach mClickAttach;

    public void setClickAttach(OnClickKnowledgeAttach clickAttach) {
        mClickAttach = clickAttach;
    }
}
