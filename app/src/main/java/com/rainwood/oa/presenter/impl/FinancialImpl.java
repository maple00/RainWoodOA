package com.rainwood.oa.presenter.impl;

import com.rainwood.oa.model.domain.Reimbursement;
import com.rainwood.oa.model.domain.TeamFunds;
import com.rainwood.oa.presenter.IFinancialPresenter;
import com.rainwood.oa.view.IFinancialCallbacks;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/4 15:06
 * @Desc:
 */
public final class FinancialImpl implements IFinancialPresenter {

    private IFinancialCallbacks mFinancialCallbacks;

    @Override
    public void requestReimburseData() {
        // 模拟报销费用
        List<Reimbursement> reimbursementList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Reimbursement reimbursement = new Reimbursement();
            reimbursement.setVoucher(true);
            reimbursement.setType("广告费");
            reimbursement.setMoney("￥20000");
            reimbursement.setName("贾科斯");
            reimbursement.setContent("桶装水没有了，新送了5桶桶装水");
            reimbursement.setAllocated("已拨付");
            reimbursement.setTime("2020.03.31");
            reimbursementList.add(reimbursement);
        }

        mFinancialCallbacks.getReimburseData(reimbursementList);
    }

    @Override
    public void requestTeamFundsData() {
        // 模拟团队基金数据
        List<TeamFunds> fundsList = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            TeamFunds funds = new TeamFunds();
            funds.setContent("为员工购买的加班福利，牛奶面包为员工购买的加班福利，牛奶面包");
            funds.setMoney("-283.50");
            funds.setTime("2020.03.17 15:56:00");
            fundsList.add(funds);
        }
        mFinancialCallbacks.getTeamFundsData(fundsList);
    }

    @Override
    public void registerViewCallback(IFinancialCallbacks callback) {
        mFinancialCallbacks = callback;
    }

    @Override
    public void unregisterViewCallback(IFinancialCallbacks callback) {
        mFinancialCallbacks = null;
    }
}
