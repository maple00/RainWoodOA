package com.rainwood.oa.utils;

import android.util.Log;

import com.rainwood.oa.BuildConfig;

/**
 * @Author: a797s
 * @Date: 2020/4/17 11:58
 * @Desc: 日志管理
 */
public final class LogUtils {

    public static void i(Object TAG, Object msg) {
        logLong("i", TAG instanceof String ? TAG.toString() :TAG.getClass().getSimpleName(), msg);
    }

    public static void d(Object TAG, Object msg) {
        logLong("d", TAG instanceof String ? TAG.toString() :TAG.getClass().getSimpleName(), msg);
    }

    public static void v(Object TAG, Object msg) {
        logLong("v", TAG instanceof String ? TAG.toString() :TAG.getClass().getSimpleName(), msg);
    }

    public static void w(Object TAG, Object msg) {
        logLong("w", TAG instanceof String ? TAG.toString() :TAG.getClass().getSimpleName(), msg);
    }

    public static void e(Object TAG, Object msg) {
        logLong("e", TAG instanceof String ? TAG.toString() :TAG.getClass().getSimpleName(), msg);
    }

    /**
     * 日志太长打印不全时，分段打印
     *
     * @param type
     * @param tag
     * @param msg
     */
    private static void logLong(String type, String tag, Object msg) {
        String content = (null == msg) ? "" : msg.toString();
        int maxLength = 1000;
        long length = content.length();
        if (length <= maxLength) {
            logByType(type, tag, content);
        } else {
            while (content.length() > maxLength) {
                String logContent = content.substring(0, maxLength);
                content = content.replace(logContent, "");
                logByType(type, tag, logContent);
            }
            logByType(type, tag, content);
        }
    }

    /**
     * 打印不同颜色日志
     */
    private static void logByType(String type, String tag, String content) {
        if (BuildConfig.DEBUG) {
            switch (type) {
                case "i":
                    Log.i(tag, "" + (null == content ? "" : content));
                    break;
                case "d":
                    Log.d(tag, "" + (null == content ? "" : content));
                    break;
                case "v":
                    Log.v(tag, "" + (null == content ? "" : content));
                    break;
                case "w":
                    Log.w(tag, "" + (null == content ? "" : content));
                    break;
                case "e":
                    Log.e(tag, "" + (null == content ? "" : content));
                    break;
            }
        }
    }
}
