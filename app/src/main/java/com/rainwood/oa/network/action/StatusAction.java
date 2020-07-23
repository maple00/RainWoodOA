package com.rainwood.oa.network.action;

import android.app.Application;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.RawRes;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

import com.rainwood.oa.R;
import com.rainwood.oa.utils.ActivityStackManager;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.tools.wheel.widget.HintLayout;

/**
 * create by a797s in 2020/7/20 11:45
 *
 * @Description : 状态界面
 * @Usage : 展示状态界面
 **/
public interface StatusAction {

    /**
     * 获取提示布局
     */
    HintLayout getHintLayout();

    /**
     * 显示加载中
     */
    default void showLoading() {
        showLoading(R.raw.loading);
    }

    default void showLoading(@RawRes int id) {
        HintLayout layout = getHintLayout();
        layout.show();
        layout.setAnim(id);
        layout.setHint("加载中，请稍后");
        layout.setOnClickListener(null);
    }



    /**
     * 显示加载完成
     */
    default void showComplete() {
        HintLayout layout = getHintLayout();
        if (layout != null && layout.isShow()) {
            layout.hide();
        }
    }

    /**
     * 显示空提示
     */
    default void showEmpty() {
        showLayout(R.drawable.ic_hint_empty, com.rainwood.tools.R.string.hint_layout_no_data, null);
    }

    /**
     * 显示错误提示
     */
    default void showError(View.OnClickListener listener) {
        Application application = ActivityStackManager.getInstance().getApplication();
        if (application != null) {
            ConnectivityManager manager = ContextCompat.getSystemService(application, ConnectivityManager.class);
            if (manager != null) {
                NetworkInfo info = manager.getActiveNetworkInfo();
                // 判断网络是否连接
                if (info == null || !info.isConnected()) {
                    showLayout(com.rainwood.tools.R.drawable.ic_hint_nerwork, com.rainwood.tools.R.string.hint_layout_error_network, listener);
                    return;
                }
            }
        }
        showLayout(R.drawable.ic_hint_error, com.rainwood.tools.R.string.hint_layout_error_request, listener);
    }

    /**
     * 显示自定义提示
     */
    default void showLayout(@DrawableRes int drawableId, @StringRes int stringId, View.OnClickListener listener) {
        showLayout(ContextCompat.getDrawable(ActivityStackManager.getInstance().getTopActivity(), drawableId), ActivityStackManager.getInstance().getTopActivity().getString(stringId), listener);
    }

    default void showLayout(Drawable drawable, CharSequence hint, View.OnClickListener listener) {
        HintLayout layout = getHintLayout();
        layout.show();
        layout.setIcon(drawable);
        layout.setHint(hint);
        layout.setOnClickListener(listener);
    }

}