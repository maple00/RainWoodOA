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
     * 财务管理 -- 费用报销
     *
     * @param allocated  拨付状态 -- 默认查询所有
     * @param staffId    员工ID
     * @param type       类型
     * @param payer      付款方
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @param searchText 搜索内容
     * @param page       页码
     */
    void requestReimburseData(String allocated, String staffId, String type, String payer, String startTime, String endTime,
                              String searchText, int page);

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

    /**
     * 收支记录列表
     *
     * @param staffId    员工id
     * @param origin     来源
     * @param classify   分类
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @param searchText 搜索内容
     * @param page       页码
     */
    void requestBalanceRecords(String staffId, String origin, String classify, String startTime,
                               String endTime, String searchText, int page);

    /**
     * 收支记录列表 condition
     */
    void requestBalanceCondition();

    /**
     * 请求分类统计数据
     */
    void requestClassStatics(String startTime, String endTime);

    /**
     * 请求收支曲线 -- 按月
     * @param startMonth 开始月份
     * @param endMonth 结束月份
     */
    void requestBalanceByMonth(String startMonth, String endMonth);

    /**
     * 请求收支曲线 -- 按年
     */
    void requestBalanceByYear();

    /**
     * 请求员工数量
     */
    void requestStaffNum();

    /**
     * 请i去费用报销凭证
     * @param reimburseId
     */
    void requestReimburseDetail(String reimburseId);
}
