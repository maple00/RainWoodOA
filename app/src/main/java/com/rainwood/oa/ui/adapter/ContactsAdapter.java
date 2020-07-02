package com.rainwood.oa.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.rainwood.contactslibrary.ContactsBean;
import com.rainwood.oa.R;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/17 13:13
 * @Desc: 联系人
 */
public final class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    private List<ContactsBean> mDatas;
    private Context mContext;

    public void setDatas(List<ContactsBean> datas) {
        mDatas = datas;
        notifyDataSetChanged();
    }

    @Override
    public ContactsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address_book, parent, false);
        return new ContactsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ContactsBean contactsBean = mDatas.get(position);
        holder.staffName.setText(contactsBean.getName());
        holder.position.setText(contactsBean.getJob());
        Glide.with(mContext).load(contactsBean.getIco())
                .placeholder(mContext.getDrawable(R.drawable.ic_default_head))
                .error(mContext.getDrawable(R.drawable.ic_default_head))
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(holder.headPhoto);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContactListener.onClickItem(contactsBean, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @ViewInject(R.id.tv_name)
        private TextView staffName;
        @ViewInject(R.id.tv_position)
        private TextView position;
        @ViewInject(R.id.iv_head_photo)
        ImageView headPhoto;

        public ViewHolder(View itemView) {
            super(itemView);
            ViewBind.inject(this, itemView);
        }
    }

    public interface OnClickContactListener {
        /**
         * 查看详情
         *
         * @param contact
         * @param position
         */
        void onClickItem(ContactsBean contact, int position);
    }

    private OnClickContactListener mContactListener;

    public void setContactListener(OnClickContactListener contactListener) {
        mContactListener = contactListener;
    }
}
