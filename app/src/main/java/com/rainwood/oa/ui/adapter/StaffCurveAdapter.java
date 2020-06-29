package com.rainwood.oa.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.StaffCurveList;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/29 13:57
 * @Desc: 员工曲线适配器
 */
public class StaffCurveAdapter extends BaseAdapter {

    private List<StaffCurveList> mCurveList;

    public void setCurveList(List<StaffCurveList> curveList) {
        mCurveList = curveList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return ListUtils.getSize(mCurveList);
    }

    @Override
    public StaffCurveList getItem(int position) {
        return mCurveList.get(position);
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_staff_num_curve, parent, false);
            ViewBind.inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mTextMonth.setText(getItem(position).getMonth());
        holder.mTextStaffNum.setText(getItem(position).getNumber());
        return convertView;
    }

    private static class ViewHolder {
        @ViewInject(R.id.tv_month)
        private TextView mTextMonth;
        @ViewInject(R.id.tv_staff_num)
        private TextView mTextStaffNum;
    }
}
