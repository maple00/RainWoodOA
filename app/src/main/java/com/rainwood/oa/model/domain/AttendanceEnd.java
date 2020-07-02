package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/6/29 17:34
 * @Desc: 考勤记录 下班
 */
public final class AttendanceEnd implements Serializable {

    /**
     * 下班打卡时间
     */
    private String time;

    /**
     * 下班签到
     */
    private AttendanceEndTab tab;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public AttendanceEndTab getTab() {
        return tab;
    }

    public void setTab(AttendanceEndTab tab) {
        this.tab = tab;
    }
}
