package com.rainwood.oa.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.FontAndFont;
import com.rainwood.oa.model.domain.OrderStatics;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/20 11:04
 * @Desc: 订单的属性
 */
public final class OrderNatureAdapter extends BaseAdapter {

    private List<FontAndFont> mList;

    public void setList(List<FontAndFont> list) {
        mList = list;
    }

    @Override
    public int getCount() {
        return ListUtils.getSize(mList);
    }

    @Override
    public FontAndFont getItem(int position) {
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_nature, parent, false);
            ViewBind.inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.title.setText(getItem(position).getTitle());
        holder.value.setText(getItem(position).getDesc());
        return convertView;
    }


    private static class ViewHolder {
        @ViewInject(R.id.tv_title)
        private TextView title;
        @ViewInject(R.id.tv_value)
        private TextView value;
        @ViewInject(R.id.ll_order_nature)
        private LinearLayout orderNature;
    }
}
