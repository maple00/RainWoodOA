package com.rainwood.oa.network.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.rainwood.oa.R;
import com.rainwood.oa.network.okhttp.NetworkUtils;
import com.rainwood.oa.ui.dialog.MessageDialog;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.tools.wheel.BaseDialog;

/**
 * @Author: a797s
 * @Date: 2020/7/20 14:46
 * @Desc: 网络是否可用的广播
 */
public final class NetReceiver extends BroadcastReceiver {

    private static volatile NetReceiver sInstance;

    // 创建单例的网络检测对象
    public static NetReceiver getInstance() {
        if (sInstance == null) {
            synchronized (NetReceiver.class) {
                if (sInstance == null) {
                    sInstance = new NetReceiver();
                }
            }
        }
        return sInstance;
    }

    private BaseDialog mNetDialog;

    @Override
    public void onReceive(Context context, Intent intent) {

        boolean available = NetworkUtils.isAvailable(context);
        if (available) {
            // 先没网了再执行
            if (mNetDialog != null && mNetDialog.isShowing()) {
                mNetDialog.dismiss();
            }
            LogUtils.d("sxs", "当前网络可用");
            // 如果可用，重新执行之前的请求 -- 暴露对外调用的接口
            String networkType = NetworkUtils.getNetworkType(context);
            mChangeListener.changeListener(networkType);
        } else {
            // 提示没网
            mNetDialog = new MessageDialog.Builder(context)
                    .setMessage("当前网络较弱")
                    .setCanceledOnTouchOutside(false)
                    .setCancel(context.getString(R.string.common_cancel))
                    .setConfirm("切换网络")
                    .setShowImageClose(false)
                    .setShowConfirm(false)
                    .setListener(new MessageDialog.OnListener() {
                        @Override
                        public void onConfirm(BaseDialog dialog) {
                            context.startActivity(new Intent("android.net.wifi.PICK_WIFI_NETWORK"));
                        }

                        @Override
                        public void onCancel(BaseDialog dialog) {
                            dialog.dismiss();
                        }
                    }).show();
        }
    }

    public interface NetChangeListener {
        /**
         * 网络改变
         *
         * @param status 网络状态
         */
        void changeListener(String status);
    }

    private NetChangeListener mChangeListener;

    public void setChangeListener(NetChangeListener changeListener) {
        mChangeListener = changeListener;
    }
}
