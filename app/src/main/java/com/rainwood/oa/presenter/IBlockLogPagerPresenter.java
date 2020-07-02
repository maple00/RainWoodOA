package com.rainwood.oa.presenter;

import com.rainwood.oa.base.IBasePresenter;
import com.rainwood.oa.view.IBlockLogPagerCallbacks;

/**
 * @Author: a797s
 * @Date: 2020/6/19 10:17
 * @Desc:
 */
public interface IBlockLogPagerPresenter extends IBasePresenter<IBlockLogPagerCallbacks> {

    /**
     * 请求状态内容
     * @param state
     */
    void requestBlockData(String state);
}
