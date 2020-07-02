package com.rainwood.oa.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.BalanceCurveListData;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/29 11:14
 * @Desc:
 */
public class BalanceCurveAdapter extends BaseAdapter {

    private List<BalanceCurveListData> mCurveListData;

    public void setCurveListData(List<BalanceCurveListData> curveListData) {
        LogUtils.d("sxs", "-- 列表--- " + curveListData);
        mCurveListData = curveListData;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return ListUtils.getSize(mCurveListData);
    }

    @Override
    public BalanceCurveListData getItem(int position) {
        return mCurveListData.get(position);
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_balance_curve_list, parent, false);
            ViewBind.inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.month.setText(getItem(position).getMonth());
        holder.income.setText(getItem(position).getIncome());
        holder.outcome.setText(getItem(position).getOutcome());
        holder.salary.setText(getItem(position).getSalary());
        holder.settlement.setText(getItem(position).getSettlement());
        return convertView;
    }

    private static class ViewHolder {
        @ViewInject(R.id.tv_month)
        private TextView month;
        @ViewInject(R.id.tv_income)
        private TextView income;
        @ViewInject(R.id.tv_outcome)
        private TextView outcome;
        @ViewInject(R.id.tv_salary)
        private TextView salary;
        @ViewInject(R.id.tv_settlement)
        private TextView settlement;
    }
}
