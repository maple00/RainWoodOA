package com.rainwood.oa.presenter;

import com.rainwood.oa.base.IBasePresenter;
import com.rainwood.oa.view.ISearchCallback;

/**
 * @Author: a797s
 * @Date: 2020/6/30 15:20
 * @Desc: 搜索 presenter
 */
public interface ISearchPresenter extends IBasePresenter<ISearchCallback> {

    /**
     * 角色列表搜索
     */
    void requestSearchRoleList(String roleName, String moduleOne, String moduleTwo);
}
