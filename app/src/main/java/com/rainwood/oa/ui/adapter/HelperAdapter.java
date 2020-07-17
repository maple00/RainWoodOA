package com.rainwood.oa.ui.adapter;

import android.annotation.SuppressLint;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.Article;
import com.rainwood.oa.model.domain.Helper;
import com.rainwood.oa.model.domain.Logcat;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/2 10:39
 * @Desc: 帮助中心adapter
 */
public final class HelperAdapter extends RecyclerView.Adapter<HelperAdapter.ViewHolder> {

    private List<Article> mHelperList ;

    public void setHelperList(List<Article> helperList) {
        mHelperList = helperList;
        notifyDataSetChanged();
    }

    /**
     * 追加一些数据
     */
    public void addData(List<Article> data) {
        if (data == null || data.size() == 0) {
            return;
        }

        if (mHelperList == null || mHelperList.size() == 0) {
            setHelperList(data);
        } else {
            mHelperList.addAll(data);
            notifyItemRangeInserted(mHelperList.size() - data.size(), data.size());
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_helper, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(mHelperList.get(position).getTitle());
        holder.content.setText(Html.fromHtml(mHelperList.get(position).getWord()));
        holder.screenSort.setText(mHelperList.get(position).getClickRate() + " 浏览");
        holder.time.setText(mHelperList.get(position).getUpdateTime());

        // 点击事件
        holder.itemHelper.setOnClickListener(v -> mClickItemHelper.onClickItem(mHelperList.get(position), position));
    }

    @Override
    public int getItemCount() {
        return ListUtils.getSize(mHelperList);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @ViewInject(R.id.ll_item_helper)
        private LinearLayout itemHelper;
        @ViewInject(R.id.tv_title)
        private TextView title;
        @ViewInject(R.id.tv_content)
        private TextView content;
        @ViewInject(R.id.tv_screen_sort)
        private TextView screenSort;
        @ViewInject(R.id.tv_time)
        private TextView time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ViewBind.inject(this, itemView);
        }
    }

    public interface OnClickItemHelper{
        /**
         * 查看详情
         * @param article
         * @param position
         */
        void onClickItem(Article article, int position);
    }

    private OnClickItemHelper mClickItemHelper;

    public void setClickItemHelper(OnClickItemHelper clickItemHelper) {
        mClickItemHelper = clickItemHelper;
    }
}
