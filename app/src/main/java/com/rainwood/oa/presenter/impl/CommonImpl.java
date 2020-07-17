package com.rainwood.oa.presenter.impl;

import com.rainwood.oa.model.domain.AuditRecords;
import com.rainwood.oa.model.domain.RecordsDetail;
import com.rainwood.oa.network.json.JsonParser;
import com.rainwood.oa.network.okhttp.HttpResponse;
import com.rainwood.oa.network.okhttp.OkHttp;
import com.rainwood.oa.network.okhttp.OnHttpListener;
import com.rainwood.oa.network.okhttp.RequestParams;
import com.rainwood.oa.presenter.ICommonPresenter;
import com.rainwood.oa.utils.Constants;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.oa.view.ICommonCallbacks;

import org.joda.time.TimeOfDay;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/5/26 14:22
 * @Desc: 公共数据请求---下拉选择框数据、popWindow数据，dialog数据
 */
public final class CommonImpl implements ICommonPresenter, OnHttpListener {

    private ICommonCallbacks mCommonCallbacks;

    /**
     * 审核记录列表
     */
    @Override
    public void AuditRecords() {
        // 模拟审核记录
        List<AuditRecords> recordsList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            AuditRecords auditRecords = new AuditRecords();
            auditRecords.setContent("韩梅梅驳回了蔡川的加班申请");
            auditRecords.setTime("2020.03.20 16:48");
            recordsList.add(auditRecords);
        }

        Map<String, List<AuditRecords>> recordMap = new HashMap<>();
        recordMap.put("overtime", recordsList);
     //   mCommonCallbacks.getAuditRecords(recordMap);
    }

    /**
     * 行政人事 --- 加班记录详情
     * @param overTimeId
     */
    @Override
    public void requestOverTimeRecordsById(String overTimeId) {
        RequestParams params = new RequestParams();
        params.add("life", Constants.life);
        params.add("workAddId", overTimeId);
        OkHttp.post(Constants.BASE_URL + "cla=workAdd&fun=detail", params, this);
    }

    /**
     * 行政人事 --- 请假详情
     * @param askLeaveId
     */
    @Override
    public void requestAskLeaveById(String askLeaveId) {
        RequestParams params = new RequestParams();
        params.add("life", Constants.life);
        params.add("id", askLeaveId);
        OkHttp.post(Constants.BASE_URL + "cla=work&fun=detail", params, this);
    }

    /**
     * 行政人事 -- 外出记录详情
     * @param staffId
     */
    @Override
    public void requestAskOutByStaffId(String staffId) {
        RequestParams params = new RequestParams();
        params.add("life", Constants.life);
        params.add("workOutId", staffId);
        OkHttp.post(Constants.BASE_URL + "cla=workOut&fun=detail", params, this);
    }

    /**
     * 行政人事 -- 补卡记录详情
     * @param reissueId
     */
    @Override
    public void requestReissueCardDetailById(String reissueId) {
        RequestParams params = new RequestParams();
        params.add("life", Constants.life);
        params.add("id", reissueId);
        OkHttp.post(Constants.BASE_URL + "cla=workSignAdd&fun=detail", params, this);
    }

    @Override
    public void registerViewCallback(ICommonCallbacks callback) {
        mCommonCallbacks = callback;
    }

    @Override
    public void unregisterViewCallback(ICommonCallbacks callback) {
        mCommonCallbacks = null;
    }

    @Override
    public void onHttpFailure(HttpResponse result) {
        mCommonCallbacks.onEmpty();
    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        LogUtils.d("sxs", "result ---- " + result.body());
        if (!(result.code() == 200)) {
            mCommonCallbacks.onError();
            return;
        }
        try {
            String warn = JsonParser.parseJSONObjectString(result.body()).getString("warn");
            if (!"success".equals(warn)) {
                mCommonCallbacks.onError(warn);
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 行政人事 -- 加班记录详情
        if (result.url().contains("cla=workAdd&fun=detail")){
            try {
                RecordsDetail recordsDetail = JsonParser.parseJSONObject(RecordsDetail.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("workAdd"));
                mCommonCallbacks.getOverTimeDetail(recordsDetail);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 行政人事 -- 请假详情
        else if (result.url().contains("cla=work&fun=detail")){
            try {
                RecordsDetail recordsDetail = JsonParser.parseJSONObject(RecordsDetail.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("work"));
                mCommonCallbacks.getAskLeaveDetailData(recordsDetail);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 行政人事 -- 外出记录详情
        else if (result.url().contains("cla=workOut&fun=detail")){
            try {
                RecordsDetail recordsDetail = JsonParser.parseJSONObject(RecordsDetail.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("workOut"));
                mCommonCallbacks.getAskOutDetailData(recordsDetail);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 行政人事 -- 补卡记录详情
        else if (result.url().contains("cla=workSignAdd&fun=detail")){
            try {
                RecordsDetail recordsDetail = JsonParser.parseJSONObject(RecordsDetail.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("add"));
                mCommonCallbacks.getReissueDetailData(recordsDetail);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
