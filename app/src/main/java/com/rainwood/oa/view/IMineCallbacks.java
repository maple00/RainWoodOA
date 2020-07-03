package com.rainwood.oa.view;

import com.rainwood.contactslibrary.ContactsBean;
import com.rainwood.oa.base.IBaseCallback;
import com.rainwood.oa.model.domain.AuditRecord;
import com.rainwood.oa.model.domain.Depart;
import com.rainwood.oa.model.domain.DepartStructure;
import com.rainwood.oa.model.domain.FontAndFont;
import com.rainwood.oa.model.domain.IconAndFont;
import com.rainwood.oa.model.domain.MineData;
import com.rainwood.oa.model.domain.MineInfo;
import com.rainwood.oa.model.domain.MineInvoice;
import com.rainwood.oa.model.domain.MineRecordTime;
import com.rainwood.oa.model.domain.MineRecords;
import com.rainwood.oa.model.domain.MineReimbursement;
import com.rainwood.oa.model.domain.SelectedItem;
import com.rainwood.oa.model.domain.TeamFunds;

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
     * 我的---通讯录--- 员工列表
     */
    default void getMineAddressBookData(List<ContactsBean> contactsList) {
    }
    /**
     * 我的---通讯录--- 部门员工
     */
    default void getMineAddressBookDepartData(List<DepartStructure> departStructureList) {
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
    default void getMineReimbursementRecords(List<MineReimbursement> reimbursementList) {
    }

    /**
     * 我的-- 开记录
     */
    default void getMineInvoiceRecords(List<MineInvoice> invoiceList) {
    }

    /**
     * 获取短信验证码
     */
    default void getSmsVerifyCode(String smsId) {
    }

    /***
     * 校验验证码
     */
    default void getVerifySuccess(boolean success) {
    }

    /**
     * 退出登录
     */
    default void getLogout(boolean success) {
    }

    /**
     * 部门职位列表
     * @param departList
     */
    default void getDepartListData(List<Depart> departList){}

    /**
     * 我的请假记录 -- condition
     * @param stateList
     * @param leaveTypeList
     */
    default void getMineLeaveRecords(List<SelectedItem> stateList, List<SelectedItem> leaveTypeList){}
}
