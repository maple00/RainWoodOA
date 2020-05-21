package com.rainwood.oa.presenter;

import com.rainwood.oa.base.IBasePresenter;
import com.rainwood.oa.view.ICommunicationCallbacks;

/**
 * @Author: a797s
 * @Date: 2020/5/21 11:49
 * @Desc: 沟通技巧模块
 */
public interface ICommunicationPresenter extends IBasePresenter<ICommunicationCallbacks> {

    /**
     * 请求所有的数据
     */
    void getAllData();
}
