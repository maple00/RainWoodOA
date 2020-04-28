package com.rainwood.oa.ui.adapter;

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
import com.rainwood.oa.model.domain.IconAndFont;
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
        if (convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_manager_module, parent, false);
            ViewBind.inject(holder, convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        Glide.with(convertView).load(R.mipmap.bg_monkey_king)
               // .error(R.mipmap.bg_monkey_king)
               // .placeholder(R.mipmap.bg_monkey_king)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(holder.moduleImg);
        holder.moduleName.setText(getItem(position).getDesc());
        return convertView;
    }

    private class ViewHolder{
        @ViewInject(R.id.iv_module_img)
        private ImageView moduleImg;
        @ViewInject(R.id.tv_module_name)
        private TextView moduleName;
    }
}
