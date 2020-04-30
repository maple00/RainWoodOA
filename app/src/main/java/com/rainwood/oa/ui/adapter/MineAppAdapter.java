package com.rainwood.oa.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.TempAppMine;
import com.rainwood.oa.utils.AppCacheManager;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.toast.ToastUtils;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/4/28 17:14
 * @Desc: mine--- 应用信息
 */
public final class MineAppAdapter extends BaseAdapter {

    private List<TempAppMine> mList;

    public void setAppMines(List<TempAppMine> appMines) {
        this.mList = appMines;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public TempAppMine getItem(int position) {
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mine_app, parent, false);
            ViewBind.inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.icon.setImageResource(getItem(position).getIcon());
        holder.name.setText(getItem(position).getName());
        holder.label.setText(getItem(position).getNote());
        // 点击事件
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ToastUtils.setGravity(Gravity.BOTTOM, 0, 0);
                switch (position) {
                    case 0:         // 修改密码
                        ToastUtils.show("点击了：" + getItem(position).getName());
                        break;
                    case 1:         // 清除缓存
                        try {
                            LogUtils.d("sxs", "缓存大小 ----> " + AppCacheManager.getTotalCacheSize(parent.getContext()));
                            String totalCacheSize = AppCacheManager.getTotalCacheSize(parent.getContext());
                            getItem(position).setNote(totalCacheSize);
                            notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case 2:         // 版本更新
                        // 升级对话框
                        ToastUtils.show("点击了：" + getItem(position).getName());
                        break;
                }
            }
        });
        return convertView;
    }

    private class ViewHolder {
        @ViewInject(R.id.ll_item)
        private LinearLayout item;
        @ViewInject(R.id.iv_icon)
        private ImageView icon;
        @ViewInject(R.id.tv_name)
        private TextView name;
        @ViewInject(R.id.tv_label)
        private TextView label;
    }
}
