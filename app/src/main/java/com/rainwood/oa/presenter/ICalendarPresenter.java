package com.rainwood.oa.presenter;

import com.rainwood.oa.base.IBasePresenter;
import com.rainwood.oa.view.ICalendarCallbacks;

/**
 * @Author: a797s
 * @Date: 2020/6/10 13:51
 * @Desc: 日历相关 presenter
 */
public interface ICalendarPresenter extends IBasePresenter<ICalendarCallbacks> {

    /**
     * 获取工作日的月份
     */
    void begWorkDayMonth();

    /**
     * 请求当前月份的工作日
     */
    void requestCurrentWorkDay(String checkedMonth);
}
