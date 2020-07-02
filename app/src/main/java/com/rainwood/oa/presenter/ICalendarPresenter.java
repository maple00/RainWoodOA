package com.rainwood.oa.presenter;

import com.rainwood.oa.base.IBasePresenter;
import com.rainwood.oa.view.ICalendarCallback;

import org.joda.time.LocalDate;

/**
 * @Author: a797s
 * @Date: 2020/6/10 13:51
 * @Desc: 日历相关 presenter
 */
public interface ICalendarPresenter extends IBasePresenter<ICalendarCallback> {

    /**
     * 获取工作日的月份
     */
    void begWorkDayMonth();

    /**
     * 请求当前月份的工作日
     */
    void requestCurrentWorkDay(String checkedMonth);

    /**
     * 请求当天的考勤信息
     *
     * @param currentMonth
     */
    void requestCurrentDayAttendance(String staffId, String currentMonth);
}
