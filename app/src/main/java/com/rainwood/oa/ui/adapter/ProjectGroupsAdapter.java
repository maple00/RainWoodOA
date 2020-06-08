package com.rainwood.oa.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.ProjectGroup;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/21 17:23
 * @Desc: 项目组
 */
public final class ProjectGroupsAdapter extends BaseAdapter {

    private List<ProjectGroup> mGroupList;
    private int parentPos;

    public void setGroupList(List<ProjectGroup> groupList) {
        mGroupList = groupList;
    }

    @Override
    public int getCount() {
        return ListUtils.getSize(mGroupList);
    }

    @Override
    public ProjectGroup getItem(int position) {
        return mGroupList.get(position);
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_project_groups, parent, false);
            ViewBind.inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.group.setText(getItem(position).getName());
        holder.duty.setText(getItem(position).getText());
        // 点击事件--点击组查看详情
        holder.itemGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickGroup.onClickItem(parentPos, getItem(position));
            }
        });
        return convertView;
    }

    private static class ViewHolder {

        @ViewInject(R.id.ll_item_group)
        private LinearLayout itemGroup;
        @ViewInject(R.id.tv_group)
        private TextView group;
        @ViewInject(R.id.tv_duty)
        private TextView duty;
    }

    public interface OnClickGroup {
        /**
         * 点击项目组
         *
         * @param group
         */
        void onClickItem(int parentPos, ProjectGroup group);
    }

    private OnClickGroup mClickGroup;

    public void setClickGroup(int parentPos, OnClickGroup clickGroup) {
        mClickGroup = clickGroup;
        this.parentPos = parentPos;
    }
}
