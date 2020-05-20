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
}
