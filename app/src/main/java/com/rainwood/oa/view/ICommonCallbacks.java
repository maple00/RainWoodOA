package com.rainwood.oa.view;

import com.rainwood.oa.base.IBaseCallback;
import com.rainwood.oa.model.domain.RecordsDetail;

import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/5/26 14:13
 * @Desc: 公用数据请求-- 大多数是选择框选择数据
 */
public interface ICommonCallbacks extends IBaseCallback {

    /**
     * 审核记录
     */
    void getAuditRecords(Map recordsMap);

    /**
     * 加班详情
     */
    default void getOverTimeDetail(RecordsDetail recordsDetail){}

    /**
     * 请假详情
     */
    default void getAskLeaveDetailData(RecordsDetail recordsDetail){}

    /**
     * 外出详情
     */
    default void getAskOutDetailData(RecordsDetail recordsDetail){}

    /**
     * 补卡记录详情
     * @param recordsDetail
     */
    default void getReissueDetailData(RecordsDetail recordsDetail){}
}
