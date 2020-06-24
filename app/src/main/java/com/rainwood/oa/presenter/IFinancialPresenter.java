package com.rainwood.oa.presenter;

import com.rainwood.oa.base.IBasePresenter;
import com.rainwood.oa.view.IFinancialCallbacks;

/**
 * @Author: a797s
 * @Date: 2020/6/4 15:03
 * @Desc: 财务管理
 */
public interface IFinancialPresenter extends IBasePresenter<IFinancialCallbacks> {

    /**
     * 请求费用报销数据
     */
    void requestReimburseData();

    /**
     * 请求团队基金数据
     */
    void requestTeamFundsData(String direction);

    /**
     * 行政处罚
     *
     * @param staffId
     * @param money
     * @param reason
     * @param password
     */
    void requestPunishStaff(String staffId, String money, String reason, String password);

    /**
     * 费用报销 --- 类型condition
     */
    void requestReimburseCondition();
}
