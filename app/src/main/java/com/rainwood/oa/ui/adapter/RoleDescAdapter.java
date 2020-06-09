package com.rainwood.oa.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.SubRoleXModule;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.List;
import java.util.Objects;

/**
 * @Author: a797s
 * @Date: 2020/6/8 18:00
 * @Desc: 角色权限
 */
public final class RoleDescAdapter extends BaseAdapter {

    private List<SubRoleXModule> mList;

    public void setList(List<SubRoleXModule> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return ListUtils.getSize(mList);
    }

    @Override
    public SubRoleXModule getItem(int position) {
        return mList.get(position);
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_item_role_module, parent, false);
            ViewBind.inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.permission.setText(mList.get(position).getName());
        return convertView;
    }

    private static class ViewHolder {
        @ViewInject(R.id.tv_role_module)
        private TextView permission;
    }
}
