package com.rainwood.oa.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.CalendarStatics;
import com.rainwood.oa.ui.dialog.MessageDialog;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.wheel.BaseDialog;

import java.util.List;

/**
 * create by a797s in 2020/5/13 15:03
 *
 * @Description :
 * @Usage : 用于日历页面的统计信息展示
 **/
public final class CalendarStaticsAdapter extends BaseAdapter {

    private List<CalendarStatics> mList;
    // 每行有几个item default：2
    private int lineCount = 2;

    public void setList(List<CalendarStatics> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public void setLineCount(int lineCount) {
        this.lineCount = lineCount;
    }


    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public CalendarStatics getItem(int position) {
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_calendar_statics, parent, false);
            ViewBind.inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // 设置分割线
        holder.divider.setVisibility(position % lineCount == 0 ? View.GONE : View.VISIBLE);
        // 设置数据
        holder.data.setText(TextUtils.isEmpty(getItem(position).getData())
                ? "--"
                : getItem(position).getData());
        holder.data.setTextSize(getItem(position).getDataFontSize());
        holder.data.setBackgroundResource(getItem(position).getDataBackground() == 0
                ? R.drawable.selector_transparent
                : getItem(position).getDataBackground());
        // 设置数据描述
        holder.dataDesc.setText(getItem(position).getDescData());
        holder.doubtIV.setVisibility(getItem(position).getDescIcon() == 0 ? View.GONE : View.VISIBLE);

        // 设置信息备注
        if (TextUtils.isEmpty(getItem(position).getNote())) {
            holder.note.setVisibility(View.GONE);
        } else {
            holder.note.setVisibility(View.VISIBLE);
            holder.note.setText(getItem(position).getNote());
            holder.note.setTextSize(getItem(position).getNoteFontSize());
            holder.note.setBackgroundResource(getItem(position).getNoteBackground());
        }
        // 点击事件
        holder.doubtItem.setOnClickListener(v -> {
            if (getItem(position).getDescIcon() != 0)
                showDoubtDialog(position, parent.getContext(), holder, getItem(position).getNote());
        });
        return convertView;
    }

    /**
     * 点击疑问，显示弹窗
     *
     * @param position
     * @param context
     * @param holder
     * @param note
     */
    private void showDoubtDialog(int position, Context context, ViewHolder holder, String note) {
        new MessageDialog.Builder(context)
                .setTitle(null)
                .setMessage(note)
                .setConfirm(context.getString(R.string.common_confirm))
                .setCancel(null)
                .setShowConfirm(false)
                .setListener(new MessageDialog.OnListener() {
                    @Override
                    public void onConfirm(BaseDialog dialog) {
                    }

                    @Override
                    public void onCancel(BaseDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private static class ViewHolder {
        @ViewInject(R.id.tv_data)
        private TextView data;
        @ViewInject(R.id.tv_data_desc)
        private TextView dataDesc;
        @ViewInject(R.id.divider)
        private View divider;
        @ViewInject(R.id.tv_note)
        private TextView note;
        @ViewInject(R.id.ll_desc_doubt)
        private LinearLayout doubtItem;
        @ViewInject(R.id.iv_doubt)
        private ImageView doubtIV;
    }
}
