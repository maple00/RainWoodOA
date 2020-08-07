package com.rainwood.oa.presenter.impl;

import com.rainwood.customchartview.utils.LogUtil;
import com.rainwood.oa.network.aop.CheckNet;
import com.rainwood.oa.network.json.JsonParser;
import com.rainwood.oa.network.okhttp.HttpResponse;
import com.rainwood.oa.network.okhttp.OkHttp;
import com.rainwood.oa.network.okhttp.OnHttpListener;
import com.rainwood.oa.network.okhttp.RequestParams;
import com.rainwood.oa.presenter.ILoginAboutPresenter;
import com.rainwood.oa.utils.Constants;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.oa.utils.SystemUtil;
import com.rainwood.oa.view.ILoginAboutCallbacks;

import org.json.JSONException;

/**
 * @Author: a797s
 * @Date: 2020/5/19 10:28
 * @Desc:
 */
public final class LoginAboutImpl implements OnHttpListener, ILoginAboutPresenter {

    private ILoginAboutCallbacks mLoginAboutCallback;

    /**
     * 登录
     *
     * @param userName 用户名
     * @param password 密码
     */
    @CheckNet
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

    /**
     * 发送验证码
     *
     * @param userName
     */
    @Override
    public void sendVerifyCode(String userName) {
        RequestParams params = new RequestParams();
        params.add("userName", userName);
        OkHttp.post(Constants.BASE_URL + "cla=forgetPas", params, new OnHttpListener() {
            @Override
            public void onHttpFailure(HttpResponse result) {
                mLoginAboutCallback.onError();
            }

            @Override
            public void onHttpSucceed(HttpResponse result) {
                // TODO: 发送验证码
                LogUtils.d("sxs", "--- 验证码 --- " + result.body());
                try {
                    mLoginAboutCallback.getVerifyResult("success".equals(JsonParser.parseJSONObjectString(result.body()).getString("warn")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
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
        //LogUtil.d("sxs", "----- result ---" );
        mLoginAboutCallback.onError();
    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        LogUtils.d("sxs", result.body());
        if (!(result.code() == 200)) {
            mLoginAboutCallback.onError();
            return;
        }
        try {
            String warn = JsonParser.parseJSONObjectString(result.body()).getString("warn");
            if (!"success".equals(warn)) {
                mLoginAboutCallback.onError(warn);
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String life = JsonParser.parseJSONObject(result.body()).get("life");
        mLoginAboutCallback.Login(life);
    }

}
