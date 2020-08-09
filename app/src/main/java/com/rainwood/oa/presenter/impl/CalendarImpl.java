package com.rainwood.oa.presenter.impl;

import com.rainwood.oa.model.domain.AttendanceData;
import com.rainwood.oa.model.domain.CalendarBody;
import com.rainwood.oa.model.domain.Month;
import com.rainwood.oa.network.json.JsonParser;
import com.rainwood.oa.network.okhttp.HttpResponse;
import com.rainwood.oa.network.okhttp.OkHttp;
import com.rainwood.oa.network.okhttp.OnHttpListener;
import com.rainwood.oa.network.okhttp.RequestParams;
import com.rainwood.oa.presenter.ICalendarPresenter;
import com.rainwood.oa.utils.Constants;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.oa.utils.StringUtil;
import com.rainwood.oa.view.ICalendarCallback;
import com.rainwood.tkrefreshlayout.utils.LogUtil;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/10 13:54
 * @Desc: 日历相关逻辑implement
 */
public final class CalendarImpl implements ICalendarPresenter, OnHttpListener {

    private ICalendarCallback mCalendarCallbacks;

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
        params.add("life", Constants.life);
        params.add("moon", checkedMonth);
        OkHttp.post(Constants.BASE_URL + "cla=workDay&fun=home", params, this);
    }

    /**
     * 请求当月的考勤信息
     *
     * @param currentMonth
     */
    @Override
    public void requestCurrentDayAttendance(String staffId, String currentMonth) {
        RequestParams params = new RequestParams();
        params.add("life", Constants.life);
        params.add("stid", staffId);
        params.add("month", currentMonth);
        OkHttp.post(Constants.BASE_URL + "cla=workSign&fun=home", params, this);
    }

    /**
     * 请求我的考勤数据
     * @param month
     */
    @Override
    public void requestMineAttendanceData(String month) {
        RequestParams params = new RequestParams();
        params.add("life", Constants.life);
        params.add("month", month);
        OkHttp.post(Constants.BASE_URL + "cla=workSign&fun=my", params, new OnHttpListener() {
            @Override
            public void onHttpFailure(HttpResponse result) {
                if (mCalendarCallbacks != null){
                    mCalendarCallbacks.onError();
                }
            }

            @Override
            public void onHttpSucceed(HttpResponse result) {
                //LogUtils.d("sxs", "------ result ------" + result.body());
                AttendanceData attendanceData = JsonParser.parseJSONObject(AttendanceData.class, result.body());
                if (mCalendarCallbacks != null){
                    mCalendarCallbacks.getAttendanceData(attendanceData);
                }
            }
        });
    }

    @Override
    public void registerViewCallback(ICalendarCallback callback) {
        mCalendarCallbacks = callback;
    }

    @Override
    public void unregisterViewCallback(ICalendarCallback callback) {
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
                // json数据处理
                CalendarBody calendarBody = JsonParser.parseJSONObject(CalendarBody.class,
                        JsonParser.parseJSONObjectString(result.body()));
                String dayNote = JsonParser.parseJSONObjectString(result.body()).getString("text");
                mCalendarCallbacks.getWorkDayData(calendarBody, dayNote);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 考勤
        else if (result.url().contains("cla=workSign&fun=home")) {
            AttendanceData attendanceData = JsonParser.parseJSONObject(AttendanceData.class, result.body());
            mCalendarCallbacks.getAttendanceData(attendanceData);
        }
    }
}
