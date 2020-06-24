package com.rainwood.oa.presenter.impl;

import com.rainwood.oa.model.domain.Reimbursement;
import com.rainwood.oa.model.domain.SelectedItem;
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

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/4 15:06
 * @Desc: 财务管理逻辑impl
 */
public final class FinancialImpl implements IFinancialPresenter, OnHttpListener {

    private IFinancialCallbacks mFinancialCallbacks;

    /**
     * 费用报销
     */
    @Override
    public void requestReimburseData() {
        RequestParams params = new RequestParams();
        OkHttp.post(Constants.BASE_URL + "cla=cost&fun=home", params, this);
    }

    /**
     * 费用报销 -- condition
     */
    @Override
    public void requestReimburseCondition() {
        RequestParams params = new RequestParams();
        OkHttp.post(Constants.BASE_URL + "cla=cost&fun=search", params, this);
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

    /**
     * 行政处罚
     *
     * @param staffId
     * @param money
     * @param reason
     * @param password
     */
    @Override
    public void requestPunishStaff(String staffId, String money, String reason, String password) {
        RequestParams params = new RequestParams();
        params.add("stid", staffId);
        params.add("money", money);
        params.add("text", reason);
        params.add("password", password);
        OkHttp.post(Constants.BASE_URL + "cla=teamFund&fun=punish", params, this);
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
        // 费用报销 --- condition
        else if (result.url().contains("cla=cost&fun=search")){
            try {
                JSONArray typeArray = JsonParser.parseJSONArrayString(JsonParser.parseJSONObjectString(
                        JsonParser.parseJSONObjectString(result.body()).getString("search")).getString("type"));
                JSONArray payerArray = JsonParser.parseJSONArrayString(JsonParser.parseJSONObjectString(
                        JsonParser.parseJSONObjectString(result.body()).getString("search")).getString("payer"));
                List<SelectedItem> typeSelectedList = new ArrayList<>();
                List<SelectedItem> payerSelectedList = new ArrayList<>();
                for (int i = 0; i < typeArray.length(); i++) {
                    SelectedItem item = new SelectedItem();
                    item.setName(typeArray.getString(i));
                    typeSelectedList.add(item);
                }
                for (int i = 0; i < payerArray.length(); i++) {
                    SelectedItem item = new SelectedItem();
                    item.setName(payerArray.getString(i));
                    payerSelectedList.add(item);
                }
                mFinancialCallbacks.getReimburseCondition(typeSelectedList, payerSelectedList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 行政处罚
        else if (result.url().contains("cla=teamFund&fun=punish")) {
            try {
                String warn = JsonParser.parseJSONObjectString(result.body()).getString("warn");
                mFinancialCallbacks.getPunishResult("success".equals(warn));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
