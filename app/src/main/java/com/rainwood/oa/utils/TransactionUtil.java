package com.rainwood.oa.utils;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.FloatRange;

/**
 * @Author: a797s
 * @Date: 2020/5/27 14:33
 * @Desc: 设置View 透明度util
 */
public final class TransactionUtil {

    /**
     * 设置view 透明度 包括子view
     *
     * @param view
     * @param alpha 10进制
     */
    public static void setAlphaAllView(View view, @FloatRange(from = 0.0, to = 1.0) float alpha) {
        if (view == null) {
            return;
        }
        if (view.getBackground() != null) {
            view.getBackground().mutate().setAlpha((int) (alpha * 255));
        }
        view.setAlpha(alpha);
        //设置子view透明度
        if (view instanceof ViewGroup) {
            ViewGroup vp = (ViewGroup) view;
            for (int i = 0; i < vp.getChildCount(); i++) {
                View viewChild = vp.getChildAt(i);
                //调用本身（递归）
                setAlphaAllView(viewChild, alpha);
            }
        }
    }

}
