package com.rainwood.oa.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.FontAndFont;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/4/28 16:36
 * @Desc: 我的--账户信息
 */
public final class MineAccountAdapter extends BaseAdapter {

    private List<FontAndFont> mList;

    public void setList(List<FontAndFont> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public FontAndFont getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mine_account, parent, false);
            ViewBind.inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.verticalLine.setVisibility(position % 2 == 0 ? View.VISIBLE : View.GONE);
        holder.title.setText(getItem(position).getTitle());
        holder.content.setText(getItem(position).getDesc());
        // 点击事件
        holder.itemPersonalAccount.setOnClickListener(v -> mPersonalAccount.onClickItem(getItem(position), position));
        return convertView;
    }


    private static class ViewHolder {
        @ViewInject(R.id.rl_item_personal_account)
        private RelativeLayout itemPersonalAccount;
        @ViewInject(R.id.tv_title)
        private TextView title;
        @ViewInject(R.id.tv_content)
        private TextView content;
        @ViewInject(R.id.v_vertical_line)
        private View verticalLine;
    }

    public interface OnClickPersonalAccount{

        /**
         * 查看详情
         * @param item
         * @param position
         */
        void  onClickItem(FontAndFont item, int position);
    }

    private OnClickPersonalAccount mPersonalAccount;

    public void setPersonalAccount(OnClickPersonalAccount personalAccount) {
        mPersonalAccount = personalAccount;
    }
}
