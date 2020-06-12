package com.rainwood.oa.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.Month;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.utils.DateTimeUtils;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/10 13:23
 * @Desc: 横向月份adapter
 */
public final class HorizontalMonthAdapter extends RecyclerView.Adapter<HorizontalMonthAdapter.ViewHolder> {

    private List<Month> mMonthList;
    private Context mContext;
    private String currentYear;

    public void setMonthList(List<Month> monthList) {
        mMonthList = monthList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_month, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // 获取真实的地址
        int realPosition = position % ListUtils.getSize(mMonthList);
        if (mMonthList.get(realPosition).getMonth().equals(getCurrentMonth())
                && currentYear.contains(getCurrentYear())) {
            holder.month.setBackground(mContext.getDrawable(R.drawable.shape_oval_theme));
            holder.month.setTextColor(mContext.getColor(R.color.white));
            holder.month.setText(mMonthList.get(realPosition).getMonth() + "月");
            // holder.month.setTextSize(FontSwitchUtil.dip2px(mContext, 12f));
        } else {
            holder.month.setBackground(mContext.getDrawable(R.drawable.shape_oval_theme_transparent));
            holder.month.setText(mMonthList.get(realPosition).getMonth() + "月");
            holder.month.setTextColor(mContext.getColor(R.color.fontColor));
        }
    }

    @Override
    public int getItemCount() {
        return ListUtils.getSize(mMonthList);
    }

    public void setYear(String year) {
        currentYear = year;
    }

    /**
     * 当前月份
     *
     * @return
     */
    private String getCurrentMonth() {
        return String.valueOf(DateTimeUtils.getNowMonth());
    }

    /**
     * 当前年份
     *
     * @return
     */
    private String getCurrentYear() {
        return String.valueOf(DateTimeUtils.getNowYear());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @ViewInject(R.id.tv_month)
        private TextView month;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ViewBind.inject(this, itemView);
        }
    }
}
