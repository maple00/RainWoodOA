package com.rainwood.oa.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.SelectedItem;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/18 16:12
 * @Desc: 选择item adapter
 */
public final class SelectedItemAdapter extends BaseAdapter {

    private List<SelectedItem> mList;

    public void setList(List<SelectedItem> list) {
        LogUtils.d("sxs", "数据 ---- >" + list);
        mList = list;
    }

    @Override
    public int getCount() {
        return ListUtils.getSize(mList);
    }

    @Override
    public SelectedItem getItem(int position) {
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selected, parent, false);
            ViewBind.inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.content.setBackground(getItem(position).isHasSelected()
                ? parent.getContext().getDrawable(R.drawable.selector_selected)
                : parent.getContext().getDrawable(R.drawable.selector_uncheck_selected));
        holder.content.setTextColor(getItem(position).isHasSelected()
                ? parent.getContext().getColor(R.color.colorPrimary)
                : parent.getContext().getColor(R.color.colorMiddle));
        holder.content.setText(getItem(position).getName());
        return convertView;
    }


    private static class ViewHolder {
        @ViewInject(R.id.tv_content)
        private TextView content;
    }
}
