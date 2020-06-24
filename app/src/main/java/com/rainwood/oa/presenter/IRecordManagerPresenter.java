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
     */
    void requestOvertimeRecord(String customId);

    /**
     * 请求记录状态
     */
    void requestOverTimeStateData();

    /**
     * 行政人事--加班记录
     */
    void requestOvertimeRecord();

    /**
     * 请求请假记录
     */
    void requestLeaveRecord();

    /**
     * 请求请假的查询条件
     */
    void requestLeaveCondition();

    /**
     * 请求外出记录
     */
    void requestGoOutRecord(String customId);

    /**
     * 行政人事--外出记录
     */
    void requestGoOutRecord();

    /**
     * 行政人事 -- 外出记录condition
     */
    void requestGoOutCondition();

    /**
     * 请求补卡记录
     */
    void requestReissueRecord();

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
    void requestCustomReceivableRecords(String customId);

    /**
     * 请求客户的回款记录详情
     */
    void requestCustomReceivableRecordDetail(String receivableId);

    /**
     * 请求客户下的开票记录
     */
    void requestCustomInvoiceRecords(String customId);

    /**
     * 请求开票记录（财务管理）
     */
    void requestInvoiceRecords(String type);

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
     * 知识管理中的跟进记录
     */
    void requestKnowledgeFollowRecords();

}
