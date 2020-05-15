package com.rainwood.oa.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.IconAndFont;
import com.rainwood.oa.ui.activity.AttendanceActivity;
import com.rainwood.oa.ui.activity.CustomNewActivity;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/4/28 11:00
 * @Desc: 管理界面子项模块
 */
public final class ItemModuleAdapter extends BaseAdapter {

    private List<IconAndFont> mList;

    public void setList(List<IconAndFont> list) {
        mList = list;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public IconAndFont getItem(int position) {
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_manager_module, parent, false);
            ViewBind.inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Glide.with(convertView).load(getItem(position).getLocalMipmap())
                .error(R.drawable.bg_monkey_king)
                .placeholder(R.drawable.bg_monkey_king)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(holder.moduleImg);
        holder.moduleName.setText(getItem(position).getDesc());
        // 点击事件
        onItemClickValues(position, holder, convertView, parent);
        return convertView;
    }

    private static class ViewHolder {
        @ViewInject(R.id.iv_module_img)
        private ImageView moduleImg;
        @ViewInject(R.id.tv_module_name)
        private TextView moduleName;
        @ViewInject(R.id.ll_module_item)
        private LinearLayout moduleItem;
    }

    /**
     * 点击不同模块的点击事件
     *
     * @param position 点击的模块
     * @param holder   ViewHolder
     */
    private void onItemClickValues(int position, ViewHolder holder, View convertView, ViewGroup parent) {
        //setContext(convertView.getContext());
        holder.moduleItem.setOnClickListener(v -> {
            try {
                //ToastUtils.show("点击了-- " + position + " -- module ---- " + getItem(position).getDesc());
            } catch (Exception e) {
                // ToastUtils.show("点击了-- " + position);
            }
            switch (getItem(position).getDesc()) {
                case "我的考勤":
                    convertView.getContext().startActivity(getNewIntent(parent.getContext(), AttendanceActivity.class, "我的考勤"));
                    break;
                case "补卡记录":
                    break;
                // 客户管理模块
                case "新建客户":
                    convertView.getContext().startActivity(getNewIntent(parent.getContext(), CustomNewActivity.class, "新建客户"));
                    break;
                case "客户附件":

                    break;
            }
        });
    }


    /**
     * startActivity 优化
     */
    private Intent getNewIntent(Context context, Class<? extends BaseActivity> clazz, String title) {
        Intent intent = new Intent(context, clazz);
        intent.putExtra("title", title);
        return intent;
    }

}
