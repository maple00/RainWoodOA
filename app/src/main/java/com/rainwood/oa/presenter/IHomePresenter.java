package com.rainwood.oa.presenter;

import com.rainwood.oa.base.IBasePresenter;
import com.rainwood.oa.view.IHomeCallbacks;

/**
 * @Author: a797s
 * @Date: 2020/4/29 10:51
 * @Desc: 首页
 */
public interface IHomePresenter extends IBasePresenter<IHomeCallbacks> {

    /**
     * 获取工资曲线内容
     */
    void getHomeSalaryData();
}
