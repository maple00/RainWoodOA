package com.rainwood.oa.base;

import android.app.Application;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import com.rainwood.oa.utils.ActivityStackManagerUtil;
import com.rainwood.oa.utils.Constants;
import com.rainwood.oa.utils.DeviceIdUtil;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.tools.toast.ToastInterceptor;
import com.rainwood.tools.toast.ToastUtils;
import com.tencent.smtt.sdk.QbSdk;

/**
 * @Author: a797s
 * @Date: 2020/4/27 15:52
 * @Desc: app
 */
public final class BaseApplication extends Application {


    /**
     * 是否调试
     */
    private boolean isDebug = false;
    /**
     * 是否显示
     */
    private boolean isShow = false;

    /**
     * application 对象
     */
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
        // 初始化工具类
        initUtil();
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

        /*
         TBS 文件预览
         */
        //初始化X5内核
        QbSdk.PreInitCallback callback = new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {
                //x5内核初始化完成回调接口，此接口回调并表示已经加载起来了x5，有可能特殊情况下x5内核加载失败，切换到系统内核。
                LogUtils.d("sxs", "TBS load success");
            }

            @Override
            public void onViewInitFinished(boolean b) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                LogUtils.d("sxs", "TBS is loading: " + b);
                // 如果加载失败，多次回调
                /*if (!b && initialSize < 3) {
                    initialSize++;
                    QbSdk.initX5Environment(app.getApplicationContext(), null);
                }*/
                //QbSdk.initX5Environment(app.getApplicationContext(), null);
            }
        };
        // 允许使用流量下载
        QbSdk.setDownloadWithoutWifi(true);
        QbSdk.initX5Environment(this.getApplicationContext(), callback);

    }

    /**
     * 初始化工具类
     */
    private void initUtil() {
        // 获取IMEI
        Constants.IMEI = DeviceIdUtil.getDeviceId(this);
    }

    private void initActivity() {
        ActivityStackManagerUtil.getInstance().register(app);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ActivityStackManagerUtil.getInstance().unRegister(app);
    }

    public boolean isDetermineNetwork() {
        return true;
    }

    /**
     * 是否是调试环境
     *
     * @return
     */
    public boolean isDebug() {
        return isDebug;
    }

    /**
     * 设置调试模式
     *
     * @param debug
     */
    public void setDebugMode(boolean debug) {
        isDebug = debug;
        isShow = false;
    }

    /**
     * 设置调试环境
     *
     * @param enable
     * @param show   显示
     */
    public void setDebugMode(boolean enable, boolean show) {
        isDebug = enable;
        isShow = show;
    }

    /**
     * @return
     */
    public boolean isShowDebug() {
        return isShow;
    }

}
