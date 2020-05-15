package com.rainwood.oa.presenter;

import com.rainwood.oa.base.IBasePresenter;
import com.rainwood.oa.view.ICustomDetailCallbacks;

/**
 * create by a797s in 2020/5/15 10:37
 *
 * @Description : 定义客户详情
 * @Usage :
 **/
public interface ICustomDetailPresenter extends IBasePresenter<ICustomDetailCallbacks> {

    /**
     * 获取详情数据
     */
    void getDetailData();
}
