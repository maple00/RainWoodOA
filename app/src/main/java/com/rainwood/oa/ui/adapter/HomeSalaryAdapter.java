package com.rainwood.oa.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.FontAndFont;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/4/29 11:06
 * @Desc: 首页工资曲线适配器
 */
public final class HomeSalaryAdapter extends BaseAdapter {

    private List<FontAndFont> mList;

    public void setList(List<FontAndFont> list) {
        mList = list;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public FontAndFont getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_salary, parent, false);
            ViewBind.inject(holder, convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.salary.setText(getItem(position).getTitle());
        holder.salaryDesc.setText(getItem(position).getDesc());
        return convertView;
    }

    private static class ViewHolder{
        @ViewInject(R.id.tv_salary)
        private TextView salary;
        @ViewInject(R.id.tv_desc)
        private TextView salaryDesc;
    }
}
