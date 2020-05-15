package com.rainwood.oa.presenter;

import com.rainwood.oa.base.IBasePresenter;
import com.rainwood.oa.view.IContactCallbacks;

/**
 * create by a797s in 2020/5/15 16:49
 *
 * @Description : 联系人
 * @Usage :
 **/
public interface IContactPresenter extends IBasePresenter<IContactCallbacks> {

    /**
     * 获取所有的联系人
     */
    void getAllContact();
}
