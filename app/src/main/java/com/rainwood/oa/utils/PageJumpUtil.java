package com.rainwood.oa.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;

import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.Contact;
import com.rainwood.oa.model.domain.Custom;
import com.rainwood.oa.model.domain.Logcat;
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
     * @param context
     * @param clazz
     * @param departId
     */
    public static void departList2Detail(Context context, Class<? extends BaseActivity> clazz, String departId){
        sIntent = new Intent(context, clazz);
        sIntent.putExtra("title", "部门详情");
        sIntent.putExtra("departId", departId);
        context.startActivity(sIntent);
    }

    /**
     * 职位列表跳转到详情页面
     * @param context
     * @param clazz
     * @param postId
     */
    public static void postList2Detail(Context context, Class<? extends BaseActivity> clazz, String postId){
        sIntent = new Intent(context, clazz);
        sIntent.putExtra("title", "职位详情");
        sIntent.putExtra("postId", postId);
        context.startActivity(sIntent);
    }

    /**
     * 客户列表跳转到客户详情页面
     *
     * @param context con
     * @param clazz   跳转目的地
     * @param customId  客户id
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
     * @param context
     * @param clazz
     * @param orderId
     */
    public static void orderList2Detail(Context context, Class<? extends BaseActivity> clazz, String orderId, String status){
        sIntent = new Intent(context, clazz);
        sIntent.putExtra("title", "订单详情");
        sIntent.putExtra("orderId", orderId);
        sIntent.putExtra("status", status);
        context.startActivity(sIntent);
    }

    /**
     * 费用报销列表页跳转到详情页面
     * @param context
     * @param clazz
     * @param reimburseId
     */
    public static void reimburseList2Detail(Context context, Class<? extends BaseActivity> clazz, String reimburseId){
        sIntent = new Intent(context, clazz);
        sIntent.putExtra("title", "费用详情");
        sIntent.putExtra(reimburseId, reimburseId);
        context.startActivity(sIntent);
    }

    /**
     * 文章列表页面跳转到文章详情页面
     * @param context
     * @param clazz
     * @param articleId
     */
    public static void skillList2Detail(Context context, Class<? extends BaseActivity> clazz, String articleId, String title){
        sIntent = new Intent(context, clazz);
        sIntent.putExtra("title", title);
        sIntent.putExtra("articleId", articleId);
        context.startActivity(sIntent);
    }

    /**
     * 日志列表跳转到日志详情
     * @param context
     * @param clazz
     * @param logcat
     */
    public static void logcatList2Detail(Context context, Class<? extends BaseActivity> clazz, String title, Logcat logcat){
        sIntent = new Intent(context, clazz);
        sIntent.putExtra("title", title);
        sIntent.putExtra("logcat", logcat);
        context.startActivity(sIntent);
    }
}
