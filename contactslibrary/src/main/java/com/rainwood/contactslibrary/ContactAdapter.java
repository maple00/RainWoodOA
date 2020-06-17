package com.rainwood.contactslibrary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

/**
 * Created by zhangxutong .
 * Date: 16/08/28
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    private List<ContactsBean> mDatas;
    private Context mContext;

    public void setDatas(List<ContactsBean> datas) {
        mDatas = datas;
        notifyDataSetChanged();
    }

    @Override
    public ContactAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ContactAdapter.ViewHolder holder, final int position) {
        final ContactsBean contactsBean = mDatas.get(position);
        holder.staffName.setText(contactsBean.getName());
        holder.position.setText(contactsBean.getJob());
        holder.telTv.setText(contactsBean.getTel());
        holder.qqTv.setText(contactsBean.getQq());
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
        holder.telLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContactListener.onClickTel(contactsBean, position);
            }
        });
        holder.qqLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContactListener.onClickQq(contactsBean, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCity, staffName, position, telTv, qqTv;
        ImageView headPhoto;
        LinearLayout telLL, qqLL;

        public ViewHolder(View itemView) {
            super(itemView);
            tvCity = itemView.findViewById(R.id.tvCity);
            headPhoto = itemView.findViewById(R.id.iv_head_photo);
            staffName = itemView.findViewById(R.id.tv_name);
            position = itemView.findViewById(R.id.tv_position);
            telTv = itemView.findViewById(R.id.tv_tel);
            qqTv = itemView.findViewById(R.id.tv_qq);
            telLL = itemView.findViewById(R.id.ll_tel);
            qqLL = itemView.findViewById(R.id.ll_qq);
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

        /**
         * 拨打电话
         *
         * @param contact
         * @param position
         */
        void onClickTel(ContactsBean contact, int position);

        /**
         * 复制QQ号
         *
         * @param contact
         * @param position
         */
        void onClickQq(ContactsBean contact, int position);
    }

    private OnClickContactListener mContactListener;

    public void setContactListener(OnClickContactListener contactListener) {
        mContactListener = contactListener;
    }
}
