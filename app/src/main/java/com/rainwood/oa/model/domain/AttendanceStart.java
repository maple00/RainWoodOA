package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/6/29 17:30
 * @Desc: 考勤 -- 上班
 */
public final class AttendanceStart implements Serializable {

    /**
     * 上班打卡时间
     */
    private String time;

    private AttendanceStartTab tab;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public AttendanceStartTab getTab() {
        return tab;
    }

    public void setTab(AttendanceStartTab tab) {
        this.tab = tab;
    }
}
