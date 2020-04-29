package com.rainwood.oa.presenter.impl;

import com.rainwood.oa.model.domain.FontAndFont;
import com.rainwood.oa.network.okhttp.HttpResponse;
import com.rainwood.oa.network.okhttp.OnHttpListener;
import com.rainwood.oa.presenter.IHomePresenter;
import com.rainwood.oa.view.IHomeCallbacks;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/4/29 10:53
 * @Desc: 首页主要内容
 */
public final class HomeMainImpl implements IHomePresenter, OnHttpListener {

    private IHomeCallbacks mHomeCallbacks;

    // 模拟数据
    private List<FontAndFont> mSalaryList;
    private String[] salaries = {"基本工资(元)", "岗位津贴(元)", "会计账户(元)", "结算账户(元)"};
    private String[] salayData = {"2000", "1500", "596.5", "1680"};
    /**
     * 获取首页工资曲线内容
     */
    @Override
    public void getHomeSalaryData() {
        if (mHomeCallbacks != null){
            mHomeCallbacks.onLoading();
        }
        // 模拟数据
        mSalaryList = new ArrayList<>();
        for (int i = 0; i < salaries.length; i++) {
            FontAndFont andFont = new FontAndFont();
            andFont.setTitle(salayData[i]);
            andFont.setDesc(salaries[i]);
            mSalaryList.add(andFont);
        }

        mHomeCallbacks.getSalariesData(mSalaryList);
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

    }
}
