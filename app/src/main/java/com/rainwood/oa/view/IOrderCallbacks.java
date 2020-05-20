package com.rainwood.oa.view;

import com.rainwood.oa.base.IBaseCallback;

import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/5/19 16:49
 * @Desc: 订单callbacks
 */
public interface IOrderCallbacks extends IBaseCallback {

    /**
     * 获取所有的审批人
     *
     * @param examinationData
     */
    default void getAllExaminationData(Map examinationData) {
    }

    /**
     * 获取所有的订单统计信息
     *
     * @param orderListData 订单列表信息
     */
    default void getAllOrderPage(Map orderListData) {
    }
}
