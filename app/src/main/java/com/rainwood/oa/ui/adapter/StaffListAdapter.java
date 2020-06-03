package com.rainwood.oa.ui.adapter;

import android.app.Activity;
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
import com.rainwood.oa.model.domain.StaffStructure;
import com.rainwood.oa.ui.widget.GroupIconText;
import com.rainwood.oa.utils.ClipboardUtil;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.oa.utils.PhoneCallUtil;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.toast.ToastUtils;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/3 17:03
 * @Desc: 员工列表adapter
 */
public final class StaffListAdapter extends BaseAdapter {

    private List<StaffStructure> mStaffStructureList;

    public void setStaffStructureList(List<StaffStructure> staffStructureList) {
        mStaffStructureList = staffStructureList;
    }

    @Override
    public int getCount() {
        return ListUtils.getSize(mStaffStructureList);
    }

    @Override
    public StaffStructure getItem(int position) {
        return mStaffStructureList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_item_staff, parent, false);
            ViewBind.inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Glide.with(convertView).load(R.drawable.bg_monkey_king)
                .placeholder(parent.getContext().getDrawable(R.drawable.ic_default_head))
                .error(parent.getContext().getDrawable(R.drawable.ic_default_head))
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(holder.headPhoto);
        holder.name.setText(getItem(position).getName());
        holder.post.setText(getItem(position).getPost());
        holder.telNum.setValue(getItem(position).getTel());
        holder.qqNumber.setValue(getItem(position).getTel());
        // 点击事件
        holder.telNum.setOnClickListener(v -> {
            // 拨打电话
            PhoneCallUtil.callPhoneDump((Activity) parent.getContext(), getItem(position).getTel());
        });
        holder.qqNumber.setOnClickListener(v -> {
            // 复制qq号
            ClipboardUtil.clipFormat2Board((Activity) parent.getContext(), "qqNum", getItem(position).getQq());
            ToastUtils.show("已复制");
        });
        return convertView;
    }

    private static class ViewHolder {
        @ViewInject(R.id.iv_head_photo)
        private ImageView headPhoto;
        @ViewInject(R.id.tv_name)
        private TextView name;
        @ViewInject(R.id.tv_post)
        private TextView post;
        @ViewInject(R.id.git_tel_num)
        private GroupIconText telNum;
        @ViewInject(R.id.git_qq_number)
        private GroupIconText qqNumber;
    }
}
