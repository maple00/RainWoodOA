package com.rainwood.oa.ui.adapter;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.Attachment;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/19 13:52
 * @Desc: 订单附件
 */
public final class OrderAttachAdapter extends BaseAdapter {

    List<Attachment> mList;

    public void setList(List<Attachment> list) {
        mList = list;
    }

    @Override
    public int getCount() {
        return ListUtils.getSize(mList);
    }

    @Override
    public Attachment getItem(int position) {
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_attach, parent, false);
            ViewBind.inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.attachName.setText(TextUtils.isEmpty(getItem(position).getName()) ? "" : getItem(position).getName());
        holder.nameTime.setText((TextUtils.isEmpty(getItem(position).getStaffName()) ? "" : getItem(position).getStaffName()) +
                (TextUtils.isEmpty(getItem(position).getTime()) ? "" : getItem(position).getTime()));
        // 点击事件
        holder.wasteClear.setOnClickListener(v -> {
            mClickWasteClear.onClickClear(position);
            notifyDataSetChanged();
        });
        return convertView;
    }

    private static class ViewHolder {
        @ViewInject(R.id.iv_attach_logo)
        private ImageView attachLogo;
        @ViewInject(R.id.tv_attach_name)
        private TextView attachName;
        @ViewInject(R.id.iv_waste_clear)
        private ImageView wasteClear;
        @ViewInject(R.id.tv_name_time)
        private TextView nameTime;
    }

    public interface OnClickWasteClear {
        /**
         * 点击删除
         *
         * @param position
         */
        void onClickClear(int position);
    }

    private OnClickWasteClear mClickWasteClear;

    public void setClickWasteClear(OnClickWasteClear clickWasteClear) {
        mClickWasteClear = clickWasteClear;
    }
}
