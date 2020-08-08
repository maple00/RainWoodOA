package com.rainwood.oa.view;

import com.rainwood.oa.base.IBaseCallback;
import com.rainwood.oa.model.domain.BalanceByMonthOrYear;
import com.rainwood.oa.model.domain.BalanceDetailData;
import com.rainwood.oa.model.domain.BalanceRecord;
import com.rainwood.oa.model.domain.ClassificationStatics;
import com.rainwood.oa.model.domain.ManagerMain;
import com.rainwood.oa.model.domain.MineReimbursement;
import com.rainwood.oa.model.domain.Reimbursement;
import com.rainwood.oa.model.domain.SelectedItem;
import com.rainwood.oa.model.domain.StaffCurve;
import com.rainwood.oa.model.domain.TeamFunds;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/4 14:36
 * @Desc: 财务管理
 */
public interface IFinancialCallbacks extends IBaseCallback {

    /**
     * 获取费用报销数据
     */
    default void getReimburseData(List<Reimbursement> reimbursementList) {
    }

    /**
     * 费用报销 -- condition
     */
    default void getReimburseCondition(List<SelectedItem> typeList, List<SelectedItem> payerList) {
    }

    /**
     * 获取团队基金数据
     */
    default void getTeamFundsData(List<TeamFunds> fundsList, String money) {
    }

    /**
     * 行政处罚结果
     */
    default void getPunishResult(boolean success) {
    }

    /**
     * 收支记录列表
     *
     * @param balanceRecordList
     */
    default void getBalanceRecords(List<BalanceRecord> balanceRecordList) {
    }

    /**
     * 分类统计 ---
     *
     * @param incomeList
     * @param incomeMoney
     * @param classificationOutcomeList
     * @param balance
     */
    default void getClassificationIncome(List<ClassificationStatics> incomeList, String incomeMoney,
                                         List<ClassificationStatics> classificationOutcomeList, String outcomeMoney, String balance) {
    }

    /**
     * 收支平衡
     * @param originList
     * @param balanceTypeList
     * @param showDepart
     */
    default void getInOutComeData(List<SelectedItem> originList, List<ManagerMain> balanceTypeList, String showDepart) {
    }

    /**
     * 收支曲线 -- 按月
     * @param balanceYearMonth
     * @param monthBalanceList
     */
    default void getBalanceMonthData(List<String> balanceYearMonth, List<BalanceByMonthOrYear> monthBalanceList){}

    /**
     * 收支曲线 -- 按年
     * @param balanceYearMonth
     * @param monthBalanceList
     */
    default void getBalanceYearData(List<String> balanceYearMonth, List<BalanceByMonthOrYear> monthBalanceList){}

    /**
     * 员工数量曲线
     * @param xValues
     * @param staffNumList
     */
    default void getStaffNumByCurve(List<String> xValues, List<StaffCurve> staffNumList){};

    /**
     * 费用报销详情
     * @param reimbursement
     */
    default void getReimburseDetail(MineReimbursement reimbursement){}

    /**
     * 收支记录详情
     * @param balanceDetailData
     */
    default void getBalanceDetailData(BalanceDetailData balanceDetailData){};
}
