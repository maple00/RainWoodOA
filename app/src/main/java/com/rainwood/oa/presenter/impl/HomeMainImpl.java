package com.rainwood.oa.presenter.impl;

import com.rainwood.oa.model.domain.FontAndFont;
import com.rainwood.oa.model.domain.HomeSalary;
import com.rainwood.oa.network.json.JsonParser;
import com.rainwood.oa.network.okhttp.HttpResponse;
import com.rainwood.oa.network.okhttp.OkHttp;
import com.rainwood.oa.network.okhttp.OnHttpListener;
import com.rainwood.oa.network.okhttp.RequestParams;
import com.rainwood.oa.presenter.IHomePresenter;
import com.rainwood.oa.utils.Constants;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.oa.view.IHomeCallbacks;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/4/29 10:53
 * @Desc: 首页主要内容
 */
public final class HomeMainImpl implements IHomePresenter, OnHttpListener {

    private IHomeCallbacks mHomeCallbacks;

    private String[] salaries = {"基本工资(元)", "岗位津贴(元)", "会计账户(元)", "结算账户(元)"};

    /**
     * 请求工资曲线
     */
    @Override
    public void requestSalaryData(String startMonth, String endMonth) {
        if (mHomeCallbacks != null) {
            mHomeCallbacks.onLoading();
        }
        RequestParams params = new RequestParams();
        params.add("startMoon", startMonth);
        params.add("endMoon", endMonth);
        OkHttp.post(Constants.BASE_URL + "cla=home&fun=wages", params, this);
    }

    @Override
    public void registerViewCallback(IHomeCallbacks callback) {
        this.mHomeCallbacks = callback;
    }

    @Override
    public void unregisterViewCallback(IHomeCallbacks callback) {
        this.mHomeCallbacks = null;
    }

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        LogUtils.d("sxs", "result ---- " + result.body());
        if (!(result.code() == 200)) {
            mHomeCallbacks.onError();
            return;
        }
        try {
            String warn = JsonParser.parseJSONObjectString(result.body()).getString("warn");
            if (!"success".equals(warn)) {
                mHomeCallbacks.onError(warn);
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 工资曲线
        if (result.url().contains("cla=home&fun=wages")) {
            try {
                // 收入
                JSONArray incomeJsonArray = JsonParser.parseJSONArrayString(JsonParser.parseJSONObjectString(result.body()).getString("income"));
                List<String> incomeList = new ArrayList<>();
                for (int i = 0; i < incomeJsonArray.length(); i++) {
                    incomeList.add(incomeJsonArray.getString(i));
                }
                //年份
                JSONArray monthJsonArray = JsonParser.parseJSONArrayString(JsonParser.parseJSONObjectString(result.body()).getString("list"));
                List<String> monthList = new ArrayList<>();
                for (int i = 0; i < monthJsonArray.length(); i++) {
                    monthList.add(monthJsonArray.getString(i));
                }
                //
                HomeSalary homeSalary = JsonParser.parseJSONObject(HomeSalary.class, result.body());
                List<FontAndFont> salaryList = new ArrayList<>();
                for (int i = 0; i < salaries.length; i++) {
                    FontAndFont andFont = new FontAndFont();
                    andFont.setDesc(salaries[i]);
                    switch (i) {
                        case 0:
                            andFont.setTitle(homeSalary.getBasePay());
                            break;
                        case 1:
                            andFont.setTitle(homeSalary.getSubsidy());
                            break;
                        case 2:
                            andFont.setTitle(homeSalary.getMoney());
                            break;
                        case 3:
                            andFont.setTitle(homeSalary.getLastMoney());
                            break;
                    }
                    salaryList.add(andFont);
                }
                mHomeCallbacks.getSalariesData(incomeList, monthList, salaryList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
