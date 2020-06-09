package com.rainwood.oa.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.StaffPost;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/22 13:22
 * @Desc: 员工部门职位
 */
public final class StaffPostAdapter extends BaseAdapter {

    private List<StaffPost> mPostList;
    private int parentPos = 0;

    public void setPostList(List<StaffPost> postList) {
        mPostList = postList;
    }

    @Override
    public int getCount() {
        return ListUtils.getSize(mPostList);
    }

    @Override
    public StaffPost getItem(int position) {
        return mPostList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_item_text_view, parent, false);
            ViewBind.inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.post.setText(getItem(position).getName());
        holder.post.setTextColor(getItem(position).isSelected() ? parent.getContext().getColor(R.color.colorPrimary)
                : parent.getContext().getColor(R.color.fontColor));
        // 点击事件
        holder.post.setOnClickListener(v -> {
            mClickPost.onClickPost(parentPos, position);
            notifyDataSetChanged();
        });
        return convertView;
    }

    private static class ViewHolder {
        @ViewInject(R.id.tv_text_value)
        private TextView post;
    }

    public interface OnClickPost {
        /**
         * 点击职位
         *
         * @param position
         */
        void onClickPost(int parentPos, int position);
    }

    private OnClickPost mClickPost;

    public void setClickPost(int parentPos, OnClickPost clickPost) {
        mClickPost = clickPost;
        this.parentPos = parentPos;
    }
}
