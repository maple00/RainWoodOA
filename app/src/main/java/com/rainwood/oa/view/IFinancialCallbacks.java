package com.rainwood.oa.view;

import com.rainwood.oa.base.IBaseCallback;
import com.rainwood.oa.model.domain.Reimbursement;
import com.rainwood.oa.model.domain.TeamFunds;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/4 14:36
 * @Desc: 财务管理
 */
public interface IFinancialCallbacks extends IBaseCallback {

    /**
     * 获取费用报销数据
     */
    default void getReimburseData(List<Reimbursement> reimbursementList){}

    /**
     * 获取团队基金数据
     */
    default void getTeamFundsData(List<TeamFunds> fundsList){}
}
