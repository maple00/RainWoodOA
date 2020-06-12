package com.rainwood.oa.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.TeamFunds;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/5 10:36
 * @Desc: 团队基金 adapter
 */
public final class TeamFundsAdapter extends RecyclerView.Adapter<TeamFundsAdapter.ViewHolder> {

    private List<TeamFunds> mList;
    private Context mContext;

    public void setList(List<TeamFunds> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_team_funds, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.content.setText(mList.get(position).getText());
        holder.time.setText(mList.get(position).getTime());
        holder.money.setText("收入".equals(mList.get(position).getDirection())
                ? "+" + mList.get(position).getMoney()
                : "-" + mList.get(position).getMoney());
        holder.money.setTextColor("收入".equals(mList.get(position).getDirection())
                ? mContext.getColor(R.color.colorPrimary) : mContext.getColor(R.color.fontColor));
        // 点击事件
    }

    @Override
    public int getItemCount() {
        return ListUtils.getSize(mList);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @ViewInject(R.id.ll_item_team)
        private LinearLayout itemTeam;
        @ViewInject(R.id.tv_money)
        private TextView money;
        @ViewInject(R.id.tv_content)
        private TextView content;
        @ViewInject(R.id.tv_time)
        private TextView time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ViewBind.inject(this, itemView);
        }
    }
}
