package com.rainwood.oa.presenter;

import com.rainwood.oa.base.IBasePresenter;
import com.rainwood.oa.view.IManagerSystemCallbacks;

/**
 * @Author: a797s
 * @Date: 2020/5/25 19:03
 * @Desc: 管理制度
 */
public interface IManagerSystemPresenter extends IBasePresenter<IManagerSystemCallbacks> {

    /**
     * 请求管理制度的内容
     */
    void requestAllData();
}
