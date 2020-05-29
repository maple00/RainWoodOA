package com.rainwood.oa.presenter.impl;

import com.rainwood.oa.network.okhttp.HttpResponse;
import com.rainwood.oa.network.okhttp.OkHttp;
import com.rainwood.oa.network.okhttp.OnHttpListener;
import com.rainwood.oa.network.okhttp.RequestParams;
import com.rainwood.oa.presenter.ILoginAboutPresenter;
import com.rainwood.oa.utils.Constants;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.oa.utils.SystemUtil;
import com.rainwood.oa.view.ILoginAboutCallbacks;

/**
 * @Author: a797s
 * @Date: 2020/5/19 10:28
 * @Desc:
 */
public final class LoginAboutImpl implements OnHttpListener, ILoginAboutPresenter {

    private ILoginAboutCallbacks mLoginAboutCallback;

    @Override
    public void Login(String userName, String password) {
//        LogUtils.d("sxs -- 手机厂商", SystemUtil.getDeviceBrand());
//        LogUtils.d("sxs -- 系统语言", SystemUtil.getSystemLanguage());
//        LogUtils.d("sxs -- 手机型号", SystemUtil.getSystemModel());
//        LogUtils.d("sxs -- 系统版本", SystemUtil.getSystemVersion());
        RequestParams params = new RequestParams();
        params.add("userName", userName);
        params.add("password", password);
        // 手机厂商-手机型号-系统版本
        params.add("deviceName", SystemUtil.getDeviceBrand() + SystemUtil.getSystemModel() + SystemUtil.getSystemVersion());
        params.add("deviceID", Constants.IMEI);
        OkHttp.post(Constants.BASE_URL + "cla=login", params, this);
    }

    @Override
    public void registerViewCallback(ILoginAboutCallbacks callback) {
        mLoginAboutCallback = callback;
    }

    @Override
    public void unregisterViewCallback(ILoginAboutCallbacks callback) {
        mLoginAboutCallback = null;
    }

    @Override
    public void onHttpFailure(HttpResponse result) {
        mLoginAboutCallback.onError();
    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        LogUtils.d("sxs", result.body());
        mLoginAboutCallback.Login();
    }

}
