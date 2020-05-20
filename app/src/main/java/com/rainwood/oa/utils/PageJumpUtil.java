package com.rainwood.oa.utils;

import android.content.Context;
import android.content.Intent;

import com.rainwood.oa.base.BaseActivity;
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
     * @param context con
     * @param clazz 跳转目的地
     * @param custom data
     */
    public static void listJump2CustomDetail(Context context, Class<? extends BaseActivity> clazz, Custom custom) {
        sIntent = new Intent(context, clazz);
        sIntent.putExtra("title", "客户详情");
        sIntent.putExtra("customData", custom);
        context.startActivity(sIntent);
    }
}
