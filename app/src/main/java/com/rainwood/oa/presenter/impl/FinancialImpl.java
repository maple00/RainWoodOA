package com.rainwood.oa.presenter.impl;

import com.rainwood.oa.model.domain.Reimbursement;
import com.rainwood.oa.model.domain.TeamFunds;
import com.rainwood.oa.network.json.JsonParser;
import com.rainwood.oa.network.okhttp.HttpResponse;
import com.rainwood.oa.network.okhttp.OkHttp;
import com.rainwood.oa.network.okhttp.OnHttpListener;
import com.rainwood.oa.network.okhttp.RequestParams;
import com.rainwood.oa.presenter.IFinancialPresenter;
import com.rainwood.oa.utils.Constants;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.oa.view.IFinancialCallbacks;

import org.json.JSONException;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/4 15:06
 * @Desc: 财务管理逻辑impl
 */
public final class FinancialImpl implements IFinancialPresenter, OnHttpListener {

    private IFinancialCallbacks mFinancialCallbacks;

    @Override
    public void requestReimburseData() {
        RequestParams params = new RequestParams();
        OkHttp.post(Constants.BASE_URL + "cla=cost&fun=home", params, this);
    }

    /**
     * 团队基金
     *
     * @param direction 请求类型
     */
    @Override
    public void requestTeamFundsData(String direction) {
        RequestParams params = new RequestParams();
        params.add("direction", direction);
        OkHttp.post(Constants.BASE_URL + "cla=teamFund&fun=home", params, this);
    }

    @Override
    public void registerViewCallback(IFinancialCallbacks callback) {
        mFinancialCallbacks = callback;
    }

    @Override
    public void unregisterViewCallback(IFinancialCallbacks callback) {
        mFinancialCallbacks = null;
    }

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        LogUtils.d("sxs", "result ---- " + result.body());
        if (!(result.code() == 200)) {
            mFinancialCallbacks.onError();
            return;
        }
        try {
            String warn = JsonParser.parseJSONObjectString(result.body()).getString("warn");
            if (!"success".equals(warn)) {
                mFinancialCallbacks.onError(warn);
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 团队基金
        if (result.url().contains("cla=teamFund&fun=home")) {
            try {
                List<TeamFunds> fundsList = JsonParser.parseJSONArray(TeamFunds.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("teamFund"));
                String money = JsonParser.parseJSONObjectString(result.body()).getString("money");
                mFinancialCallbacks.getTeamFundsData(fundsList, money);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 费用报销
        else if (result.url().contains("cla=cost&fun=home")) {
            try {
                List<Reimbursement> reimbursementList = JsonParser.parseJSONArray(Reimbursement.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("cost"));
                mFinancialCallbacks.getReimburseData(reimbursementList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
