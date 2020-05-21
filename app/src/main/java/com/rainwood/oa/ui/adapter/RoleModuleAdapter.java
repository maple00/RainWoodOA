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

/**
 * @Author: a797s
 * @Date: 2020/5/21 14:03
 * @Desc: 角色列表 --- 角色模块
 */
public final class RoleModuleAdapter extends BaseAdapter {

    private List<SubRoleXModule> mModuleList;

    public void setModuleList(List<SubRoleXModule> moduleList) {
        mModuleList = moduleList;
    }

    @Override
    public int getCount() {
        return ListUtils.getSize(mModuleList);
    }

    @Override
    public SubRoleXModule getItem(int position) {
        return mModuleList.get(position);
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
        holder.roleModule.setText(getItem(position).getModuleX());
        return convertView;
    }

    private static class ViewHolder {
        @ViewInject(R.id.tv_role_module)
        private TextView roleModule;
    }
}
