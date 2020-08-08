package com.rainwood.oa.view;

import com.rainwood.oa.base.IBaseCallback;

/**
 * @Author: a797s
 * @Date: 2020/5/19 9:52
 * @Desc: 登录相关--登录，忘记密码
 */
public interface ILoginAboutCallbacks extends IBaseCallback {

    /**
     * 登录
     * @param life
     */
    void Login(String life);

    default void getVerifyResult(boolean warn){}
}
