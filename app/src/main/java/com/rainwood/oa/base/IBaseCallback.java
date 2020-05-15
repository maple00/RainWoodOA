package com.rainwood.oa.base;

/**
 * @Author: a797s
 * @Date: 2020/4/27 16:21
 * @Desc: 状态接口基类
 */
public interface IBaseCallback {

    /**
     * 数据错误
     */
    void onError();

    /**
     * 加载中
     */
    void onLoading();

    /**
     * 空数据
     */
    void onEmpty();
}
