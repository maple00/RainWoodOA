package com.rainwood.oa.presenter;

import com.rainwood.oa.base.IBasePresenter;
import com.rainwood.oa.view.IRecordCallbacks;

/**
 * @Author: a797s
 * @Date: 2020/5/25 19:58
 * @Desc: 记录管理
 */
public interface IRecordManagerPresenter extends IBasePresenter<IRecordCallbacks> {

    /**
     * 客户下的加班记录
     *
     * @param customId
     * @param page
     */
    void requestOvertimeRecord(String customId, int page);

    /**
     * 请求记录状态
     */
    void requestOverTimeStateData();

    /**
     * 行政人事--加班记录
     *
     * @param staffId   员工id
     * @param state     状态
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param page      页码
     */
    void requestOvertimeRecord(String staffId, String state, String startTime, String endTime, int page);

    /**
     * 请假记录
     *
     * @param staffId   员工id
     * @param type      请假类型
     * @param state     状态
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param page      页码
     */
    void requestLeaveRecord(String staffId, String type, String state, String startTime, String endTime, int page);

    /**
     * 请求请假的查询条件
     */
    void requestLeaveCondition();

    /**
     * 请求外出记录
     */
    void requestGoOutRecord(String customId, int page);

    /**
     * 行政人事--外出记录
     *
     * @param staffId   员工id
     * @param state     状态
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param page      页码
     */
    void requestGoOutRecord(String staffId, String state, String startTime, String endTime, int page);

    /**
     * 行政人事 -- 外出记录condition
     */
    void requestGoOutCondition();

    /**
     * 行政人事 --- 请求补卡记录
     *
     * @param staffId   员工id
     * @param state     状态
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param page      页码
     */
    void requestReissueRecord(String staffId, String state, String startTime, String endTime, int page);

    /**
     * 行政人事 -- 补卡记录condition
     */
    void requestReissueCondition();

    /**
     * 请求客户的跟进记录
     */
    void requestCustomFollowRecords(String customId);

    /**
     * 请求客户的回款记录
     */
    void requestCustomReceivableRecords(String customId, int page);

    /**
     * 请求客户的回款记录详情
     */
    void requestCustomReceivableRecordDetail(String receivableId);

    /**
     * 请求客户下的开票记录
     */
    void requestCustomInvoiceRecords(String customId, int page);

    /**
     * 财务管理 -- 开票记录
     * @param type 是否已开票
     * @param staffId 员工id
     * @param company 销售方
     * @param invoiceType 发票类型
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param page 页码
     */
    void requestInvoiceRecords(String type, String staffId, String company, String invoiceType,
                               String startTime, String endTime, int page);

    /**
     * 开票记录 -- condition
     */
    void requestInvoiceCondition();

    /**
     * 请求客户下的开票记录中的开票参数
     */
    void requestCustomInvoiceParams();

    /**
     * 新建开票记录
     */
    void CreateInvoiceRecord(String seller, String type, String money, String note,
                             String customId);

    /**
     * 知识管理 -- 跟进记录
     * @param staffId 跟进员工
     * @param target 跟进对象
     * @param searchText 搜索内容
     * @param page 页码
     */
    void requestKnowledgeFollowRecords(String staffId, String target, String searchText, int page);

    /**
     * 请求记录类型
     */
    void requestRecordType();
}
