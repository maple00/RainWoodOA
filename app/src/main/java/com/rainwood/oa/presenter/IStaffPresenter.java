package com.rainwood.oa.presenter;

import com.rainwood.oa.base.IBasePresenter;
import com.rainwood.oa.view.IStaffCallbacks;

/**
 * @Author: a797s
 * @Date: 2020/5/22 11:34
 * @Desc: 员工管理
 */
public interface IStaffPresenter extends IBasePresenter<IStaffCallbacks> {

    /**
     * 请求所有的部门信息
     */
    void requestAllDepartData();

    /**
     * 请求所有的员工
     */
    void requestAllStaff();
}
