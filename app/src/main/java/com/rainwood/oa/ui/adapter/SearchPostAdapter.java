package com.rainwood.oa.ui.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/7/3 10:01
 * @Desc: 位置搜索adapter
 */
public final class SearchPostAdapter extends RecyclerView.Adapter<SearchPostAdapter.ViewHolder> {

    // 搜索结果
    private List<String> mList;
    // 搜索关键字
    private String searchKey;
    private Context mContext;

    public void setList(List<String> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext= parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_search_position, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mTextPosition.setText(Html.fromHtml(mList.get(position).replace(searchKey,
                "<font color=" + mContext.getColor(R.color.labelColor) + ">" + searchKey + "</font>")));
        // 点击事件
        holder.mTextPosition.setOnClickListener(v -> mOnClickSearchPost.onClickItem(mList.get(position), position));
    }

    @Override
    public int getItemCount() {
        return ListUtils.getSize(mList);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @ViewInject(R.id.tv_position)
        private TextView mTextPosition;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ViewBind.inject(this, itemView);
        }
    }

    public interface OnClickSearchPost{
        /**
         * 选中
         * @param clickText
         * @param position
         */
        void onClickItem(String clickText, int position);
    }

    private OnClickSearchPost mOnClickSearchPost;

    public void setOnClickSearchPost(OnClickSearchPost onClickSearchPost) {
        mOnClickSearchPost = onClickSearchPost;
    }
}
