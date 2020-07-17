package com.rainwood.oa.presenter;

import com.rainwood.oa.base.IBasePresenter;
import com.rainwood.oa.view.IOrderCallbacks;

/**
 * @Author: a797s
 * @Date: 2020/5/19 16:51
 * @Desc:
 */
public interface IOrderPresenter extends IBasePresenter<IOrderCallbacks> {

    /**
     * 请求所有的审批人
     */
    void requestAllExaminationData();

    /**
     * 请求所有订单的统计信息
     */
    // void requestOrderData();

    /**
     * 请求客户下的订单列表
     */
    void requestCustomOrderList(String customId);

    /**
     * 新建订单
     */
    void CreateNewOrder(String customId, String orderId, String orderNameStr, String moneyStr, String noteStr);

    /**
     * 通过客户关键字查询客户名称
     */
    void requestCustomName(String key);

    /**
     * 订单列表
     * @param orderName 搜索关键字
     * @param state 状态
     * @param staffId 部门员工
     * @param sorting 排序
     */
    void requestOrderList(String orderName, String state, String staffId, String sorting, int page);

    /**
     * 条件查询
     */
    void requestCondition();

    /**
     * 请求订单详情
     */
    void requestOrderDetailById(String orderId);

}
