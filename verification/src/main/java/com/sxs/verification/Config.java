package com.sxs.verification;

import android.graphics.Color;

/**
 * @Author: a797s
 * @Date: 2020/5/19 11:17
 * @Desc: 用于对验证码控件参数的配置
 */
public final class Config {

    // 验证码更新时间
    public static final int PTEDE_TIME = 1200;
    // 点数设置
    public static final int POINT_NUM = 100;
    // 线段数设置
    public static final int LINE_NUM = 2;
    //设置背景颜色
    public static final int COLOR = Color.parseColor("#ADE3D5");
    //随机数据长度
    public static int TEXT_LENGTH = 4;
    //设置验证码字体大小
    public static int TEXT_SIZE = 30;
}
