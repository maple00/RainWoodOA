package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/6/29 17:32
 * @Desc: 考勤记录上下班 状态标记
 */
public final class AttendanceStartTab implements Serializable {

    /**
     * 请假
     */
    private String holiday;

    /**
     * 补卡
     */
    private String signAdd;

    /**
     * 矿工
     */
    private String leave;

    /**
     * 迟到
     */
    private String late;

    public String getHoliday() {
        return holiday;
    }

    public void setHoliday(String holiday) {
        this.holiday = holiday;
    }

    public String getSignAdd() {
        return signAdd;
    }

    public void setSignAdd(String signAdd) {
        this.signAdd = signAdd;
    }

    public String getLeave() {
        return leave;
    }

    public void setLeave(String leave) {
        this.leave = leave;
    }

    public String getLate() {
        return late;
    }

    public void setLate(String late) {
        this.late = late;
    }
}
