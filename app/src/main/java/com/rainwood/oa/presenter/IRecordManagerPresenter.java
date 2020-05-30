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
     * 请求加班记录
     */
    void requestOvertimeRecord(String customId);

    /**
     * 请求请假记录
     */
    void requestLeaveRecord();

    /**
     * 请求外出记录
     */
    void requestGoOutRecord(String customId);

    /**
     * 请求补卡记录
     */
    void requestReissueRecord();

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
}
