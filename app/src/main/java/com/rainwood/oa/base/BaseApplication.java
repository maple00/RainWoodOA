package com.rainwood.oa.base;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import com.rainwood.oa.model.domain.LoginData;
import com.rainwood.oa.model.domain.VersionData;
import com.rainwood.oa.network.caught.CaughtException;
import com.rainwood.oa.network.caught.OnCaughtExceptionListener;
import com.rainwood.oa.network.sqlite.SQLiteHelper;
import com.rainwood.oa.utils.ActivityStackManager;
import com.rainwood.oa.utils.Constants;
import com.rainwood.oa.utils.DeviceIdUtil;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.tools.toast.ToastInterceptor;
import com.rainwood.tools.toast.ToastUtils;
import com.rainwood.tools.toast.style.ToastAliPayStyle;
import com.tencent.smtt.sdk.QbSdk;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/4/27 15:52
 * @Desc: app
 */
public final class BaseApplication extends Application {

    private static final String TAG = "sxs";
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
        // 程序创建的时候执行
        LogUtils.d(TAG, "onCreate");
        // android 7.0系统解决拍照的问题
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
//            builder.detectFileUriExposure();
        }
        app = this;
        // initActivity 初始化Activity 栈管理
        initActivity();
        // 初始化三方的框架
        initSDK();
        // 初始化工具类
        initUtil();
        // 初始化数据库
        initDB();
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
        ToastUtils.initStyle(new ToastAliPayStyle(this));

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
        // 异常抓捕
        CaughtException.Builder caughtBuilder = new CaughtException.Builder(this);
        caughtBuilder.fileName("rainWoodCaught.txt");
        caughtBuilder.folderName("Exception");
        caughtBuilder.listener(new OnCaughtExceptionListener() {
            @Override
            public void onCaughtExceptionSucceed(File file) {
                // TODO: 上传caught文件

            }

            @Override
            public void onCaughtExceptionFailure(String error) {

            }
        });
        caughtBuilder.build();
    }

    /**
     * 创建数据表
     */
    private void initDB() {
        // 提示
        SQLiteHelper.with(this).createTable("rainWoodTips", new String[]{"module", "desc", "state"});
        String selectSql = "SELECT module, desc,state FROM rainWoodTips";
        List<Map<String, String>> list = SQLiteHelper.with(this).query(selectSql);
        // 插入数据
        if (ListUtils.getSize(list) == 0) {
            String sql = "INSERT INTO rainWoodTips (module, desc, state)" +
                    "VALUES('customIntroduce', '温馨提示：是否是第一次加载', 'notLoaded')";
            SQLiteHelper.with(this).insert(sql);
        }
        // 创建登录表
        // SQLiteHelper.with(this).dropTable(LoginData.class.getSimpleName());
        SQLiteHelper.with(this).createTable(LoginData.class);
        // 创建版本相关数据库
        SQLiteHelper.with(this).createTable(VersionData.class);
    }

    private void initActivity() {
        ActivityStackManager.getInstance().init(app);
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

    @Override
    public void onTerminate() {
        // 程序终止的时候执行
        LogUtils.d(TAG, "onTerminate");
        super.onTerminate();
    }

    @Override
    public void onLowMemory() {
        // 低内存的时候执行
        LogUtils.d(TAG, "onLowMemory");
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        // 程序在内存清理的时候执行（回收内存）
        // HOME键退出应用程序、长按MENU键，打开Recent TASK都会执行
        LogUtils.d(TAG, "onTrimMemory");
        super.onTrimMemory(level);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.fontScale != 1) { //fontScale不为1，需要强制设置为1
            getResources();
        }
    }

    @Override
    public Resources getResources() {
        Resources resources = super.getResources();
        if (resources.getConfiguration().fontScale != 1) { //fontScale不为1，需要强制设置为1
            Configuration newConfig = new Configuration();
            newConfig.setToDefaults();//设置成默认值，即fontScale为1
            resources.updateConfiguration(newConfig, resources.getDisplayMetrics());
        }
        return resources;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // MultiDex.install(this);
    }
}
