package com.rainwood.oa.presenter;

import com.rainwood.oa.base.IBasePresenter;
import com.rainwood.oa.view.ILoginAboutCallbacks;

/**
 * @Author: a797s
 * @Date: 2020/5/19 10:01
 * @Desc:
 */
public interface ILoginAboutPresenter extends IBasePresenter<ILoginAboutCallbacks> {

    /**
     * 请求登录
     *
     * @param userName 用户名
     * @param password 密码
     */
    void Login(String userName, String password);
}
