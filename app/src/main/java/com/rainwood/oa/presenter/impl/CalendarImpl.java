package com.rainwood.oa.presenter.impl;

import com.rainwood.oa.model.domain.Month;
import com.rainwood.oa.network.json.JsonParser;
import com.rainwood.oa.network.okhttp.HttpResponse;
import com.rainwood.oa.network.okhttp.OkHttp;
import com.rainwood.oa.network.okhttp.OnHttpListener;
import com.rainwood.oa.network.okhttp.RequestParams;
import com.rainwood.oa.presenter.ICalendarPresenter;
import com.rainwood.oa.utils.Constants;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.oa.view.ICalendarCallbacks;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/10 13:54
 * @Desc: 日历相关逻辑implement
 */
public final class CalendarImpl implements ICalendarPresenter, OnHttpListener {

    private ICalendarCallbacks mCalendarCallbacks;

    /**
     * 生成月份
     */
    @Override
    public void begWorkDayMonth() {
        List<Month> months = new ArrayList<>();
        // 12月份
        for (int i = 0; i < 12; i++) {
            Month month = new Month();
            month.setMonth(String.valueOf(i + 1));
            months.add(month);
        }
        mCalendarCallbacks.getWorkDayMonthData(months);
    }

    /**
     * 请求当前月的工作日
     *
     * @param checkedMonth
     */
    @Override
    public void requestCurrentWorkDay(String checkedMonth) {
        RequestParams params = new RequestParams();
        params.add("moon", checkedMonth);
        OkHttp.post(Constants.BASE_URL + "cla=workDay&fun=home", params, this);
    }

    @Override
    public void registerViewCallback(ICalendarCallbacks callback) {
        mCalendarCallbacks = callback;
    }

    @Override
    public void unregisterViewCallback(ICalendarCallbacks callback) {
        mCalendarCallbacks = null;
    }

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        LogUtils.d("sxs", "result ---- " + result.body());
        if (!(result.code() == 200)) {
            mCalendarCallbacks.onError();
            return;
        }
        try {
            String warn = JsonParser.parseJSONObjectString(result.body()).getString("warn");
            if (!"success".equals(warn)) {
                mCalendarCallbacks.onError(warn);
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 当前月份的工作日
        if (result.url().contains("cla=workDay&fun=home")) {
            try {
                JSONArray jsonArray = JsonParser.parseJSONArrayString(
                        JsonParser.parseJSONObjectString(result.body()).getString("day"));
                // json数据处理
                List<String> dayList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    dayList.add((Integer.parseInt(jsonArray.getString(i)) < 10
                            ? "0" + jsonArray.getString(i)
                            : jsonArray.getString(i)));
                }
                mCalendarCallbacks.getWorkDayData(dayList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
