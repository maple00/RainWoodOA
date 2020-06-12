package com.rainwood.oa.ui.adapter;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.StaffExperience;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/25 10:49
 * @Desc: 员工工作经历
 */
public final class StaffExperienceAdapter extends BaseAdapter {

    private List<StaffExperience> mList;

    public void setList(List<StaffExperience> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return ListUtils.getSize(mList);
    }

    @Override
    public StaffExperience getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_staff_work_experience, parent, false);
            ViewBind.inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.company.setText(TextUtils.isEmpty(getItem(position).getCompanyName()) ? "" : getItem(position).getCompanyName());
        holder.post.setText(TextUtils.isEmpty(getItem(position).getPosition()) ? "" : getItem(position).getPosition());
        //holder.entryOutTime.setText((TextUtils.isEmpty(getItem(position).getEntryTime()) ? "" : getItem(position).getCompanyName())
        //        + "-"
        //        + (TextUtils.isEmpty(getItem(position).getDepartureTime()) ? "" : getItem(position).getDepartureTime()));
        holder.entryOutTime.setText(getItem(position).getDayStart());
        holder.duty.setText(TextUtils.isEmpty(getItem(position).getContent()) ? "" : getItem(position).getContent());
        holder.leavingReason.setText(TextUtils.isEmpty(getItem(position).getCause()) ? "" : getItem(position).getCause());
        // 点击事件--查看详情
        holder.itemExperience.setOnClickListener(v -> mClickExperience.onClickExperience(getItem(position)));
        return convertView;
    }

    private static class ViewHolder {
        @ViewInject(R.id.tv_company)
        private TextView company;
        @ViewInject(R.id.tv_post)
        private TextView post;
        @ViewInject(R.id.tv_entry_out_time)
        private TextView entryOutTime;
        @ViewInject(R.id.tv_duty)
        private TextView duty;
        @ViewInject(R.id.tv_reason)
        private TextView leavingReason;
        @ViewInject(R.id.ll_item_experience)
        private LinearLayout itemExperience;
    }

    public interface OnItemClickExperience{
        /**
         * 查看详情
         * @param staffExperience
         */
        void onClickExperience(StaffExperience staffExperience);
    }

    private OnItemClickExperience mClickExperience;

    public void setClickExperience(OnItemClickExperience clickExperience) {
        mClickExperience = clickExperience;
    }
}
