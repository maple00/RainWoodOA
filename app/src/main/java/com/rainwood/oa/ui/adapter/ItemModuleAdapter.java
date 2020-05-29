package com.rainwood.oa.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.IconAndFont;
import com.rainwood.oa.ui.activity.AdminPunishActivity;
import com.rainwood.oa.ui.activity.AttendanceActivity;
import com.rainwood.oa.ui.activity.CommonActivity;
import com.rainwood.oa.ui.activity.CustomIntroduceActivity;
import com.rainwood.oa.ui.activity.CustomListActivity;
import com.rainwood.oa.ui.activity.CustomNewActivity;
import com.rainwood.oa.ui.activity.CustomOrderListActivity;
import com.rainwood.oa.ui.activity.DepartManagerActivity;
import com.rainwood.oa.ui.activity.ExchangeSkillActivity;
import com.rainwood.oa.ui.activity.FollowRecordActivity;
import com.rainwood.oa.ui.activity.LogcatActivity;
import com.rainwood.oa.ui.activity.ManagerSystemActivity;
import com.rainwood.oa.ui.activity.OrderListActivity;
import com.rainwood.oa.ui.activity.OrderNewActivity;
import com.rainwood.oa.ui.activity.PostManagerActivity;
import com.rainwood.oa.ui.activity.RecordManagerActivity;
import com.rainwood.oa.ui.activity.RoleManagerActivity;
import com.rainwood.oa.ui.activity.StaffManagerActivity;
import com.rainwood.oa.utils.Constants;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.toast.ToastUtils;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/4/28 11:00
 * @Desc: 管理界面子项模块
 */
public final class ItemModuleAdapter extends BaseAdapter {

    private List<IconAndFont> mList;

    public void setList(List<IconAndFont> list) {
        mList = list;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public IconAndFont getItem(int position) {
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_manager_module, parent, false);
            ViewBind.inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Glide.with(convertView).load(getItem(position).getLocalMipmap())
                .error(R.drawable.bg_monkey_king)
                .placeholder(R.drawable.bg_monkey_king)
                //.apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(holder.moduleImg);
        holder.moduleName.setText(getItem(position).getDesc());
        // 点击事件
        onItemClickValues(position, holder, convertView, parent);
        return convertView;
    }

    private static class ViewHolder {
        @ViewInject(R.id.iv_module_img)
        private ImageView moduleImg;
        @ViewInject(R.id.tv_module_name)
        private TextView moduleName;
        @ViewInject(R.id.ll_module_item)
        private LinearLayout moduleItem;
    }

    /**
     * 点击不同模块的点击事件
     *
     * @param position 点击的模块
     * @param holder   ViewHolder
     */
    private void onItemClickValues(int position, ViewHolder holder, View convertView, ViewGroup parent) {
        //setContext(convertView.getContext());
        holder.moduleItem.setOnClickListener(v -> {
            try {
                //ToastUtils.show("点击了-- " + position + " -- module ---- " + getItem(position).getDesc());
            } catch (Exception e) {
                // ToastUtils.show("点击了-- " + position);
            }
            switch (getItem(position).getDesc()) {
                case "我的考勤":
                    convertView.getContext().startActivity(getNewIntent(parent.getContext(), AttendanceActivity.class, "我的考勤"));
                    break;
                case "我的补卡记录":
                    break;
                // 客户管理模块
                case "新建客户":
                    convertView.getContext().startActivity(getNewIntent(parent.getContext(), CustomNewActivity.class, "新建客户"));
                    break;
                case "客户附件":
                    convertView.getContext().startActivity(getNewIntent(parent.getContext(), CommonActivity.class, "客户附件"));
                    break;
                case "介绍客户":
                    convertView.getContext().startActivity(getNewIntent(parent.getContext(), CustomIntroduceActivity.class, "介绍客户"));
                    break;
                case "客户列表":
                    convertView.getContext().startActivity(getNewIntent(parent.getContext(), CustomListActivity.class, "客户列表"));
                    break;
                case "跟进记录":
                    convertView.getContext().startActivity(getNewIntent(parent.getContext(), FollowRecordActivity.class, "跟进记录"));
                    break;
                // 订单模块
                case "新建订单":
                    convertView.getContext().startActivity(getNewIntent(parent.getContext(), OrderNewActivity.class, "新建订单"));
                    break;
                case "订单列表":
                    LogUtils.d("sxs", "customId----" + Constants.CUSTOM_ID);
                    if (Constants.CUSTOM_ID != null){
                        convertView.getContext().startActivity(getNewIntent(parent.getContext(), CustomOrderListActivity.class, "订单列表"));
                    }else {
                        convertView.getContext().startActivity(getNewIntent(parent.getContext(), OrderListActivity.class, "订单列表"));
                    }
                    break;
                case "沟通技巧":
                    convertView.getContext().startActivity(getNewIntent(parent.getContext(), ExchangeSkillActivity.class, "沟通技巧"));
                    break;
                // 行政人事
                case "角色管理":
                    convertView.getContext().startActivity(getNewIntent(parent.getContext(), RoleManagerActivity.class, "角色管理"));
                    break;
                case "部门管理":
                    convertView.getContext().startActivity(getNewIntent(parent.getContext(), DepartManagerActivity.class, "部门管理"));
                    break;
                case "职位管理":
                    convertView.getContext().startActivity(getNewIntent(parent.getContext(), PostManagerActivity.class, "职位管理"));
                    break;
                case "员工管理":
                    convertView.getContext().startActivity(getNewIntent(parent.getContext(), StaffManagerActivity.class, "员工管理"));
                    break;
                case "工作日":
                    ToastUtils.show("工作日");
                    break;
                case "通讯录":
                    ToastUtils.show("通讯录");
                    break;
                case "管理制度":
                    convertView.getContext().startActivity(getNewIntent(parent.getContext(), ManagerSystemActivity.class, "管理制度"));
                    break;
                case "加班记录":
                    convertView.getContext().startActivity(getNewIntent(parent.getContext(), RecordManagerActivity.class, "加班记录"));
                    break;
                case "请假记录":
                    convertView.getContext().startActivity(getNewIntent(parent.getContext(), RecordManagerActivity.class, "请假记录"));
                    break;
                case "外出记录":
                    convertView.getContext().startActivity(getNewIntent(parent.getContext(), RecordManagerActivity.class, "外出记录"));
                    break;
                case "补卡记录":
                    convertView.getContext().startActivity(getNewIntent(parent.getContext(), RecordManagerActivity.class, "补卡记录"));
                    break;
                case "回款记录":
                    convertView.getContext().startActivity(getNewIntent(parent.getContext(), RecordManagerActivity.class, "回款记录"));
                    break;
                case "考勤记录":
                    ToastUtils.show("考勤记录");
                    break;
                case "行政处罚":
                    convertView.getContext().startActivity(getNewIntent(parent.getContext(), AdminPunishActivity.class, "行政处罚"));
                    break;
                // 系统设置
                case "系统日志":
                    convertView.getContext().startActivity(getNewIntent(parent.getContext(), LogcatActivity.class, "系统日志"));
                    break;
            }
        });
    }


    /**
     * startActivity 优化
     */
    private Intent getNewIntent(Context context, Class<? extends BaseActivity> clazz, String title) {
        Intent intent = new Intent(context, clazz);
        intent.putExtra("title", title);
        return intent;
    }

}
