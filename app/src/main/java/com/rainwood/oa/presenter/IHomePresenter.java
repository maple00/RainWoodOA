package com.rainwood.oa.presenter;

import com.rainwood.oa.base.IBasePresenter;
import com.rainwood.oa.view.IHomeCallbacks;

/**
 * @Author: a797s
 * @Date: 2020/4/29 10:51
 * @Desc: 首页
 */
public interface IHomePresenter extends IBasePresenter<IHomeCallbacks> {

    /**
     * 请求工资数据
     *
     * @param startMonth 开始月份
     * @param endMonth   结束月份
     */
    void requestSalaryData(String startMonth, String endMonth);
}
