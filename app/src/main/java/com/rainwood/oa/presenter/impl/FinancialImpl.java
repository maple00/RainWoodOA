package com.rainwood.oa.presenter.impl;

import com.rainwood.oa.model.domain.BalanceByMonthOrYear;
import com.rainwood.oa.model.domain.BalanceDetailData;
import com.rainwood.oa.model.domain.BalanceRecord;
import com.rainwood.oa.model.domain.ClassificationStatics;
import com.rainwood.oa.model.domain.IconAndFont;
import com.rainwood.oa.model.domain.ManagerMain;
import com.rainwood.oa.model.domain.MineReimbursement;
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
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.oa.view.IFinancialCallbacks;

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
     *
     * @param allocated  拨付状态
     * @param staffId    员工ID
     * @param type       类型
     * @param payer      付款方
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @param searchText 搜索内容
     * @param page       页码
     */
    @Override
    public void requestReimburseData(String allocated, String staffId, String type, String payer, String startTime, String endTime,
                                     String searchText, int page) {
        RequestParams params = new RequestParams();
        params.add("life", Constants.life);
        params.add("stid", staffId);
        params.add("type", "全部".equals(type) ? "" : type);
        params.add("payer", "全部".equals(payer) ? "" : payer);
        params.add("startDay", startTime);
        params.add("endDay", endTime);
        params.add("text", searchText);
        params.add("pay", allocated);
        OkHttp.post(Constants.BASE_URL + "cla=cost&fun=home&page=" + page, params, this);
    }

    /**
     * 费用报销 -- condition
     */
    @Override
    public void requestReimburseCondition() {
        RequestParams params = new RequestParams();
        params.add("life", Constants.life);
        OkHttp.post(Constants.BASE_URL + "cla=cost&fun=search", params, this);
    }

    /**
     * 收支记录列表
     *
     * @param staffId    员工id
     * @param origin     来源
     * @param direction  收入/支出
     * @param classify   分类
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @param searchText 搜索内容
     * @param page       页码
     */
    @Override
    public void requestBalanceRecords(String staffId, String origin, String direction, String classify, String startTime,
                                      String endTime, String searchText, int page) {
        RequestParams params = new RequestParams();
        params.add("life", Constants.life);
        params.add("stid", staffId);
        params.add("target", "全部".equals(origin) ? "" : origin);
        params.add("direction", "全部".equals(direction) ? "" : direction);
        params.add("type", "全部".equals(classify) ? "" : classify);
        params.add("startDay", startTime);
        params.add("endDay", endTime);
        params.add("text", searchText);
        OkHttp.post(Constants.BASE_URL + "cla=profit&fun=home&page=" + page, params, new OnHttpListener() {
            @Override
            public void onHttpFailure(HttpResponse result) {
                mFinancialCallbacks.onError();
            }

            @Override
            public void onHttpSucceed(HttpResponse result) {
                LogUtils.d("sxs", "result ---- " + result.body());
                LogUtils.d("sxs", "result ---- " + result.requestParams());
                try {
                    List<BalanceRecord> balanceRecordList = JsonParser.parseJSONArray(BalanceRecord.class,
                            JsonParser.parseJSONObjectString(result.body()).getString("profit"));
                    mFinancialCallbacks.getBalanceRecords(balanceRecordList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 收支记录列表 -- condition
     */
    @Override
    public void requestBalanceCondition() {
        RequestParams params = new RequestParams();
        params.add("life", Constants.life);
        OkHttp.post(Constants.BASE_URL + "cla=profit&fun=search", params, this);
    }

    @Override
    public void requestBalanceDetail(String recordId) {
        RequestParams params = new RequestParams();
        params.add("life", Constants.life);
        params.add("id", recordId);
        OkHttp.post(Constants.BASE_URL + "cla=profit&fun=detail", params, new OnHttpListener() {
            @Override
            public void onHttpFailure(HttpResponse result) {
                mFinancialCallbacks.onError();
            }

            @Override
            public void onHttpSucceed(HttpResponse result) {
                LogUtils.d("sxs", "result ---- " + result.body());
                LogUtils.d("sxs", "result ---- " + result.requestParams());

                try {
                    BalanceDetailData balanceDetailData = JsonParser.parseJSONObject(BalanceDetailData.class,
                            JsonParser.parseJSONObjectString(result.body()).getString("profit"));
                    mFinancialCallbacks.getBalanceDetailData(balanceDetailData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
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
        params.add("life", Constants.life);
        params.add("startDay", startTime);
        params.add("endDay", endTime);
        OkHttp.post(Constants.BASE_URL + "cla=profit&fun=profitTotal", params, this);
    }

    /**
     * 收支曲线 -- 按月
     *
     * @param startMonth 开始月份
     * @param endMonth   结束月份
     */
    @Override
    public void requestBalanceByMonth(String startMonth, String endMonth) {
        RequestParams params = new RequestParams();
        params.add("life", Constants.life);
        params.add("startMoon", startMonth);
        params.add("endMoon", endMonth);
        OkHttp.post(Constants.BASE_URL + "cla=profit&fun=profitMoon", params, this);
    }

    /**
     * 收支曲线 -- 按年
     * @param startYear
     * @param endYear
     */
    @Override
    public void requestBalanceByYear(String startYear, String endYear) {
        RequestParams params = new RequestParams();
        params.add("life", Constants.life);
        params.add("startYear", startYear);
        params.add("endYear", endYear);
        OkHttp.post(Constants.BASE_URL + "cla=profit&fun=profitYear", params, this);
    }

    /**
     * 员工数曲线图 ---
     *
     * @param startMonth
     * @param endMonth
     */
    @Override
    public void requestStaffNum(String startMonth, String endMonth) {
        RequestParams params = new RequestParams();
        params.add("life", Constants.life);
        params.add("startMoon", startMonth);
        params.add("endMoon", endMonth);
        OkHttp.post(Constants.BASE_URL + "cla=profit&fun=staffNum", params, this);
    }

    /**
     * 费用报销详情
     *
     * @param reimburseId
     */
    @Override
    public void requestReimburseDetail(String reimburseId) {
        RequestParams params = new RequestParams();
        params.add("life", Constants.life);
        OkHttp.post(Constants.BASE_URL + "cla=cost&fun=detail", params, this);
    }


    /**
     * 团队基金
     *
     * @param direction 请求类型
     */
    @Override
    public void requestTeamFundsData(String searchText, String direction, String startTime, String endTime, int page) {
        RequestParams params = new RequestParams();
        params.add("life", Constants.life);
        params.add("direction", direction);
        params.add("text", searchText);
        params.add("startDay", startTime);
        params.add("endDay", endTime);
        OkHttp.post(Constants.BASE_URL + "cla=teamFund&fun=home&page=" + page, params, this);
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
        params.add("life", Constants.life);
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
        LogUtils.d("sxs", "result ---- " + result.requestParams());

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
                // TODO: null Exception
                mFinancialCallbacks.getReimburseData(reimbursementList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 费用报销 --- condition
        else if (result.url().contains("cla=cost&fun=search")) {
            try {
                List<String> typeArray = JsonParser.parseJSONList(JsonParser.parseJSONObjectString(
                        JsonParser.parseJSONObjectString(result.body()).getString("search")).getString("type"));
                typeArray.add(0, "全部");
                List<String> payerArray = JsonParser.parseJSONList(JsonParser.parseJSONObjectString(
                        JsonParser.parseJSONObjectString(result.body()).getString("search")).getString("payer"));
                payerArray.add(0, "全部");
                List<SelectedItem> typeSelectedList = new ArrayList<>();
                List<SelectedItem> payerSelectedList = new ArrayList<>();
                for (int i = 0; i < ListUtils.getSize(typeArray); i++) {
                    SelectedItem item = new SelectedItem();
                    item.setName(typeArray.get(i));
                    typeSelectedList.add(item);
                }
                for (int i = 0; i < ListUtils.getSize(payerArray); i++) {
                    SelectedItem item = new SelectedItem();
                    item.setName(payerArray.get(i));
                    payerSelectedList.add(item);
                }
                mFinancialCallbacks.getReimburseCondition(typeSelectedList, payerSelectedList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 费用报销详情
        else if (result.url().contains("cla=cost&fun=detail")) {
            try {
                MineReimbursement reimbursement = JsonParser.parseJSONObject(MineReimbursement.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("cost"));
                mFinancialCallbacks.getReimburseDetail(reimbursement);
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
        // 收支记录列表-- condition
        else if (result.url().contains("cla=profit&fun=search")) {
            try {
                // 来源
                List<String> targetList = JsonParser.parseJSONList(JsonParser.parseJSONObjectString(
                        JsonParser.parseJSONObjectString(result.body()).getString("search")).getString("target"));
                targetList.add(0, "全部");
                List<SelectedItem> originList = new ArrayList<>();
                for (int i = 0; i < ListUtils.getSize(targetList); i++) {
                    SelectedItem item = new SelectedItem();
                    item.setName(targetList.get(i));
                    originList.add(item);
                }
                // 分类
                List<ManagerMain> balanceTypeList = JsonParser.parseJSONArray(ManagerMain.class,
                        JsonParser.parseJSONObjectString(JsonParser.parseJSONObjectString(result.body())
                                .getString("search")).getString("direction"));
                ManagerMain main = new ManagerMain();
                main.setName("全部");
                balanceTypeList.add(0, main);
                for (ManagerMain managerMain : balanceTypeList) {
                    managerMain.setHasSelected(false);
                    IconAndFont font = new IconAndFont();
                    font.setName("全部");
                    if (ListUtils.getSize(managerMain.getArray()) != 0) {
                        managerMain.getArray().add(0, font);
                    }
                }
                balanceTypeList.get(0).setHasSelected(true);
                // 权限显示部门员工
                String showDepart = JsonParser.parseJSONObjectString(JsonParser.parseJSONObjectString(result.body())
                        .getString("search")).getString("stid");

                mFinancialCallbacks.getInOutComeData(originList, balanceTypeList, showDepart);
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
                // 按月收支曲线 X轴
                List<String> balanceYearMonth = JsonParser.parseJSONList(
                        JsonParser.parseJSONObjectString(result.body()).getString("abscissa"));
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
                List<String> balanceYearMonth = JsonParser.parseJSONList(JsonParser.parseJSONObjectString(
                        result.body()).getString("abscissa"));
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
                List<String> xValues = JsonParser.parseJSONList(JsonParser.parseJSONObjectString(result.body()).getString("abscissa"));
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
