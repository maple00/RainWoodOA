package com.rainwood.oa.presenter;

import com.rainwood.oa.base.IBasePresenter;
import com.rainwood.oa.view.IAdministrativeCallbacks;

/**
 * @Author: a797s
 * @Date: 2020/5/21 14:09
 * @Desc: 行政事务----角色管理、部门管理、职位管理、工作日、通讯录
 */
public interface IAdministrativePresenter extends IBasePresenter<IAdministrativeCallbacks> {

    /**
     * 请求角色列表的所有角色信息
     */
    void requestAllRole();

    /**
     * 部门管理列表
     */
    void requestAllDepartData();

    /**
     * 通过部门id查询部门详情
     */
    void requestDepartDetailById(String departId);

    /**
     * 职位列表
     */
    void requestPostListData();

    /**
     * 通过职位id查询职位详情
     * @param postId
     */
    void requestPostDetailById(String postId);
}
