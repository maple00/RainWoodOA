package com.rainwood.oa.view;

import com.rainwood.oa.base.IBaseCallback;

import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/5/26 14:13
 * @Desc: 公用数据请求-- 大多数是选择框选择数据
 */
public interface ICommonCallbacks extends IBaseCallback {

    /**
     * 审核记录
     */
    void getAuditRecords(Map recordsMap);
}
