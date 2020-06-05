package com.rainwood.oa.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.KnowledgeFollowRecord;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.utils.FontSwitchUtil;
import com.rainwood.tools.widget.ExpandTextView;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/5 13:54
 * @Desc: 跟进记录adapter
 */
public final class FollowRecordsAdapter extends RecyclerView.Adapter<FollowRecordsAdapter.ViewHolder> {

    private List<KnowledgeFollowRecord> mRecordList;
    private Context mContext;
    private int default_width;

    public void setDefault_width(int default_width) {
        this.default_width = default_width;
    }

    public void setRecordList(List<KnowledgeFollowRecord> recordList) {
        mRecordList = recordList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_knowledge_follow_record, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.type.setText(mRecordList.get(position).getType() + " · ");
        holder.name.setText(mRecordList.get(position).getName());
        holder.nameTime.setText(Html.fromHtml("<font size=\"" + FontSwitchUtil.dip2px(mContext, 13f) + "\">"
                + mRecordList.get(position).getStaffName() + "  </font>"
                + "<font size=\"" + FontSwitchUtil.dip2px(mContext, 12f) + "\"> " + mRecordList.get(position).getTime() + "</font>"));
        int minimumWidth = holder.content.getMeasuredWidth();
        LogUtils.d("sxs", "--- measuredWidth --- " + minimumWidth);
        holder.content.initWidth(FontSwitchUtil.dip2px(mContext, minimumWidth));
        holder.content.setMaxLines(4);
        holder.content.setHasAnimation(true);
        holder.content.setCloseInNewLine(true);
        holder.content.setOpenSuffix("展开");
        holder.content.setCloseSuffix("收起");
        holder.content.setCloseSuffixColor(mContext.getColor(R.color.blue05));
        holder.content.setOpenSuffixColor(mContext.getColor(R.color.blue05));
        holder.content.setOriginalText(mRecordList.get(position).getContent());
        //TODO: ExTextView 有bug
    }

    @Override
    public int getItemCount() {
        return ListUtils.getSize(mRecordList);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @ViewInject(R.id.ll_type)
        private LinearLayout typeLL;
        @ViewInject(R.id.tv_type)
        private TextView type;
        @ViewInject(R.id.tv_name)
        private TextView name;
        @ViewInject(R.id.etv_content)
        private ExpandTextView content;
        @ViewInject(R.id.tv_name_time)
        private TextView nameTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ViewBind.inject(this, itemView);
        }
    }
}
