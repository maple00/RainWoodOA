package com.rainwood.oa.view;

import com.rainwood.oa.base.IBaseCallback;
import com.rainwood.oa.model.domain.CustomFollowRecord;
import com.rainwood.oa.model.domain.InvoiceRecord;
import com.rainwood.oa.model.domain.LeaveOutRecord;
import com.rainwood.oa.model.domain.OvertimeRecord;
import com.rainwood.oa.model.domain.ReceivableRecord;

import java.util.List;
import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/5/25 19:54
 * @Desc: 记录callback----加班记录、请假记录、外出记录、补卡记录
 */
public interface IRecordCallbacks extends IBaseCallback {

    /**
     * 获取所有的加班记录
     */
    default void getOvertimeRecords(List<OvertimeRecord> overtimeRecords){}

    /**
     * 获取所有的请假记录
     */
    default void getLeaveRecords(Map recordMap){}

    /**
     * 获取所有的外出记录
     */
    default void getGoOutRecords(List<LeaveOutRecord> leaveOutList){}

    /**
     * 获取所有的补卡记录
     */
    default void getReissueRecords(Map reissueMap){}

    /**
     * 获取客户的跟进记录
     */
    default void getCustomFollowRecords(List<CustomFollowRecord> recordList){}

    /**
     * 获取客户的回款记录
     */
    default void getCustomReceivableRecords(List<ReceivableRecord> receivableRecordList){};

    /**
     * 获取客户回款记录详情
     */
    default void getCustomReceivableRecordDetail(ReceivableRecord receivableRecord){}

    /**
     * 客户下的开票记录
     */
    default void getCustomInvoiceRecords(List<InvoiceRecord> invoiceRecordList){};

    /**
     * 客户新建开票记录的页面数据
     */
    default void getCustomNewInvoiceRecordsPageParams(Map pageMap){}

    /**
     * 创建客户开票记录
     * @param flag
     */
    default void createInvoiceRecord(boolean flag){};
}
