package com.rainwood.oa.view;

import com.rainwood.oa.base.IBaseCallback;
import com.rainwood.oa.model.domain.Custom;
import com.rainwood.oa.model.domain.CustomOrder;

import java.util.List;
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

    /**
     * 获取客户下的订单列表
     */
    default void getCustomOrderList(List<CustomOrder> customOrderList){};
}
