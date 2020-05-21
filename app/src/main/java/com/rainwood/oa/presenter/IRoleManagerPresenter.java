package com.rainwood.oa.presenter;

import com.rainwood.oa.base.IBasePresenter;
import com.rainwood.oa.view.IRoleManagerCallbacks;

/**
 * @Author: a797s
 * @Date: 2020/5/21 14:09
 * @Desc: 角色管理
 */
public interface IRoleManagerPresenter extends IBasePresenter<IRoleManagerCallbacks> {

    /**
     * 请求角色列表的所有角色信息
     */
    void requestAllRole();

    /**
     * 查询指定角色的权限
     */
}
