package com.rainwood.oa.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.contactslibrary.ContactsBean;
import com.rainwood.oa.R;
import com.rainwood.oa.utils.KeyWordUtil;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/7/21 13:32
 * @Desc: 联系人搜索列表
 */
public final class SearchContactAdapter extends RecyclerView.Adapter<SearchContactAdapter.ViewHolder> {

    private List<ContactsBean> contactsList = new ArrayList<>();
    private String keyWord;
    private Context mContext;

    public void setContactsList(List<ContactsBean> contactsList, String keyWord) {
        this.contactsList.clear();
        this.contactsList.addAll(contactsList);
        this.keyWord = keyWord;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.sub_item_text_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mTextValue.setText(KeyWordUtil.matcherSearchTitle(mContext.getColor(R.color.colorPrimary),
                contactsList.get(position).getName(), keyWord));
        holder.mTextValue.setOnClickListener(v -> mOnClickItemListener.onClickItem(contactsList.get(position), position));
    }

    @Override
    public int getItemCount() {
        return ListUtils.getSize(contactsList);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @ViewInject(R.id.tv_text_value)
        private TextView mTextValue;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ViewBind.inject(this, itemView);
        }
    }

    public interface OnClickItemListener{
        void onClickItem(ContactsBean contacts, int position);
    }

    private OnClickItemListener mOnClickItemListener;

    public void setOnClickItemListener(OnClickItemListener onClickItemListener) {
        mOnClickItemListener = onClickItemListener;
    }
}
