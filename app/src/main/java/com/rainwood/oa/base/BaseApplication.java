package com.rainwood.oa.base;

import android.app.Application;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import com.rainwood.oa.utils.ActivityStackManagerUtil;
import com.rainwood.tools.toast.ToastInterceptor;
import com.rainwood.tools.toast.ToastUtils;

/**
 * @Author: a797s
 * @Date: 2020/4/27 15:52
 * @Desc: app
 */
public final class BaseApplication extends Application {

    public static BaseApplication app;

    @Override
    public void onCreate() {
        super.onCreate();
        // android 7.0系统解决拍照的问题
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            builder.detectFileUriExposure();
        }
        app = this;
        // initActivity 初始化Activity 栈管理
        initActivity();
        // 初始化三方的框架
        initSDK();
    }

    private void initSDK() {
        // 设置 Toast 拦截器
        ToastUtils.setToastInterceptor(new ToastInterceptor() {
            @Override
            public boolean intercept(Toast toast, CharSequence text) {
                boolean intercept = super.intercept(toast, text);
                if (intercept) {
                    Log.e("Toast", "空 Toast");
                } else {
                    Log.i("Toast", text.toString());
                }
                return intercept;
            }
        });
        // 吐司工具类
        ToastUtils.init(this);

    }

    private void initActivity() {
        ActivityStackManagerUtil.getInstance().register(this);
    }


    public boolean isDetermineNetwork() {
        return true;
    }
}
