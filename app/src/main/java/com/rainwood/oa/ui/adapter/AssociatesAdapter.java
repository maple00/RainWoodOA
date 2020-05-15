package com.rainwood.oa.ui.adapter;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.Associates;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.wheel.aop.SingleClick;

import java.util.List;

/**
 * create by a797s in 2020/5/15 11:00
 *
 * @Description : 协作人adapter
 * @Usage :
 **/
public final class AssociatesAdapter extends BaseAdapter {

    private List<Associates> mList;

    public void setList(List<Associates> list) {
        mList = list;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Associates getItem(int position) {
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_associates, parent, false);
            ViewBind.inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Glide.with(convertView).load(getItem(position).getHeadSrc())
                .error(R.drawable.bg_monkey_king)
                .placeholder(R.drawable.bg_monkey_king)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(holder.headPhoto);
        holder.name.setText(TextUtils.isEmpty(getItem(position).getName()) ? "" : getItem(position).getName());
        holder.departPost.setText(getItem(position).getDepart() + "-" + getItem(position).getPost());
        // 点击删除事件
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @SingleClick
            @Override
            public void onClick(View v) {
                LogUtils.d("sxs", "删除了协作人: " + getItem(position).getName());
                mList.remove(position);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    private static class ViewHolder {
        @ViewInject(R.id.iv_head_photo)
        private ImageView headPhoto;
        @ViewInject(R.id.tv_name)
        private TextView name;
        @ViewInject(R.id.tv_depart_post)
        private TextView departPost;
        @ViewInject(R.id.iv_clear)
        private ImageView delete;
    }
}
