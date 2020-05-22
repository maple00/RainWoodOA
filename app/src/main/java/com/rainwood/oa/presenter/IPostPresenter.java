package com.rainwood.oa.presenter;

import com.rainwood.oa.base.IBasePresenter;
import com.rainwood.oa.view.IPostCallbacks;

/**
 * @Author: a797s
 * @Date: 2020/5/22 9:55
 * @Desc: 职位管理
 */
public interface IPostPresenter extends IBasePresenter<IPostCallbacks> {

    /**
     * 请求所有的职位信息
     */
    void getAllPost();
}
