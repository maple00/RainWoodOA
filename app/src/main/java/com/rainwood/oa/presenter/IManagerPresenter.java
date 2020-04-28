package com.rainwood.oa.presenter;

import com.rainwood.oa.base.IBasePresenter;
import com.rainwood.oa.view.IManagerCallbacks;

/**
 * @Author: a797s
 * @Date: 2020/4/28 11:50
 * @Desc: 管理界面presenter
 */
public interface IManagerPresenter extends IBasePresenter<IManagerCallbacks> {

    /**
     * 管理界面数据
     */
    void getManagerData();

}
