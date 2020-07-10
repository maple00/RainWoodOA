package com.rainwood.oa.presenter.impl;

import com.rainwood.oa.model.domain.Logcat;
import com.rainwood.oa.model.domain.ManagerMain;
import com.rainwood.oa.network.json.JsonParser;
import com.rainwood.oa.network.okhttp.HttpResponse;
import com.rainwood.oa.network.okhttp.OkHttp;
import com.rainwood.oa.network.okhttp.OnHttpListener;
import com.rainwood.oa.network.okhttp.RequestParams;
import com.rainwood.oa.presenter.ILogcatPresenter;
import com.rainwood.oa.utils.Constants;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.oa.view.ILogcatCallbacks;

import org.json.JSONException;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/28 9:45
 * @Desc: 系统设置逻辑类
 */
public final class LogcatImpl implements ILogcatPresenter, OnHttpListener {

    private ILogcatCallbacks mLogcatCallbacks;

    /**
     * 请求日志列表
     * @param searchText
     * @param typeOne
     * @param typeTwo
     * @param staffId
     * @param startTime
     * @param endTime
     * @param pageCount
     */
    @Override
    public void requestLogcatData(String searchText, String typeOne, String typeTwo, String staffId, String startTime, String endTime, int pageCount) {
        RequestParams params = new RequestParams();
        params.add("text", searchText);
        params.add("powerOne", typeOne);
        params.add("powerTwo", typeTwo);
        params.add("stid", staffId);
        params.add("startDay", startTime);
        params.add("endDay", endTime);
        OkHttp.post(Constants.BASE_URL + "cla=log&fun=home&page=" + pageCount, params, this);
    }

    /**
     * 请求日志类型
     */
    @Override
    public void requestLogcatType() {
        RequestParams params = new RequestParams();
        OkHttp.post(Constants.BASE_URL + "cla=manage&fun=menu", params, this);
    }

    @Override
    public void registerViewCallback(ILogcatCallbacks callback) {
        mLogcatCallbacks = callback;
    }

    @Override
    public void unregisterViewCallback(ILogcatCallbacks callback) {
        mLogcatCallbacks = null;
    }

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        LogUtils.d("sxs", "result ---- " + result.body());
        if (!(result.code() == 200)) {
            mLogcatCallbacks.onError();
            return;
        }
        try {
            String warn = JsonParser.parseJSONObjectString(result.body()).getString("warn");
            if (!"success".equals(warn)) {
                mLogcatCallbacks.onError(warn);
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 系统日志
        if (result.url().contains("cla=log&fun=home")) {
            try {
                List<Logcat> logcatList = JsonParser.parseJSONArray(Logcat.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("log"));
                mLogcatCallbacks.getSystemLogcat(logcatList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 日志类型
        else if (result.url().contains("cla=manage&fun=menu")) {
            try {
                List<ManagerMain> menuList = JsonParser.parseJSONArray(ManagerMain.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("menu"));
                mLogcatCallbacks.getMainManagerData(menuList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
