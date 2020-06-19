package com.rainwood.oa.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.RecordResults;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/19 17:40
 * @Desc: 成果列表（加班成果、外出成果....）
 */
public final class RecordsResultsAdapter extends BaseAdapter {

    private List<RecordResults> mResultsList;

    public void setResultsList(List<RecordResults> resultsList) {
        mResultsList = resultsList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return ListUtils.getSize(mResultsList);
    }

    @Override
    public RecordResults getItem(int position) {
        return mResultsList.get(position);
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_record_result, parent, false);
            ViewBind.inject(holder, convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.resultObj.setText(getItem(position).getTarget());
        holder.duration.setText(getItem(position).getHour());
        holder.content.setText(getItem(position).getText());
        return convertView;
    }

    public static class ViewHolder{
        @ViewInject(R.id.tv_result_obj)
        private TextView resultObj;
        @ViewInject(R.id.tv_duration)
        private TextView duration;
        @ViewInject(R.id.tv_content)
        private TextView content;
    }
}
