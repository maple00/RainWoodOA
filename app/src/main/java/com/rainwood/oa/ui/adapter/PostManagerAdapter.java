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
import com.rainwood.oa.model.domain.Post;
import com.rainwood.oa.ui.widget.GroupIconText;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.utils.FontSwitchUtil;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/22 9:24
 * @Desc: 职位管理adapter
 */
public final class PostManagerAdapter extends RecyclerView.Adapter<PostManagerAdapter.ViewHolder> {

    private List<Post> mPostList;
    private Context mContext;

    public void setPostList(List<Post> postList) {
        mPostList = postList;
        notifyDataSetChanged();
    }

    /**
     * 追加一些数据
     */
    public void addData(List<Post> data) {
        if (data == null || data.size() == 0) {
            return;
        }
        if (mPostList == null || mPostList.size() == 0) {
            setPostList(data);
        } else {
            mPostList.addAll(data);
            notifyItemRangeInserted(mPostList.size() - data.size(), data.size());
            notifyDataSetChanged();
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_post_list, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.post.setText(mPostList.get(position).getName());
        holder.salary.setText(Html.fromHtml("<font color=" + mContext.getColor(R.color.fontColor) + " size= \""
                + FontSwitchUtil.dip2px(mContext, 12f) + "\">基</font>"
                + "<font color=" + mContext.getColor(R.color.fontColor) + " size= \""
                + FontSwitchUtil.dip2px(mContext, 15f) + "\">" + mPostList.get(position).getBasePay() + "+</font>"
                + "<font color=\"" + mContext.getColor(R.color.colorPrimary) + "\" size= \""
                + FontSwitchUtil.dip2px(mContext, 15f) + "\">津" + mPostList.get(position).getSubsidy() + "</font>"));
        holder.depart.setValue(mPostList.get(position).getDepartmentName());
        holder.role.setValue(mPostList.get(position).getRoleName());
        holder.desc.setText(mPostList.get(position).getText());
        // 点击事件
        holder.itemPost.setOnClickListener(v -> mClickPostItem.onClickPost(mPostList.get(position)));
    }

    @Override
    public int getItemCount() {
        return ListUtils.getSize(mPostList);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @ViewInject(R.id.ll_item_post)
        private LinearLayout itemPost;
        @ViewInject(R.id.tv_post)
        private TextView post;
        @ViewInject(R.id.tv_salary)
        private TextView salary;
        @ViewInject(R.id.git_depart)
        private GroupIconText depart;
        @ViewInject(R.id.gti_role)
        private GroupIconText role;
        @ViewInject(R.id.tv_desc)
        private TextView desc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ViewBind.inject(this, itemView);
        }
    }

    public interface OnClickPostItem {
        /**
         * 查看详情
         *
         * @param post
         */
        void onClickPost(Post post);
    }

    private OnClickPostItem mClickPostItem;

    public void setClickPostItem(OnClickPostItem clickPostItem) {
        mClickPostItem = clickPostItem;
    }
}
