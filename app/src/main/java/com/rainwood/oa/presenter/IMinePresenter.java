package com.rainwood.oa.presenter;

import com.rainwood.oa.base.IBasePresenter;
import com.rainwood.oa.view.IMineCallbacks;

/**
 * @Author: a797s
 * @Date: 2020/4/28 16:00
 * @Desc: 我的
 */
public interface IMinePresenter extends IBasePresenter<IMineCallbacks> {

    /**
     * 请求个人中心数据
     */
    void getMineData();

    /**
     * 请求我的个人资料数据
     */
    void requestMineInfo();

    /**
     * 请求通讯录数据
     */
    void requestAddressBookData();

    /**
     * 部门管理列表
     */
    void requestAllDepartData();

    /**
     * 请求我的会计账户
     */
    void requestAccountingAccount(String type);

    /**
     * 请求我的结算账户
     */
    void requestSettlementAccount(String  type);

    /**
     * 请求我的补卡记录
     */
    void requestMineReissueCards(String state);

    /**
     * 我的补卡记录申请
     */
    void requestMineReissueApply();

    /**
     * 请求审核记录列表
     */
    void requestAuditRecords(String recordId);

    /**
     * 请求我的请假列表
     */
    void requestAskLeaveRecords();

    /**
     * 请求我的加班列表
     */
    void requestMineOverTimeRecords();

    /**
     * 请求我的外出记录列表
     */
    void requestMineLeaveOutRecords();

    /**
     * 请求我的费用报销
     */
    void requestMineReimburseData();

    /**
     * 请求我的开票记录
     */
    void requestMineInvoiceRecords();

    /**
     * 发送验证码
     */
    void requestSmsVerifyCode();

    /**
     * 验证短信验证码
     */
    void requestCheckedSms(String currentPwd, String newPwd, String confirmPwd, String verifyCode, String smsSecret);

    /**
     * 退出登录
     */
    void requestLogout();

}
