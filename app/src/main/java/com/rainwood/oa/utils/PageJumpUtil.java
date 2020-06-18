package com.rainwood.oa.utils;

import android.content.Context;
import android.content.Intent;

import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.Contact;
import com.rainwood.oa.model.domain.Logcat;
import com.rainwood.oa.model.domain.MineRecords;
import com.rainwood.oa.model.domain.TempData;

import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/5/18 18:21
 * @Desc: 页面跳转工具类
 */
public final class PageJumpUtil {

    private static Intent sIntent;

    /**
     * 部门列表跳转到部门详情页面
     *
     * @param context
     * @param clazz
     * @param departId
     */
    public static void departList2Detail(Context context, Class<? extends BaseActivity> clazz, String departId) {
        sIntent = new Intent(context, clazz);
        sIntent.putExtra("title", "部门详情");
        sIntent.putExtra("departId", departId);
        context.startActivity(sIntent);
    }

    /**
     * 职位列表跳转到详情页面
     *
     * @param context
     * @param clazz
     * @param postId
     */
    public static void postList2Detail(Context context, Class<? extends BaseActivity> clazz, String postId) {
        sIntent = new Intent(context, clazz);
        sIntent.putExtra("title", "职位详情");
        sIntent.putExtra("postId", postId);
        context.startActivity(sIntent);
    }

    /**
     * 员工管理列表中的员工跳转到员工详情
     *
     * @param context
     * @param clazz
     * @param staffId
     */
    public static void staffList2Detail(Context context, Class<? extends BaseActivity> clazz, String staffId) {
        sIntent = new Intent(context, clazz);
        sIntent.putExtra("title", "员工详情");
        sIntent.putExtra("staffId", staffId);
        context.startActivity(sIntent);
    }

    /**
     * 员工工作经历列表跳转到工作经历详情
     *
     * @param context
     * @param clazz
     * @param experienceId
     */
    public static void staffJobExperience2Detail(Context context, Class<? extends BaseActivity> clazz, String experienceId) {
        sIntent = new Intent(context, clazz);
        sIntent.putExtra("title", "工作经历");
        sIntent.putExtra("experienceId", experienceId);
        context.startActivity(sIntent);
    }

    /**
     * 员工账户跳转到详情页
     *
     * @param context
     * @param clazz
     * @param accountId
     */
    public static void staffAccount2Detail(Context context, Class<? extends BaseActivity> clazz, String accountId) {
        sIntent = new Intent(context, clazz);
        sIntent.putExtra("title", "收支详情");
        sIntent.putExtra("accountId", accountId);
        context.startActivity(sIntent);
    }

    /**
     * 客户列表跳转到客户详情页面
     *
     * @param context  con
     * @param clazz    跳转目的地
     * @param customId 客户id
     */
    public static void listJump2CustomDetail(Context context, Class<? extends BaseActivity> clazz, String customId) {
        sIntent = new Intent(context, clazz);
        sIntent.putExtra("title", "客户详情");
        sIntent.putExtra("customId", customId);
        context.startActivity(sIntent);
    }

    /**
     * 客户详情页查询联系人列表
     *
     * @param context  context
     * @param clazz    target clazz
     * @param customId 客户id
     */
    public static void customDetail2ContactList(Context context, Class<? extends BaseActivity> clazz, String customId) {
        sIntent = new Intent(context, clazz);
        sIntent.putExtra("title", "联系人");
        sIntent.putExtra("customId", customId);
        context.startActivity(sIntent);
    }

    /**
     * 跳转到联系人信息编辑
     *
     * @param context
     * @param clazz
     * @param contact
     */
    public static void contact2Edit(Context context, Class<? extends BaseActivity> clazz, Contact contact, String customId) {
        sIntent = new Intent(context, clazz);
        sIntent.putExtra("title", "编辑联系人");
        sIntent.putExtra("contact", contact);
        sIntent.putExtra("customId", customId);
        context.startActivity(sIntent);
    }

