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
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/21 11:29
 * @Desc: 沟通技巧adapter
 */
public final class CommunicationAdapter extends RecyclerView.Adapter<CommunicationAdapter.ViewHolder> {

    private List<Article> mList = new ArrayList<>();
    private boolean loaded;

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public void setList(List<Article> list) {
        if (loaded){
            mList.clear();
        }
        mList.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exchange_skill, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(mList.get(position).getTitle());
        holder.content.setText(Html.fromHtml(mList.get(position).getWord()));
        holder.screen.setText(mList.get(position).getClickRate() + " 浏览");
        holder.time.setText(mList.get(position).getUpdateTime());

        // 点击事件
        holder.item.setOnClickListener(v -> mItemCommunication.onClickItem(mList.get(position), position));
    }

    @Override
    public int getItemCount() {
        return ListUtils.getSize(mList);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @ViewInject(R.id.ll_item_communication)
        private LinearLayout item;
        @ViewInject(R.id.tv_title)
        private TextView title;
        @ViewInject(R.id.tv_content)
        private TextView content;
        @ViewInject(R.id.tv_screen)
        private TextView screen;
        @ViewInject(R.id.tv_time)
        private TextView time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ViewBind.inject(this, itemView);
        }
    }

    public interface OnClickItemCommunication {
        /**
         * 查看详情
         *
         * @param skills
         * @param position
         */
        void onClickItem(Article skills, int position);
    }

    private OnClickItemCommunication mItemCommunication;

    public void setItemCommunication(OnClickItemCommunication itemCommunication) {
        mItemCommunication = itemCommunication;
    }
}
