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
    void requestAccountingAccount(String searchText, String type, String startTime, String endTime, int page);

    /**
     * 请求我的结算账户
     */
    void requestSettlementAccount(String searchText, String type, String startTime, String endTime, int page);

    /**
     * 请求我的补卡记录
     */
    void requestMineReissueCards(String state, String startTime, String endTime, int page);

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
     *
     * @param state     状态
     * @param type      类型
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param page      页码
     */
    void requestAskLeaveRecords(String state, String type, String startTime, String endTime, int page);

    /**
     * 我的请假记录 -- condition
     */
    void requestMineLeaveCondition();

    /**
     * 请求我的加班列表
     * @param stateText
     * @param startTime
     * @param endTime
     * @param page
     */
    void requestMineOverTimeRecords(String stateText, String startTime, String endTime, int page);


    /**
     * 请求我的外出记录列表
     * @param stateText
     * @param startTime
     * @param endTime
     * @param page
     */
    void requestMineLeaveOutRecords(String stateText, String startTime, String endTime, int page);

    /**
     * 我的费用报销
     * @param text 备注
     * @param type 类型
     * @param payer 付款方
     * @param payState 支付状态
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param page 页码
     */
    void requestMineReimburseData(String text, String type, String payer, String payState,
                                  String startTime, String endTime, int page);

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

    /**
     * 请假申请
     *
     * @param leaveType   请假类型
     * @param startTime   开始时间
     * @param endTime     结束时间
     * @param leaveReason 请假事由
     */
    void createMineLeaveRecord(String id, String leaveType, String startTime, String endTime, String leaveReason);

    /**
     * 删除请假记录
     * @param id
     */
    void delMineLeaveRecord(String id);

    /**
     * 加班申请
     * @param overtimeId 随机id
     * @param startTimeStr 开始时间
     * @param endTimeStr 结束时间
     * @param reasonStr 加班事由
     */
    void createMineOvertimeApply(String overtimeId, String startTimeStr, String endTimeStr, String reasonStr);

    /**
     * 外出申请
     * @param recordId
     * @param startTimeStr
     * @param endTimeStr
     * @param reasonStr
     */
    void createMineLeaveOutApply(String recordId, String startTimeStr, String endTimeStr, String reasonStr);
}
