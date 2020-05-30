package com.rainwood.oa.utils;

import android.content.Context;
import android.content.Intent;

import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.Contact;
import com.rainwood.oa.model.domain.Custom;

/**
 * @Author: a797s
 * @Date: 2020/5/18 18:21
 * @Desc: 页面跳转工具类
 */
public final class PageJumpUtil {

    private static Intent sIntent;

    /**
     * 跳转到客户详情页面
     *
     * @param context con
     * @param clazz   跳转目的地
     * @param custom  data
     */
    public static void listJump2CustomDetail(Context context, Class<? extends BaseActivity> clazz, Custom custom) {
        sIntent = new Intent(context, clazz);
        sIntent.putExtra("title", "客户详情");
        sIntent.putExtra("customId", custom.getKhid());
        context.startActivity(sIntent);
    }

    /**
     * 客户详情页查询联系人列表
     *
     * @param context  context
     * @param clazz    target clazz
     * @param customId 客户id
     */
    public static void CustomDetail2ContactList(Context context, Class<? extends BaseActivity> clazz, String customId) {
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
     * @param context
     * @param clazz
     */
    public static void Receivable2Detail(Context context, Class<? extends BaseActivity> clazz, String receivableId) {
        sIntent = new Intent(context, clazz);
        sIntent.putExtra("title", "回款记录详情");
        sIntent.putExtra("receivableId", receivableId);
        context.startActivity(sIntent);
    }
}
