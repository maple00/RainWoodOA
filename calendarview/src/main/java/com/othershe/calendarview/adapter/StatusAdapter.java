package com.othershe.calendarview.adapter;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.othershe.calendarview.R;
import com.othershe.calendarview.bean.StatusBean;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.List;

/**
 * create by a797s in 2020/5/11 17:44
 *
 * @Description : 用于描述OA办公考勤状态adapter
 * @Usage :
 **/
public final class StatusAdapter extends BaseAdapter {

    private List<StatusBean> mList;

    public void setList(List<StatusBean> list) {
        mList = list;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public StatusBean getItem(int position) {
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_status, parent, false);
            ViewBind.inject(this, convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // 动态设置背景颜色
        //  GradientDrawable drawable = (GradientDrawable) mContext.getResources().getDrawable(R.drawable.my_shape_message_type_title_bg);
        //  drawable .setColor(Color.parseColor("#ff4400"));

        holder.status.setText(getItem(position).getStatus());
        GradientDrawable drawable = (GradientDrawable) parent.getContext().getDrawable(R.drawable.shape_status_bg);
        drawable.setColor(getItem(position).getColor());
        holder.status.setBackground(drawable);
        return convertView;
    }

    private static class ViewHolder {

        @ViewInject(R.id.tv_status)
        private TextView status;
    }
}
