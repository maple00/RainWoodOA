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
import com.rainwood.oa.model.domain.Examination;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/19 14:54
 * @Desc: 审批流程图
 */
public final class ExaminationAdapter extends BaseAdapter {

    private List<Examination> mList = new ArrayList<>();

    public void setList(List<Examination> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return ListUtils.getSize(mList);
    }

    @Override
    public Examination getItem(int position) {
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_examination, parent, false);
            ViewBind.inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // 设置虚线的显示
        if (position == 0) {
            holder.aboveLine.setVisibility(View.INVISIBLE);
            holder.belowLine.setVisibility(View.VISIBLE);
        } else if (position == mList.size() - 1) {
            holder.aboveLine.setVisibility(View.VISIBLE);
            holder.belowLine.setVisibility(View.INVISIBLE);
        } else {
            holder.aboveLine.setVisibility(View.VISIBLE);
            holder.belowLine.setVisibility(View.VISIBLE);
        }
        // 头像
        Glide.with(convertView).load(getItem(position).getHeadPhoto())
                .error(R.drawable.ic_default_head)
                .placeholder(R.drawable.ic_default_head)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(holder.headPhoto);
        holder.name.setText(TextUtils.isEmpty(getItem(position).getName()) ? "" : getItem(position).getName());
        holder.departPost.setText(TextUtils.isEmpty(getItem(position).getDepart() + "-" + getItem(position).getPost()) ?
                "" : getItem(position).getDepart() + "-" + getItem(position).getPost());
        // 点击事件事件
        holder.itemClear.setOnClickListener(v -> {
            mClickClear.onClickExaminationClear(position);
            notifyDataSetChanged();
        });
        return convertView;
    }

    private static class ViewHolder {
        @ViewInject(R.id.iv_above_line)
        private ImageView aboveLine;
        @ViewInject(R.id.iv_below_line)
        private ImageView belowLine;
        @ViewInject(R.id.iv_head_photo)
        private ImageView headPhoto;
        @ViewInject(R.id.iv_item_clear)
        private ImageView itemClear;
        @ViewInject(R.id.tv_name)
        private TextView name;
        @ViewInject(R.id.tv_depart_post)
        private TextView departPost;
    }

    public interface OnClickClear {
        /**
         * 删除
         *
         * @param position
         */
        void onClickExaminationClear(int position);
    }

    private OnClickClear mClickClear;

    public void setClickClear(OnClickClear clickClear) {
        mClickClear = clickClear;
    }
}
