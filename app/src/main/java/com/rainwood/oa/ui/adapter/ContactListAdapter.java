package com.rainwood.oa.ui.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.Contact;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.toast.ToastUtils;
import com.rainwood.tools.wheel.aop.SingleClick;

import java.util.List;

/**
 * create by a797s in 2020/5/15 15:32
 *
 * @Description : 联系人列表
 * @Usage :
 **/
public final class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolder> {

    private List<Contact> mList;

    public void setList(List<Contact> list) {
        mList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(mList.get(position).getName());
        holder.post.setText(mList.get(position).getPost());
        holder.telNumber.setText(mList.get(position).getTelNum());
        // 非必填项
        if (TextUtils.isEmpty(mList.get(position).getSpecialPlane())) {
            holder.planeRL.setVisibility(View.GONE);
        } else {
            holder.specialPlane.setText(mList.get(position).getSpecialPlane());
        }
        if (TextUtils.isEmpty(mList.get(position).getWxNum())) {
            holder.wxRl.setVisibility(View.GONE);
        } else {
            holder.wxNumber.setText(mList.get(position).getWxNum());
        }
        if (TextUtils.isEmpty(mList.get(position).getQqNum())) {
            holder.qqRl.setVisibility(View.GONE);
        } else {
            holder.qqNumber.setText(mList.get(position).getQqNum());
        }
        if (TextUtils.isEmpty(mList.get(position).getNote())) {
            holder.note.setVisibility(View.GONE);
        } else {
            holder.note.setText(mList.get(position).getNote());
        }
        // 点击事件
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @SingleClick
            @Override
            public void onClick(View v) {
                ToastUtils.show("点击编辑");
            }
        });
        holder.playPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.show("拨打电话");
            }
        });
        holder.playPlane.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.show("拨打座机");
            }
        });
        holder.copyWXNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.show("复制微信");
            }
        });
        holder.copyQQNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.show("复制QQ");
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @ViewInject(R.id.tv_name)
        private TextView name;
        @ViewInject(R.id.tv_post)
        private TextView post;
        @ViewInject(R.id.tv_edit)
        private TextView edit;
        @ViewInject(R.id.tv_tel_number)
        private TextView telNumber;
        @ViewInject(R.id.iv_play_phone)
        private ImageView playPhone;
        @ViewInject(R.id.tv_special_plane)
        private TextView specialPlane;
        @ViewInject(R.id.iv_play_special_plane)
        private ImageView playPlane;
        @ViewInject(R.id.tv_wx_number)
        private TextView wxNumber;
        @ViewInject(R.id.tv_copy_wx_num)
        private TextView copyWXNum;
        @ViewInject(R.id.tv_qq_number)
        private TextView qqNumber;
        @ViewInject(R.id.tv_copy_qq_num)
        private TextView copyQQNum;
        @ViewInject(R.id.tv_note)
        private TextView note;

        @ViewInject(R.id.rl_special_plane)
        private RelativeLayout planeRL;
        @ViewInject(R.id.rl_wx)
        private RelativeLayout wxRl;
        @ViewInject(R.id.rl_qq)
        private RelativeLayout qqRl;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ViewBind.inject(this, itemView);
        }
    }
}
