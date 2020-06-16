package com.rainwood.contactslibrary;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Created by zhangxutong .
 * Date: 16/08/28
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    private List<ContactsBean> mDatas;

    public void setDatas(List<ContactsBean> datas) {
        mDatas = datas;
    }

    @Override
    public ContactAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ContactAdapter.ViewHolder holder, final int position) {
        final ContactsBean contactsBean = mDatas.get(position);
        holder.tvCity.setText(contactsBean.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("sxs-- ContactAdapter---", "pos:" + position);
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
}
