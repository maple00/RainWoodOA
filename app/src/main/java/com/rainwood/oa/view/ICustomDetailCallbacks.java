package com.rainwood.oa.view;

import com.rainwood.oa.base.IBaseCallback;

import java.util.List;
import java.util.Map;

/**
 * create by a797s in 2020/5/15 10:40
 *
 * @Description : 客户详情回调
 * @Usage :
 **/
public interface ICustomDetailCallbacks extends IBaseCallback {

    /**
     * 客户详情数据回调
     */
    void getCustomDetailData(Map<String, List> contentMap);


}
