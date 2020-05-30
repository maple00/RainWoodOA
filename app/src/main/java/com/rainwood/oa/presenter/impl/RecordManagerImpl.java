package com.rainwood.oa.presenter.impl;

import com.rainwood.oa.model.domain.CardRecord;
import com.rainwood.oa.model.domain.CustomFollowRecord;
import com.rainwood.oa.model.domain.LeaveOutRecord;
import com.rainwood.oa.model.domain.LeaveRecord;
import com.rainwood.oa.model.domain.OvertimeRecord;
import com.rainwood.oa.model.domain.ReceivableRecord;
import com.rainwood.oa.network.json.JsonParser;
import com.rainwood.oa.network.okhttp.HttpResponse;
import com.rainwood.oa.network.okhttp.OkHttp;
import com.rainwood.oa.network.okhttp.OnHttpListener;
import com.rainwood.oa.network.okhttp.RequestParams;
import com.rainwood.oa.presenter.IRecordManagerPresenter;
import com.rainwood.oa.utils.Constants;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.oa.view.IRecordCallbacks;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/5/25 19:59
 * @Desc: 记录管理逻辑类
 */
public final class RecordManagerImpl implements IRecordManagerPresenter, OnHttpListener {

    private IRecordCallbacks mRecordCallbacks;

    /**
     * 加班记录
     */
    @Override
    public void requestOvertimeRecord(String customId) {
        RequestParams params = new RequestParams();
        params.add("khid", customId);
        OkHttp.post(Constants.BASE_URL + "cla=client&fun=workAdd", params, this);
    }

    @Override
    public void requestLeaveRecord() {
        // 模拟请假记录
        List<LeaveRecord> leaveRecordList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            LeaveRecord leaveRecord = new LeaveRecord();
            leaveRecord.setName("无双剑姬");
            leaveRecord.setContent("动作太慢了");
            leaveRecord.setStatus("审核中");
            leaveRecord.setTime("请假时间：2020.03.20 09:00 - 2020.03.20 18:00");
            leaveRecordList.add(leaveRecord);
        }

        Map<String, List<LeaveRecord>> leaveMap = new HashMap<>();
        leaveMap.put("leave", leaveRecordList);
        mRecordCallbacks.getLeaveRecords(leaveMap);
    }

    /**
     * 外出记录
     *
     * @param customId 客户id
     */
    @Override
    public void requestGoOutRecord(String customId) {
        RequestParams params = new RequestParams();
        params.add("khid", customId);
        OkHttp.post(Constants.BASE_URL + "cla=client&fun=out", params, this);
    }

    @Override
    public void requestReissueRecord() {
        // 模拟补卡记录
        List<CardRecord> cardRecordList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            CardRecord cardRecord = new CardRecord();
            cardRecord.setName("暗影猎手-VN");
            cardRecord.setStatus("离线");
            cardRecord.setTime("2020.03.25 18:00");
            cardRecord.setContent("下午4点出发到大足鑫发集团沟通详细的需求，所以未打下卡下午4点出发到大足鑫发集团，所以未打下班卡。");
            cardRecordList.add(cardRecord);
        }
        Map<String, List<CardRecord>> cardMap = new HashMap<>();
        cardMap.put("cardRecord", cardRecordList);
        mRecordCallbacks.getReissueRecords(cardMap);
    }

    /**
     * 请求客户的跟进记录
     *
     * @param customId 客户id
     */
    @Override
    public void requestCustomFollowRecords(String customId) {
        RequestParams params = new RequestParams();
        params.add("khid", customId);
        OkHttp.post(Constants.BASE_URL + "cla=client&fun=follow", params, this);
    }

    /**
     * 客户回款记录
     *
     * @param customId
     */
    @Override
    public void requestCustomReceivableRecords(String customId) {
        RequestParams params = new RequestParams();
        params.add("khid", customId);
        OkHttp.post(Constants.BASE_URL + "cla=client&fun=collection", params, this);
    }

    /**
     * 客户回款记录详情
     *
     * @param receivableId
     */
    @Override
    public void requestCustomReceivableRecordDetail(String receivableId) {
        RequestParams params = new RequestParams();
        params.add("id ", receivableId);
        OkHttp.post(Constants.BASE_URL + "cla=client&fun=collectionDetail", params, this);
    }

    @Override
    public void registerViewCallback(IRecordCallbacks callback) {
        mRecordCallbacks = callback;
    }

    @Override
    public void unregisterViewCallback(IRecordCallbacks callback) {
        mRecordCallbacks = null;
    }

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        LogUtils.d("sxs", "result ---- " + result.body());
        if (!(result.code() == 200)) {
            mRecordCallbacks.onError();
            return;
        }
        try {
            String warn = JsonParser.parseJSONObjectString(result.body()).getString("warn");
            if (!"success".equals(warn)) {
                mRecordCallbacks.onError(warn);
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 跟进记录
        if (result.url().contains("cla=client&fun=follow")) {
            try {
                List<CustomFollowRecord> customFollowRecordList = JsonParser.parseJSONArray(CustomFollowRecord.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("follow"));
                mRecordCallbacks.getCustomFollowRecords(customFollowRecordList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 外出记录
        else if (result.url().contains("cla=client&fun=out")) {
            try {
                List<LeaveOutRecord> customFollowRecordList = JsonParser.parseJSONArray(LeaveOutRecord.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("out"));
                mRecordCallbacks.getGoOutRecords(customFollowRecordList);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        // 加班记录
        else if (result.url().contains("cla=client&fun=workAdd")) {
            try {
                List<OvertimeRecord> overtimeRecordList = JsonParser.parseJSONArray(OvertimeRecord.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("add"));
                mRecordCallbacks.getOvertimeRecords(overtimeRecordList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 回款记录
        else if (result.url().equals(Constants.BASE_URL + "cla=client&fun=collection")) {
            try {
                List<ReceivableRecord> receivableList = JsonParser.parseJSONArray(ReceivableRecord.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("collection"));
                mRecordCallbacks.getCustomReceivableRecords(receivableList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 回款记录详情
        else if (result.url().equals(Constants.BASE_URL + "cla=client&fun=collectionDetail")) {
            try {
                ReceivableRecord receivableRecord = JsonParser.parseJSONObject(ReceivableRecord.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("collection"));
                mRecordCallbacks.getCustomReceivableRecordDetail(receivableRecord);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
