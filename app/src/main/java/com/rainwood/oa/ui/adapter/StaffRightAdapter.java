package com.rainwood.oa.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.Staff;
import com.rainwood.oa.ui.widget.GroupIconText;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.utils.FontSwitchUtil;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/22 14:17
 * @Desc: 员工管理 -- 右边员工列表
 */
public final class StaffRightAdapter extends RecyclerView.Adapter<StaffRightAdapter.ViewHolder> {

    private List<Staff> mStaffList;
    private Context mContext;

    public void setStaffList(List<Staff> staffList) {
        mStaffList = staffList;
        notifyDataSetChanged();
    }

    /**
     * 追加一些数据
     */
    public void addData(List<Staff> data) {
        if (data == null || data.size() == 0) {
            return;
        }

        if (mStaffList == null || mStaffList.size() == 0) {
            setStaffList(data);
        } else {
            mStaffList.addAll(data);
            notifyItemRangeInserted(mStaffList.size() - data.size(), data.size());
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_staff_right, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(mContext).load(mStaffList.get(position).getIco())
                .placeholder(mContext.getDrawable(R.drawable.ic_default_head))
                .error(mContext.getDrawable(R.drawable.ic_default_head))
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(holder.headPhoto);
        holder.staff.setText(mStaffList.get(position).getName());

        holder.salary.setText(Html.fromHtml("<font color=\"" + mContext.getColor(R.color.fontColor)
                + "\" size=\"" + FontSwitchUtil.dip2px(mContext, 10f) + "\">基</font>"
                + "<font color=\"" + mContext.getColor(R.color.fontColor)
                + "\" size=\"" + FontSwitchUtil.dip2px(mContext, 13f) + "\">"
                + mStaffList.get(position).getBasePay() + "+</font>"
                + "<font color=\"" + mContext.getColor(R.color.colorPrimary)
                + "\" size=\"" + FontSwitchUtil.dip2px(mContext, 10f) + "\">津</font>"
                + "<font color=\"" + mContext.getColor(R.color.colorPrimary)
                + "\" size=\"" + FontSwitchUtil.dip2px(mContext, 13f) + "\">"
                + mStaffList.get(position).getSubsidy() + "</font>"));

        //holder.departPost.setText(mStaffList.get(position).getDepart() + "-" + mStaffList.get(position).getPost());
        holder.departPost.setText(mStaffList.get(position).getDepartment());
        holder.telNum.setValue(mStaffList.get(position).getTel());
        // 查看大图
        // holder.headPhoto.setOnClickListener(v -> FileManagerUtil.queryBigPicture(mContext, mStaffList.get(position).getIco()));
        // 点击事件
        holder.itemStaff.setOnClickListener(v -> mClickStaffRight.onClickStaff(mStaffList.get(position), position));
    }

    @Override
    public int getItemCount() {
        return ListUtils.getSize(mStaffList);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @ViewInject(R.id.rl_item_staff)
        private RelativeLayout itemStaff;
        @ViewInject(R.id.iv_head_photo)
        private ImageView headPhoto;
        @ViewInject(R.id.tv_staff)
        private TextView staff;
        @ViewInject(R.id.tv_salary)
        private TextView salary;
        @ViewInject(R.id.tv_depart_post)
        private TextView departPost;
        @ViewInject(R.id.git_tel_num)
        private GroupIconText telNum;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ViewBind.inject(this, itemView);
        }
    }

    public interface OnClickStaffRight {
        /**
         * 查看员工详情
         *
         * @param position
         */
        void onClickStaff(Staff staff, int position);
    }

    private OnClickStaffRight mClickStaffRight;

    public void setClickStaffRight(OnClickStaffRight clickStaffRight) {
        mClickStaffRight = clickStaffRight;
    }
}
