package com.rainwood.oa.ui.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.ReimbursementData;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/4 14:05
 * @Desc: 费用报销adapter
 */
public final class ReimbursementAdapter extends RecyclerView.Adapter<ReimbursementAdapter.ViewHolder> {

    private List<ReimbursementData> mList;
    private Context mContext;

    public void setList(List<ReimbursementData> list) {
        mList = list;
        notifyDataSetChanged();
    }

    /**
     * 追加一些数据
     */
    public void addData(List<ReimbursementData> data) {
        if (data == null || data.size() == 0) {
            return;
        }

        if (mList == null || mList.size() == 0) {
            setList(data);
        } else {
            mList.addAll(data);
            notifyItemRangeInserted(mList.size() - data.size(), data.size());
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_reimbursement, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.type.setText(mList.get(position).getType());
        holder.money.setText(mList.get(position).getMoney());
        holder.voucher.setVisibility("是".equals(mList.get(position).getPay()) ? View.VISIBLE : View.GONE);
        holder.nameContent.setText(Html.fromHtml("<font color='" + mContext.getColor(R.color.fontColor) + "'> "
                + mList.get(position).getStaffName() + "</font>" +
                "<font color='" + mContext.getColor(R.color.labelColor) + "'> | " + mList.get(position).getText() + "</font>"));
        holder.allocatedChecked.setImageDrawable("是".equals(mList.get(position).getPay())
                ? mContext.getDrawable(R.drawable.ic_reimburse_checked)
                : mContext.getDrawable(R.drawable.ic_reimburse_unchecked));
        holder.reimburse.setText("是".equals(mList.get(position).getPay()) ? "已拨付" : "未拨付");
        holder.time.setText(mList.get(position).getPayDate());
        // 点击事件
        holder.itemReimburse.setOnClickListener(v -> {
            // 查看详情
            mOnClickReimburse.onClickItem(mList.get(position), position);
        });
    }

    @Override
    public int getItemCount() {
        return ListUtils.getSize(mList);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @ViewInject(R.id.ll_item_reimburse)
        private LinearLayout itemReimburse;
        @ViewInject(R.id.tv_type)
        private TextView type;
        @ViewInject(R.id.iv_voucher)
        private ImageView voucher;
        @ViewInject(R.id.tv_money)
        private TextView money;
        @ViewInject(R.id.tv_name_content)
        private TextView nameContent;
        @ViewInject(R.id.iv_reimburse_checked)
        private ImageView allocatedChecked;
        @ViewInject(R.id.tv_reimburse)
        private TextView reimburse;
        @ViewInject(R.id.tv_time)
        private TextView time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ViewBind.inject(this, itemView);
        }
    }

    public interface OnClickReimburse {
        /**
         * 查看详情
         *
         * @param reimbursementData
         * @param position
         */
        void onClickItem(ReimbursementData reimbursementData, int position);
    }

    private OnClickReimburse mOnClickReimburse;

    public void setOnClickReimburse(OnClickReimburse onClickReimburse) {
        mOnClickReimburse = onClickReimburse;
    }
}
