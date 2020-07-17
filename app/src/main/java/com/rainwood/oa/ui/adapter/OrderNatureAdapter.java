package com.rainwood.oa.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.FontAndFont;
import com.rainwood.oa.model.domain.OrderStatics;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/20 11:04
 * @Desc: 订单的属性
 */
public final class OrderNatureAdapter extends RecyclerView.Adapter<OrderNatureAdapter.ViewHolder> {

    private List<FontAndFont> mList;

    public void setList(List<FontAndFont> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_nature, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(mList.get(position).getTitle());
        holder.value.setText(mList.get(position).getDesc());
        holder.orderNature.setOnClickListener(v -> mOnClickItemListener.onLickItem());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return ListUtils.getSize(mList);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        @ViewInject(R.id.tv_title)
        private TextView title;
        @ViewInject(R.id.tv_value)
        private TextView value;
        @ViewInject(R.id.ll_order_nature)
        private LinearLayout orderNature;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ViewBind.inject(this,itemView);
        }
    }

    public interface OnClickItemListener{

        void onLickItem();
    }

    private OnClickItemListener mOnClickItemListener;

    public void setOnClickItemListener(OnClickItemListener onClickItemListener) {
        mOnClickItemListener = onClickItemListener;
    }
}
