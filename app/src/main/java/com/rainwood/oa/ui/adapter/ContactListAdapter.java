package com.rainwood.oa.ui.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.Contact;
import com.rainwood.oa.utils.ListUtils;
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
    // 控制checkBox显示, 默认不显示
    private boolean checkShow = false;

    private int selectedCount = 0;

    public void setList(List<Contact> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public void setCheckShow(boolean checkShow) {
        this.checkShow = checkShow;
        // 设置全选与全不选数量
        selectedCount = (checkShow ? ListUtils.getSize(mList) : 0);
        notifyDataSetChanged();
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
        holder.post.setText(mList.get(position).getPosition());
        holder.telNumber.setText(mList.get(position).getTel());
        // 控制显示
        holder.mCheckBox.setVisibility(checkShow ? View.VISIBLE : View.GONE);
        // 控制选择
        holder.mCheckBox.setChecked(mList.get(position).isSelected());
        // 非必填项
        if (TextUtils.isEmpty(mList.get(position).getPhone())) {
            holder.planeRL.setVisibility(View.GONE);
        } else {
            holder.specialPlane.setText(mList.get(position).getPhone());
        }
        if (TextUtils.isEmpty(mList.get(position).getWeChat())) {
            holder.wxRl.setVisibility(View.GONE);
        } else {
            holder.wxNumber.setText(mList.get(position).getWeChat());
        }
        if (TextUtils.isEmpty(mList.get(position).getQq())) {
            holder.qqRl.setVisibility(View.GONE);
        } else {
            holder.qqNumber.setText(mList.get(position).getQq());
        }
        if (TextUtils.isEmpty(mList.get(position).getText())) {
            holder.note.setVisibility(View.GONE);
        } else {
            holder.note.setText(mList.get(position).getText());
        }
        // 点击事件
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @SingleClick
            @Override
            public void onClick(View v) {
               mOnClickSelected.editContactData(mList.get(position));
            }
        });
        holder.playPhone.setOnClickListener(v -> {
            // 拨打手机号
            mOnClickSelected.playPhone(mList.get(position).getTel());
        });
        holder.playPlane.setOnClickListener(v -> {
            // 拨打座机号
            mOnClickSelected.playPhone(mList.get(position).getPhone());
        });
        holder.copyWXNum.setOnClickListener(v -> {
            //ToastUtils.show("复制微信");
            mOnClickSelected.copyWxNum2Board(mList.get(position).getWeChat());
        });
        holder.copyQQNum.setOnClickListener(v -> {
            //ToastUtils.show("复制QQ");
            mOnClickSelected.copyQQNum2Board(mList.get(position).getQq());
        });
        // 单选选中回调
        holder.mCheckBox.setOnClickListener(v -> {
            boolean hasChecked = holder.mCheckBox.isChecked();
            if (hasChecked) {
                selectedCount++;
            } else {
                selectedCount--;
            }
            mOnClickSelected.onSelectedSwitch(hasChecked, position, selectedCount);
        });
    }

    public interface OnClickSelected {
        /**
         * @param position      被选中的item
         * @param selectedCount 被选中的总数
         * @param status        被选择的状态
         */
        void onSelectedSwitch(boolean status, int position, int selectedCount);

        /**
         * 复制微信号
         *
         * @param content 需要复制的内容
         */
        void copyWxNum2Board(String content);

        /**
         * 复制QQ号
         *
         * @param content
         */
        void copyQQNum2Board(String content);

        /**
         * 拨打号码
         *
         * @param tel
         */
        void playPhone(String tel);

        /**
         * 编辑
         */
        void editContactData(Contact contact);
    }

    private OnClickSelected mOnClickSelected;

    public void setOnClickSelected(OnClickSelected onClickSelected) {
        mOnClickSelected = onClickSelected;
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @ViewInject(R.id.cb_checked)
        private CheckBox mCheckBox;
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
