package com.rainwood.oa.presenter;

import com.rainwood.oa.base.IBasePresenter;
import com.rainwood.oa.view.ICustomListCallbacks;

/**
 * @Author: a797s
 * @Date: 2020/5/18 14:46
 * @Desc:
 */
public interface ICustomListPresenter extends IBasePresenter<ICustomListCallbacks> {

    /**
     * 请求客户列表
     */
    void getALlCustomData();

    /**
     * 查询状态
     */
    void getStatus();
}
