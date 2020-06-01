package com.rainwood.oa.ui.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.PrimaryKey;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.utils.FontSwitchUtil;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/1 16:13
 * @Desc:
 */
public final class PrimaryKeyAdapter extends RecyclerView.Adapter<PrimaryKeyAdapter.ViewHolder> {

    private List<PrimaryKey> mKeyList;
    private String keyWord = "";
    private Context mContext;

    /**
     * 设置关键字
     * @param keyWord
     */
    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public void setKeyList(List<PrimaryKey> keyList) {
        mKeyList = keyList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_item_text_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.value.setText(Html.fromHtml(mKeyList.get(position).getCompanyName().replace(keyWord,
                "<font color=" + mContext.getColor(R.color.labelColor) + "> " + keyWord + "</font>")));
        holder.value.setTextColor(mContext.getColor(R.color.fontColor));
        //holder.value.setTextSize(FontSwitchUtil.dip2px(mContext, 15f));
        // 点击事件
        holder.value.setOnClickListener(v -> mClickPrimary.onClickItem(mKeyList.get(position), position));
    }

    @Override
    public int getItemCount() {
        return ListUtils.getSize(mKeyList);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @ViewInject(R.id.tv_text_value)
        private TextView value;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ViewBind.inject(this, itemView);
        }
    }

    public interface OnClickPrimary {
        /**
         * 选中某一个
         *
         * @param primaryKey
         * @param position
         */
        void onClickItem(PrimaryKey primaryKey, int position);
    }

    private OnClickPrimary mClickPrimary;

    public void setClickPrimary(OnClickPrimary clickPrimary) {
        mClickPrimary = clickPrimary;
    }
}
