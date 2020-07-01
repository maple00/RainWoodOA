package com.rainwood.oa.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.PictureDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.IconAndFont;
import com.rainwood.oa.ui.activity.AccountFundsActivity;
import com.rainwood.oa.ui.activity.AddressBookActivity;
import com.rainwood.oa.ui.activity.AdminPunishActivity;
import com.rainwood.oa.ui.activity.AttachManagerActivity;
import com.rainwood.oa.ui.activity.AttendanceActivity;
import com.rainwood.oa.ui.activity.BalanceCurveActivity;
import com.rainwood.oa.ui.activity.BalanceRecordActivity;
import com.rainwood.oa.ui.activity.BillingDataActivity;
import com.rainwood.oa.ui.activity.ClassificationStaticsActivity;
import com.rainwood.oa.ui.activity.CommonActivity;
import com.rainwood.oa.ui.activity.CustomFollowRecordActivity;
import com.rainwood.oa.ui.activity.CustomIntroduceActivity;
import com.rainwood.oa.ui.activity.CustomListActivity;
import com.rainwood.oa.ui.activity.CustomNewActivity;
import com.rainwood.oa.ui.activity.CustomOrderListActivity;
import com.rainwood.oa.ui.activity.DepartManagerActivity;
import com.rainwood.oa.ui.activity.ExchangeSkillActivity;
import com.rainwood.oa.ui.activity.FollowRecordActivity;
import com.rainwood.oa.ui.activity.HelperActivity;
import com.rainwood.oa.ui.activity.InvoiceRecordActivity;
import com.rainwood.oa.ui.activity.LogcatActivity;
import com.rainwood.oa.ui.activity.ManagerSystemActivity;
import com.rainwood.oa.ui.activity.MineAttendanceActivity;
import com.rainwood.oa.ui.activity.MineInvoiceRecordActivity;
import com.rainwood.oa.ui.activity.MineRecordsActivity;
import com.rainwood.oa.ui.activity.MineReissueCardActivity;
import com.rainwood.oa.ui.activity.OfficeFileActivity;
import com.rainwood.oa.ui.activity.OrderListActivity;
import com.rainwood.oa.ui.activity.OrderNewActivity;
import com.rainwood.oa.ui.activity.PostManagerActivity;
import com.rainwood.oa.ui.activity.RecordManagerActivity;
import com.rainwood.oa.ui.activity.ReimbursementActivity;
import com.rainwood.oa.ui.activity.RoleManagerActivity;
import com.rainwood.oa.ui.activity.StaffCurveActivity;
import com.rainwood.oa.ui.activity.StaffManagerActivity;
import com.rainwood.oa.ui.activity.WorkDayActivity;
import com.rainwood.oa.ui.widget.svgcode.SvgSoftwareLayerSetter;
import com.rainwood.oa.utils.Constants;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

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
        notifyDataSetChanged();
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
        if (TextUtils.isEmpty(getItem(position).getIco())) {
            Glide.with(convertView).load(mList.get(position).getLocalMipmap())
                    .placeholder(R.mipmap.ic_logo)
                    .error(R.mipmap.ic_logo)
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .into(holder.moduleImg);
        } else {
            Glide.with(convertView).as(PictureDrawable.class)
                    .listener(new SvgSoftwareLayerSetter())
                    .load(getItem(position).getIco())
                    .error(R.mipmap.ic_logo)
                    .placeholder(R.mipmap.ic_logo).
                    into(holder.moduleImg);
        }
        holder.moduleName.setText(getItem(position).getName());
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
        holder.moduleItem.setOnClickListener(v -> {
            switch (getItem(position).getName()) {
                case "我的考勤":
                    if ("myworkSign".equals(getItem(position).getMenu())) {
                        convertView.getContext().startActivity(getNewIntent(parent.getContext(), MineAttendanceActivity.class, "我的考勤", "myworkSign"));
                    }
                    break;
                // 客户管理模块
                case "新建客户":
                    convertView.getContext().startActivity(getNewIntent(parent.getContext(), CustomNewActivity.class, "新建客户", "新建客户"));
                    break;
                case "客户附件":
                    convertView.getContext().startActivity(getNewIntent(parent.getContext(), CommonActivity.class, "客户附件", "客户附件"));
                    break;
                case "介绍客户":
                    convertView.getContext().startActivity(getNewIntent(parent.getContext(), CustomIntroduceActivity.class, "介绍客户", "介绍客户"));
                    break;
                case "客户列表":
                    convertView.getContext().startActivity(getNewIntent(parent.getContext(), CustomListActivity.class, "客户列表", "客户列表"));
                    break;
                case "跟进记录":
                    if (Constants.CUSTOM_ID != null) {
                        convertView.getContext().startActivity(getNewIntent(parent.getContext(), CustomFollowRecordActivity.class, "跟进记录", "客户跟进记录"));
                    } else {
                        convertView.getContext().startActivity(getNewIntent(parent.getContext(), FollowRecordActivity.class, "跟进记录", "知识管理跟进记录"));
                    }
                    break;
                case "开票信息":
                    convertView.getContext().startActivity(getNewIntent(parent.getContext(), BillingDataActivity.class, "开票信息", "开票信息"));
                    break;
                case "开票记录":
                    if ("mykehuInvoice".equals(getItem(position).getMenu())) {
                        convertView.getContext().startActivity(getNewIntent(parent.getContext(), MineInvoiceRecordActivity.class, "我的开票记录", "mykehuInvoice"));
                    } else {
                        convertView.getContext().startActivity(getNewIntent(parent.getContext(), InvoiceRecordActivity.class, "开票记录", "客户开票记录"));
                    }
                    break;
                // 订单模块
                case "新建订单":
                    convertView.getContext().startActivity(getNewIntent(parent.getContext(), OrderNewActivity.class, "新建订单", "新建订单"));
                    break;
                case "订单列表":
                    if (Constants.CUSTOM_ID != null) {
                        convertView.getContext().startActivity(getNewIntent(parent.getContext(), CustomOrderListActivity.class, "订单列表", "客户订单列表"));
                    } else {
                        convertView.getContext().startActivity(getNewIntent(parent.getContext(), OrderListActivity.class, "订单列表", "订单列表"));
                    }
                    break;
                case "业务技能":
                    convertView.getContext().startActivity(getNewIntent(parent.getContext(), ExchangeSkillActivity.class, "沟通技巧", "沟通技巧"));
                    break;
                // 行政人事
                case "角色管理":
                    convertView.getContext().startActivity(getNewIntent(parent.getContext(), RoleManagerActivity.class, "角色管理", "角色管理"));
                    break;
                case "部门管理":
                    convertView.getContext().startActivity(getNewIntent(parent.getContext(), DepartManagerActivity.class, "部门管理", "部门管理"));
                    break;
                case "职位管理":
                    convertView.getContext().startActivity(getNewIntent(parent.getContext(), PostManagerActivity.class, "职位管理", "职位管理"));
                    break;
                case "员工管理":
                    convertView.getContext().startActivity(getNewIntent(parent.getContext(), StaffManagerActivity.class, "员工管理", "员工管理"));
                    break;
                case "工作日":
                    convertView.getContext().startActivity(getNewIntent(parent.getContext(), WorkDayActivity.class, "工作日", "工作日"));
                    break;
                case "通讯录":
                    convertView.getContext().startActivity(getNewIntent(parent.getContext(), AddressBookActivity.class, "通讯录", "通讯录"));
                    break;
                case "管理制度":
                    convertView.getContext().startActivity(getNewIntent(parent.getContext(), ManagerSystemActivity.class, "管理制度", "管理制度"));
                    break;
                case "加班记录":
                    if ("myworkAdd".equals(getItem(position).getMenu())) {
                        convertView.getContext().startActivity(getNewIntent(parent.getContext(), MineRecordsActivity.class, "我的加班记录", "myworkAdd"));
                    } else {
                        convertView.getContext().startActivity(getNewIntent(parent.getContext(), RecordManagerActivity.class, "加班记录", "加班记录"));
                    }
                    break;
                case "请假记录":
                    if ("mywork".equals(getItem(position).getMenu())) {
                        convertView.getContext().startActivity(getNewIntent(parent.getContext(), MineRecordsActivity.class, "我的请假记录", "mywork"));
                    } else {
                        convertView.getContext().startActivity(getNewIntent(parent.getContext(), RecordManagerActivity.class, "请假记录", "请假记录"));
                    }
                    break;
                case "外出记录":
                    if ("myworkOut".equals(getItem(position).getMenu())) {
                        convertView.getContext().startActivity(getNewIntent(parent.getContext(), MineRecordsActivity.class, "我的外出记录", "myworkOut"));
                    } else {
                        convertView.getContext().startActivity(getNewIntent(parent.getContext(), RecordManagerActivity.class, "外出记录", "外出记录"));
                    }
                    break;
                case "补卡单":
                    if ("myworkSignAdd".equals(getItem(position).getMenu())) {
                        convertView.getContext().startActivity(getNewIntent(parent.getContext(), MineReissueCardActivity.class, "我的补卡记录", "补卡记录"));
                    } else {
                        convertView.getContext().startActivity(getNewIntent(parent.getContext(), RecordManagerActivity.class, "补卡记录", "补卡记录"));
                    }
                    break;
                case "回款记录":
                    convertView.getContext().startActivity(getNewIntent(parent.getContext(), RecordManagerActivity.class, "回款记录", "回款记录"));
                    break;
                case "考勤记录":
                    convertView.getContext().startActivity(getNewIntent(parent.getContext(), AttendanceActivity.class, "考勤记录", "考勤记录"));
                    break;
                case "行政处罚":
                    convertView.getContext().startActivity(getNewIntent(parent.getContext(), AdminPunishActivity.class, "行政处罚", "行政处罚"));
                    break;
                // 财务管理
                case "费用报销":
                    if ("mycost".equals(getItem(position).getMenu())) {
                        convertView.getContext().startActivity(getNewIntent(parent.getContext(), ReimbursementActivity.class, "我的费用报销", "mycost"));
                    } else {
                        convertView.getContext().startActivity(getNewIntent(parent.getContext(), ReimbursementActivity.class, "费用报销", "reimbursement"));
                    }
                    break;
                case "团队基金":
                    convertView.getContext().startActivity(getNewIntent(parent.getContext(), AccountFundsActivity.class, "团队基金", "teamFunds"));
                    break;
                case "收支平衡": // 收支记录列表
                    convertView.getContext().startActivity(getNewIntent(parent.getContext(), BalanceRecordActivity.class, "收支记录", "balanceRecord"));
                    break;
                case "分类统计":
                    convertView.getContext().startActivity(getNewIntent(parent.getContext(), ClassificationStaticsActivity.class, "分类统计", "classificationStatics"));
                    break;
                case "收支曲线":
                    convertView.getContext().startActivity(getNewIntent(parent.getContext(), BalanceCurveActivity.class, "收支曲线", "balanceCurve"));
                    break;
                case "员工数曲线":
                    convertView.getContext().startActivity(getNewIntent(parent.getContext(), StaffCurveActivity.class, "员工曲线数", "staffCurve"));
                    break;
                // 知识管理
                case "办公文件":
                    convertView.getContext().startActivity(getNewIntent(parent.getContext(), OfficeFileActivity.class, "办公文件", "办公文件"));
                    break;
                case "附件管理":
                    convertView.getContext().startActivity(getNewIntent(parent.getContext(), AttachManagerActivity.class, "附件管理", "附件管理"));
                    break;
                // 系统设置
                case "系统日志":
                    convertView.getContext().startActivity(getNewIntent(parent.getContext(), LogcatActivity.class, "系统日志", "系统日志"));
                    break;
                case "帮助中心":
                    convertView.getContext().startActivity(getNewIntent(parent.getContext(), HelperActivity.class, "帮助中心", "帮助中心"));
                    break;
            }
        });
    }

    /**
     * startActivity 优化
     */
    private Intent getNewIntent(Context context, Class<? extends BaseActivity> clazz, String title, String menu) {
        Intent intent = new Intent(context, clazz);
        intent.putExtra("title", title);
        intent.putExtra("menu", menu);
        return intent;
    }
}
