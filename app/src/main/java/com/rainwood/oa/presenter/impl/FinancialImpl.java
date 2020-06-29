package com.rainwood.oa.presenter.impl;

import com.rainwood.oa.model.domain.BalanceByMonthOrYear;
import com.rainwood.oa.model.domain.BalanceRecord;
import com.rainwood.oa.model.domain.ClassificationStatics;
import com.rainwood.oa.model.domain.ManagerMain;
import com.rainwood.oa.model.domain.Reimbursement;
import com.rainwood.oa.model.domain.SelectedItem;
import com.rainwood.oa.model.domain.StaffCurve;
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
     * 收支记录列表
     */
    @Override
    public void requestBalanceRecords() {
        RequestParams params = new RequestParams();
        OkHttp.post(Constants.BASE_URL + "cla=profit&fun=home", params, this);
    }

    /**
     * 收支记录列表 -- condition
     */
    @Override
    public void requestBalanceCondition() {
        RequestParams params = new RequestParams();
        OkHttp.post(Constants.BASE_URL + "cla=profit&fun=search", params, this);
    }

    /**
     * 请求分类统计数据
     *
     * @param startTime
     * @param endTime
     */
    @Override
    public void requestClassStatics(String startTime, String endTime) {
        RequestParams params = new RequestParams();
        params.add("startDay", startTime);
        params.add("endDay", endTime);
        OkHttp.post(Constants.BASE_URL + "cla=profit&fun=profitTotal", params, this);
    }

    /**
     * 收支曲线 -- 按月
     */
    @Override
    public void requestBalanceByMonth() {
        RequestParams params = new RequestParams();
        OkHttp.post(Constants.BASE_URL + "cla=profit&fun=profitMoon", params, this);
    }

    /**
     * 收支曲线 -- 按年
     */
    @Override
    public void requestBalanceByYear() {
        RequestParams params = new RequestParams();
        OkHttp.post(Constants.BASE_URL + "cla=profit&fun=profitYear", params, this);
    }

    /**
     * 员工数曲线图 ---
     */
    @Override
    public void requestStaffNum() {
        RequestParams params = new RequestParams();
        OkHttp.post(Constants.BASE_URL + "cla=profit&fun=staffNum", params, this);
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
        else if (result.url().contains("cla=cost&fun=search")) {
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
        // 收支记录列表
        else if (result.url().contains("cla=profit&fun=home")) {
            try {
                List<BalanceRecord> balanceRecordList = JsonParser.parseJSONArray(BalanceRecord.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("profit"));
                mFinancialCallbacks.getBalanceRecords(balanceRecordList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 收支记录列表-- condition
        else if (result.url().contains("cla=profit&fun=search")) {
            try {
                // 来源
                JSONArray targetArray = JsonParser.parseJSONArrayString(JsonParser.parseJSONObjectString(
                        JsonParser.parseJSONObjectString(result.body()).getString("search")).getString("target"));
                List<SelectedItem> originList = new ArrayList<>();
                for (int i = 0; i < targetArray.length(); i++) {
                    SelectedItem item = new SelectedItem();
                    item.setName(targetArray.getString(i));
                    originList.add(item);
                }
                // 分类
                List<ManagerMain> balanceTypeList = JsonParser.parseJSONArray(ManagerMain.class, JsonParser.parseJSONObjectString(JsonParser.parseJSONObjectString(result.body())
                        .getString("search")).getString("direction"));
                for (ManagerMain main : balanceTypeList) {
                    main.setHasSelected(false);
                }
                balanceTypeList.get(0).setHasSelected(true);
                mFinancialCallbacks.getInOutComeData(originList, balanceTypeList);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        // 分类统计数据
        else if (result.url().contains("cla=profit&fun=profitTotal")) {
            try {
                List<ClassificationStatics> classificationIncomeList = JsonParser.parseJSONArray(ClassificationStatics.class,
                        JsonParser.parseJSONObjectString(JsonParser.parseJSONObjectString(
                                JsonParser.parseJSONObjectString(result.body())
                                        .getString("total")).getString("income"))
                                .getString("array"));
                String incomeMoney = JsonParser.parseJSONObjectString(JsonParser.parseJSONObjectString(
                        JsonParser.parseJSONObjectString(result.body()).getString("total"))
                        .getString("income")).getString("money");
                List<ClassificationStatics> classificationOutcomeList = JsonParser.parseJSONArray(ClassificationStatics.class,
                        JsonParser.parseJSONObjectString(JsonParser.parseJSONObjectString(
                                JsonParser.parseJSONObjectString(result.body())
                                        .getString("total")).getString("cost"))
                                .getString("array"));
                String outcomeMoney = JsonParser.parseJSONObjectString(JsonParser.parseJSONObjectString(
                        JsonParser.parseJSONObjectString(result.body()).getString("total"))
                        .getString("cost")).getString("money");
                String balance = JsonParser.parseJSONObjectString(
                        JsonParser.parseJSONObjectString(result.body()).getString("total"))
                        .getString("balance");
                mFinancialCallbacks.getClassificationIncome(classificationIncomeList, incomeMoney,
                        classificationOutcomeList, outcomeMoney, balance);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 收支曲线　－－　按月
        else if (result.url().contains("cla=profit&fun=profitMoon")) {
            try {
                JSONArray abscissaArray = JsonParser.parseJSONArrayString(JsonParser.parseJSONObjectString(result.body()).getString("abscissa"));
                // 按月收支曲线 X轴
                List<String> balanceYearMonth = new ArrayList<>();
                for (int i = 0; i < abscissaArray.length(); i++) {
                    balanceYearMonth.add(abscissaArray.getString(i));
                }
                // Y轴数据
                List<BalanceByMonthOrYear> monthBalanceList = JsonParser.parseJSONArray(BalanceByMonthOrYear.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("array"));
                mFinancialCallbacks.getBalanceMonthData(balanceYearMonth, monthBalanceList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 收支曲线 -- 按年
        else if (result.url().contains("cla=profit&fun=profitYear")) {
            try {
                // 按年收支曲线 X轴
                JSONArray abscissaArray = JsonParser.parseJSONArrayString(JsonParser.parseJSONObjectString(result.body()).getString("abscissa"));
                List<String> balanceYearMonth = new ArrayList<>();
                for (int i = 0; i < abscissaArray.length(); i++) {
                    balanceYearMonth.add(abscissaArray.getString(i));
                }
                // 按年收支曲线 Y轴
                // Y轴数据
                List<BalanceByMonthOrYear> monthBalanceList = JsonParser.parseJSONArray(BalanceByMonthOrYear.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("array"));
                mFinancialCallbacks.getBalanceYearData(balanceYearMonth, monthBalanceList);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        // 员工数量曲线
        else if (result.url().contains("cla=profit&fun=staffNum")) {
            try {
                // X轴
                JSONArray abscissaArray = JsonParser.parseJSONArrayString(JsonParser.parseJSONObjectString(result.body()).getString("abscissa"));
                List<String> xValues = new ArrayList<>();
                for (int i = 0; i < abscissaArray.length(); i++) {
                    xValues.add(abscissaArray.getString(i));
                }
                // Y轴数据
                List<StaffCurve> staffNumList = JsonParser.parseJSONArray(StaffCurve.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("array"));
                mFinancialCallbacks.getStaffNumByCurve(xValues, staffNumList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
