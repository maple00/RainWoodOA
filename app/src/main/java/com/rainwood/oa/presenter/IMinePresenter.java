package com.rainwood.oa.presenter;

import com.rainwood.oa.base.IBasePresenter;
import com.rainwood.oa.view.IMineCallbacks;

/**
 * @Author: a797s
 * @Date: 2020/4/28 16:00
 * @Desc: 我的
 */
public interface IMinePresenter extends IBasePresenter<IMineCallbacks> {

    /**
     * 我的界面数据
     */
    void getMineData();
}
