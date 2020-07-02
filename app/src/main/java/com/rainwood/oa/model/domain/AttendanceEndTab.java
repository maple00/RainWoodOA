package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/6/29 17:36
 * @Desc: 考勤记录  下班状态标记
 */
public final class AttendanceEndTab implements Serializable {

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
     * 离职
     */
    private String quit;

    /**
     * 加班
     */
    private String add;

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

    public String getQuit() {
        return quit;
    }

    public void setQuit(String quit) {
        this.quit = quit;
    }

    public String getAdd() {
        return add;
    }

    public void setAdd(String add) {
        this.add = add;
    }
}
