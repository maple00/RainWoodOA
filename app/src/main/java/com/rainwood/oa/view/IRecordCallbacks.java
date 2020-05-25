package com.rainwood.oa.view;

import com.rainwood.oa.base.IBaseCallback;

import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/5/25 19:54
 * @Desc: 记录callback----加班记录、请假记录、外出记录、补卡记录
 */
public interface IRecordCallbacks extends IBaseCallback {

    /**
     * 获取所有的加班记录
     */
    void getOvertimeRecords(Map recordMap);
}
