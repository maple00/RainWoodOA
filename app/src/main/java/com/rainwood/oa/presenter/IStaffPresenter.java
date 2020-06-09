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
    void requestAllStaff(String postId);

    /**
     * 请求员工的所有照片
     */
    void requestStaffPhoto();

    /**
     * 请求员工的所有的工作经历
     */
    void requestExperience();

    /**
     * 请求员工账户类型
     */
    void requestAccountType();

    /**
     * 获取员工的所有的会计账户
     */
    void requestAllAccountData();

    /**
     * 获取员工的所有的计算账户的信息
     */
    void requestAllSettlementData();
}
