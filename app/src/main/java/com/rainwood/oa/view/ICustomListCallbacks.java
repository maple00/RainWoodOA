package com.rainwood.oa.view;

import com.rainwood.oa.base.IBaseCallback;

import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/5/18 14:45
 * @Desc:
 */
public interface ICustomListCallbacks extends IBaseCallback {

    /**
     * 获取客户列表
     *
     * @param customListValues
     */
    void getAllCustomList(Map customListValues);

    /**
     * 获取所有的状态
     */
    void getALlStatus(Map statusMap);
}
