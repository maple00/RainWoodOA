package com.rainwood.oa.presenter;

import com.rainwood.oa.base.IBasePresenter;
import com.rainwood.oa.view.ILogcatCallbacks;

/**
 * @Author: a797s
 * @Date: 2020/5/28 9:44
 * @Desc: 系统设置
 */
public interface ILogcatPresenter extends IBasePresenter<ILogcatCallbacks> {

    /**
     * 请求获取系统日志
     * @param searchText
     * @param typeOne
     * @param typeTwo
     * @param staffId
     * @param startTime
     * @param endTime
     * @param pageCount
     */
    void requestLogcatData(String searchText, String typeOne, String typeTwo, String staffId,
                           String startTime, String endTime, int pageCount);

    /**
     * 请求查询日志类型
     */
    void requestLogcatType();

    /**
     * 请求是否展示部门员工
     */
    void requestHasShowDepart();
}
