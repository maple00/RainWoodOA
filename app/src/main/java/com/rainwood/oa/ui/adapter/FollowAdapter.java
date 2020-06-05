package com.rainwood.oa.ui.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.FollowRecord;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/19 14:29
 * @Desc: 跟进记录 adapter
 */
public final class FollowAdapter extends BaseAdapter {

    private List<FollowRecord> mList;

    public void setList(List<FollowRecord> list) {
        mList = list;
    }

    @Override
    public int getCount() {
        return ListUtils.getSize(mList);
    }

    @Override
    public FollowRecord getItem(int position) {
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_custom_follow_record, parent, false);
            ViewBind.inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.record.setText(TextUtils.isEmpty(getItem(position).getRecord()) ? "" : getItem(position).getRecord());
        holder.time.setText(TextUtils.isEmpty(getItem(position).getTime()) ? "" : getItem(position).getTime());
        // 点击事件
        holder.wasteClear.setOnClickListener(v -> mOnClickWaste.onClickFollowWaste(position));
        return convertView;
    }

    private static class ViewHolder {
        @ViewInject(R.id.tv_record)
        private TextView record;
        @ViewInject(R.id.tv_time)
        private TextView time;
        @ViewInject(R.id.iv_waste_clear)
        private ImageView wasteClear;
    }

    public interface OnClickFollowWaste {
        /**
         * 删除
         *
         * @param position
         */
        void onClickFollowWaste(int position);
    }

    private OnClickFollowWaste mOnClickWaste;

    public void setOnClickWaste(OnClickFollowWaste onClickWaste) {
        mOnClickWaste = onClickWaste;
    }
}
