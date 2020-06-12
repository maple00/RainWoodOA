package com.rainwood.oa.view;

import com.rainwood.oa.base.IBaseCallback;
import com.rainwood.oa.model.domain.Month;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/10 13:49
 * @Desc: 日历相关 callbacks
 */
public interface ICalendarCallbacks extends IBaseCallback {

    /**
     * 工作日月份
     */
    default void getWorkDayMonthData(List<Month> monthList){}

    /**
     * 当前工作月工作日
     * @param dayList
     */
    default void getWorkDayData(List<String> dayList){}
}
