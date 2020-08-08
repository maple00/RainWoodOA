package com.rainwood.oa.view;

import com.rainwood.oa.base.IBaseCallback;
import com.rainwood.oa.model.domain.AdminLeaveOut;
import com.rainwood.oa.model.domain.AdminOverTime;
import com.rainwood.oa.model.domain.CardRecord;
import com.rainwood.oa.model.domain.CustomFollowRecord;
import com.rainwood.oa.model.domain.FinancialInvoiceRecord;
import com.rainwood.oa.model.domain.InvoiceDetailData;
import com.rainwood.oa.model.domain.InvoiceRecord;
import com.rainwood.oa.model.domain.KnowledgeFollowRecord;
import com.rainwood.oa.model.domain.LeaveOutRecord;
import com.rainwood.oa.model.domain.LeaveRecord;
import com.rainwood.oa.model.domain.OvertimeRecord;
import com.rainwood.oa.model.domain.ReceivableRecord;
import com.rainwood.oa.model.domain.SelectedItem;

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
    default void getOvertimeRecords(List<OvertimeRecord> overtimeRecords) {
    }

    /**
     * 获取所有的请假记录
     */
    default void getLeaveRecords(List<LeaveRecord> leaveRecordList) {
    }

    /**
     * 请假记录 condition
     *
     * @param stateList
     * @param leaveTypeList
     */
    default void getLeaveConditionData(List<SelectedItem> stateList, List<SelectedItem> leaveTypeList) {
    }

    /**
     * 获取所有的外出记录
     */
    default void getGoOutRecords(List<LeaveOutRecord> leaveOutList) {
    }

    /**
     * 行政人事--外出记录
     *
     * @param adminLeaveOutList
     */
    default void getAdminLeaveOutRecords(List<AdminLeaveOut> adminLeaveOutList) {
    }

    /**
     * 行政人事 -- 外出记录condition
     */
    default void getLeaveOutCondition(List<SelectedItem> leaveOutList) {
    }

    /**
     * 获取所有的补卡记录
     */
    default void getReissueRecords(List<CardRecord> cardRecordList) {
    }

    /**
     * 补卡记录的状态
     *
     * @param reissueStateList
     */
    default void getReissueCondition(List<SelectedItem> reissueStateList) {
    }

    /**
     * 获取客户的跟进记录
     */
    default void getCustomFollowRecords(List<CustomFollowRecord> recordList) {
    }

    /**
     * 获取客户的回款记录
     */
    default void getCustomReceivableRecords(List<ReceivableRecord> receivableRecordList) {
    }

    /**
     * 获取客户回款记录详情
     */
    default void getCustomReceivableRecordDetail(ReceivableRecord receivableRecord) {
    }

    /**
     * 客户下的开票记录
     */
    default void getCustomInvoiceRecords(List<InvoiceRecord> invoiceRecordList) {
    }

    /**
     * 财务管理 --- 开票记录
     *
     * @param financialInvoiceRecords
     */
    default void getFinancialInvoiceRecords(List<FinancialInvoiceRecord> financialInvoiceRecords) {
    }

    /**
     * 财务管理 -- 开票记录condition
     */
    default void getInvoiceCondition(List<SelectedItem> saleList, List<SelectedItem> typeList) {
    }

    /**
     * 客户新建开票记录的页面数据
     */
    default void getCustomNewInvoiceRecordsPageParams(Map pageMap) {
    }

    /**
     * 创建客户开票记录
     *
     * @param flag
     */
    default void createInvoiceRecord(boolean flag) {
    }

    /**
     * 知识管理--跟进记录
     */
    default void getKnowledgeFollowRecords(List<KnowledgeFollowRecord> recordList) {
    }

    /**
     * 知识管理 --- 跟进记录 （记录类型）
     *
     * @param typeList
     */
    default void getRecordsTypes(List<SelectedItem> typeList) {
    }

    /**
     * 行政人事--- 加班记录
     *
     * @param adminOverTimeList
     */
    default void getAdminOverTimeRecords(List<AdminOverTime> adminOverTimeList) {
    }

    /**
     * 行政人事 -- 加班状态
     *
     * @param overTimeStateList
     */
    default void getAdminOverTimeState(List<SelectedItem> overTimeStateList) {
    }

    /**
     * 开票记录详情
     * @param invoiceDetail
     */
    default void getInvoiceDetail(InvoiceDetailData invoiceDetail){}
}
