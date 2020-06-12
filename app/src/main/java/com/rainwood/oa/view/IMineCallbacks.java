package com.rainwood.oa.view;

import com.rainwood.oa.base.IBaseCallback;
import com.rainwood.oa.model.domain.AuditRecord;
import com.rainwood.oa.model.domain.FontAndFont;
import com.rainwood.oa.model.domain.IconAndFont;
import com.rainwood.oa.model.domain.MineData;
import com.rainwood.oa.model.domain.MineInfo;
import com.rainwood.oa.model.domain.MineInvoice;
import com.rainwood.oa.model.domain.MineRecordTime;
import com.rainwood.oa.model.domain.MineRecords;
import com.rainwood.oa.model.domain.MineReimbursement;
import com.rainwood.oa.model.domain.TeamFunds;
import com.rainwood.oa.utils.ListUtils;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/4/28 16:03
 * @Desc: 我的
 */
public interface IMineCallbacks extends IBaseCallback {

    /**
     * 个人中心--界面数据
     */
    default void getMenuData(MineData mineData, List<FontAndFont> accountList) {
    }

    ;

    /**
     * 个人中心-- 我的模块
     */
    default void getMineModule(List<IconAndFont> iconAndFonts) {
    }

    /**
     * 我的--个人资料
     */
    default void getMineInfo(MineInfo mineInfo) {
    }

    /**
     * 我的--会计账户
     */
    default void getAccountListData(List<TeamFunds> accountList, String money) {
    }

    /**
     * 我的--补卡记录
     */
    default void getMineReissueRecords(List<MineRecords> reissueList) {
    }

    /**
     * 我的-- 补卡记录--审核记录
     */
    default void getMineAuditRecords(List<AuditRecord> auditRecordList) {
    }

    /**
     * 我的-- 请假记录
     */
    default void getMineAskLeaveRecords(List<MineRecords> askLeaveList) {
    }

    /**
     * 我的-- 加班记录
     */
    default void getMineOverTimeRecords(List<MineRecordTime> recordTimeList) {
    }

    /**
     * 我的--费用报销
     */
    default void getMineReimbursementRecords(List<MineReimbursement> reimbursementList){}

    /**
     * 我的-- 开记录
     */
    default void getMineInvoiceRecords(List<MineInvoice> invoiceList){}
}
