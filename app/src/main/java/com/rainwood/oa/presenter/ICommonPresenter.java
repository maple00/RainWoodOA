package com.rainwood.oa.presenter;

import com.rainwood.oa.base.IBasePresenter;
import com.rainwood.oa.view.ICommonCallbacks;

import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/5/26 14:21
 * @Desc: 公共数据(客户跟进状态、加班详情)
 */
public interface ICommonPresenter extends IBasePresenter<ICommonCallbacks> {

    /**
     * 客户跟进状态
     */
    void AuditRecords();

    /**
     * 行政人事 --- 加班记录
     */
    void requestOverTimeRecordsById(String overTimeId);

    /**
     * 行政人事-- 请假详情
     */
    void requestAskLeaveById(String askLeaveId);

    /**
     * 行政人事 -- 外出详情
     */
    void requestAskOutByStaffId(String staffId);

    /**
     * 行政人事 -- 补卡记录详情
     */
    void requestReissueCardDetailById(String reissueId);
}
