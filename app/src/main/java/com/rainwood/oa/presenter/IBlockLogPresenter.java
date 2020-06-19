package com.rainwood.oa.presenter;

import com.rainwood.oa.base.IBasePresenter;
import com.rainwood.oa.view.IBlockLogCallbacks;

/**
 * @Author: a797s
 * @Date: 2020/6/17 14:37
 * @Desc: 待办事项
 */
public interface IBlockLogPresenter extends IBasePresenter<IBlockLogCallbacks> {

    /**
     * 请求待办事项状态
     */
    void requestStateData();

    /**
     * 请求待办事项详情
     */
    void requestBlockDetail(String blockId);

}
