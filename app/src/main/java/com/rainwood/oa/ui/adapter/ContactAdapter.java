package com.rainwood.oa.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.Contact;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.wheel.aop.SingleClick;

import java.util.List;

/**
 * create by a797s in 2020/5/15 11:15
 *
 * @Description : 联系人adapter
 * @Usage :
 **/
public final class ContactAdapter extends BaseAdapter {

    private List<Contact> mList;

    public void setList(List<Contact> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Contact getItem(int position) {
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
            ViewBind.inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(getItem(position).getName());
        holder.post.setText(getItem(position).getPosition());
        holder.phoneNum.setText(getItem(position).getTel());
        // 点击拨打电话
        holder.playPhone.setOnClickListener(new View.OnClickListener() {
            @SingleClick
            @Override
            public void onClick(View v) {
                LogUtils.d("sxs", "拨打电话里了：" + getItem(position).getTel());
            }
        });
        return convertView;
    }

    private static class ViewHolder {
        @ViewInject(R.id.tv_name)
        private TextView name;
        @ViewInject(R.id.tv_post)
        private TextView post;
        @ViewInject(R.id.tv_phone_number)
        private TextView phoneNum;
        @ViewInject(R.id.iv_play_phone)
        private ImageView playPhone;
    }
}
