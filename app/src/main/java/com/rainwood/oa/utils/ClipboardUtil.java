package com.rainwood.oa.utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

/**
 * @Author: a797s
 * @Date: 2020/5/28 13:44
 * @Desc:
 */
public final class ClipboardUtil {

    /**
     * 复制内容到剪切板
     * @param activity
     * @param label
     * @param content
     */
    public static void clipFormat2Board(Activity activity, String label, String content) {
        // 剪切板对象
        ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(label, content);
        // 传入剪贴板对象
        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
        }
    }
}
