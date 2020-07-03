package com.rainwood.oa.ui.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.Provision;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/19 14:08
 * @Desc: 费用计提adapter
 */
public final class ProvisionAdapter extends BaseAdapter {

    private List<Provision> mList = new ArrayList<>();

    public void setList(List<Provision> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return ListUtils.getSize(mList);
    }

    @Override
    public Provision getItem(int position) {
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_provision, parent, false);
            ViewBind.inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.money.setText("￥" + getItem(position).getMoney());
        holder.used.setText(getItem(position).getUsed());
        // 点击事件
        holder.wasteClear.setOnClickListener(v -> {
            mOnClickWaste.onClickProvisionWaste(position);
            notifyDataSetChanged();
        });
        return convertView;
    }

    private static class ViewHolder {
        @ViewInject(R.id.tv_money)
        private TextView money;
        @ViewInject(R.id.tv_used)
        private TextView used;
        @ViewInject(R.id.iv_waste_clear)
        private ImageView wasteClear;
    }

    public interface OnClickWaste {
        /**
         * 删除
         *
         * @param position
         */
        void onClickProvisionWaste(int position);
    }

    private OnClickWaste mOnClickWaste;

    public void setOnClickWaste(OnClickWaste onClickWaste) {
        mOnClickWaste = onClickWaste;
    }
}
