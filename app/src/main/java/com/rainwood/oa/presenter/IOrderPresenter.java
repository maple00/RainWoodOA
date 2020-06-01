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
    void requestOrderData();

    /**
     * 请求客户下的订单列表
     */
    void requestCustomOrderList(String customId);

    /**
     * 新建订单
     */
    void CreateNewOrder();

    /**
     * 通过客户关键字查询客户名称
     */
    void requestCustomName(String key);
}