    /**
     * 回款记录列表跳转到汇款记录详情
     *
     * @param context
     * @param clazz
     */
    public static void receivable2Detail(Context context, Class<? extends BaseActivity> clazz, String receivableId) {
        sIntent = new Intent(context, clazz);
        sIntent.putExtra("title", "回款记录详情");
        sIntent.putExtra("receivableId", receivableId);
        context.startActivity(sIntent);
    }

    /**
     * 创建订单跳转到订单编辑页面
     *
     * @param context
     * @param clazz
     * @param tempData 页面跳转携带的数据
     */
    public static void orderNew2OrderEditPage(Context context, Class<? extends BaseActivity> clazz, TempData tempData) {
        sIntent = new Intent(context, clazz);
        sIntent.putExtra("title", "编辑订单");
        sIntent.putExtra("orderValues", tempData);
        context.startActivity(sIntent);
    }

    /**
     * 订单列表跳转到订单详情页面
     *
     * @param context
     * @param clazz
     * @param orderId
     */
    public static void orderList2Detail(Context context, Class<? extends BaseActivity> clazz, String orderId, String status) {
        sIntent = new Intent(context, clazz);
        sIntent.putExtra("title", "订单详情");
        sIntent.putExtra("orderId", orderId);
        sIntent.putExtra("status", status);
        context.startActivity(sIntent);
    }

    /**
     * 费用报销列表页跳转到详情页面
     *
     * @param context
     * @param clazz
     * @param reimburseId
     */
    public static void reimburseList2Detail(Context context, Class<? extends BaseActivity> clazz, String reimburseId) {
        sIntent = new Intent(context, clazz);
        sIntent.putExtra("title", "费用详情");
        sIntent.putExtra(reimburseId, reimburseId);
        context.startActivity(sIntent);
    }

    /**
     * 文章列表页面跳转到文章详情页面
     *
     * @param context
     * @param clazz
     * @param articleId
     */
    public static void skillList2Detail(Context context, Class<? extends BaseActivity> clazz, String articleId, String title) {
        sIntent = new Intent(context, clazz);
        sIntent.putExtra("title", title);
        sIntent.putExtra("articleId", articleId);
        context.startActivity(sIntent);
    }

    /**
     * 日志列表跳转到日志详情
     *
     * @param context
     * @param clazz
     * @param logcat
     */
    public static void logcatList2Detail(Context context, Class<? extends BaseActivity> clazz, String title, Logcat logcat) {
        sIntent = new Intent(context, clazz);
        sIntent.putExtra("title", title);
        sIntent.putExtra("logcat", logcat);
        context.startActivity(sIntent);
    }

    /**
     * 我的补卡记录列表跳转到补卡申请
     *
     * @param context
     * @param clazz
     * @param title
     * @param reissue
     */
    public static void mineReissueList2Apply(Context context, Class<? extends BaseActivity> clazz, String title, MineRecords reissue) {
        sIntent = new Intent(context, clazz);
        sIntent.putExtra("title", title);
        sIntent.putExtra("reissue", reissue);
        context.startActivity(sIntent);
    }

    /**
     * 补卡申请页面跳转到补卡审核记录页面
     *
     * @param context
     * @param clazz
     * @param title
     * @param recordId
     */
    public static void reissueApply2AuditList(Context context, Class<? extends BaseActivity> clazz, String title, String recordId) {
        sIntent = new Intent(context, clazz);
        sIntent.putExtra("title", title);
        sIntent.putExtra("recordId", recordId);
        context.startActivity(sIntent);
    }

    /**
     * 跳转到成功页面
     *
     * @param context
     * @param clazz
     * @param title
     * @param data
     */
    public static void jump2SuccessPage(Context context, Class<? extends BaseActivity> clazz, String title, Map<String, String> data) {
        sIntent = new Intent(context, clazz);
        sIntent.putExtra("title", title);
        sIntent.putExtra("introduce", data.get("introduce"));
        sIntent.putExtra("introduced", data.get("introduced"));
        sIntent.putExtra("customName", data.get("customName"));
        context.startActivity(sIntent);
    }
}
