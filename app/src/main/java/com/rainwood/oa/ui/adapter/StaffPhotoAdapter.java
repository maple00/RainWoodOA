package com.rainwood.oa.ui.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.StaffPhoto;
import com.rainwood.oa.ui.activity.ImageActivity;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/25 10:12
 * @Desc: 员工的辅助信息
 */
public final class StaffPhotoAdapter extends BaseAdapter {

    private List<StaffPhoto> mList;
    private ArrayList<String> photoLit;

    public void setList(List<StaffPhoto> list) {
        mList = list;
        photoLit = new ArrayList<>();
        for (StaffPhoto photo : mList) {
            photoLit.add(photo.getOrigin());
        }
    }

    @Override
    public int getCount() {
        return ListUtils.getSize(mList);
    }

    @Override
    public StaffPhoto getItem(int position) {
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_staff_photo, parent, false);
            ViewBind.inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Glide.with(convertView).load(getItem(position).getOrigin())
                .error(R.drawable.bg_monkey_king)
                .placeholder(R.drawable.bg_monkey_king)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(holder.photo);
        // 查看大图
        holder.photo.setOnClickListener(v ->
                ImageActivity.start(parent.getContext(), photoLit, position));
        return convertView;
    }

    private static class ViewHolder {
        @ViewInject(R.id.iv_photo)
        private ImageView photo;
    }
}
