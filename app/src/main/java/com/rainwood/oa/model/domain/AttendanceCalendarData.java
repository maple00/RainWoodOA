package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/6/29 17:49
 * @Desc: 考勤 == 日历数据
 */
public final class AttendanceCalendarData implements Serializable {

    /**
     * day
     */
    private String day;

    /**
     * 是否是工作日
     */
    private String workDay;

    /**
     * 当日工作小时数
     */
    private float hour;

    /**
     * 打卡次数
     */
    private int signNum;

    /**
     * 上班打卡
     */
    private AttendanceStart start;

    private AttendanceEnd end;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getWorkDay() {
        return workDay;
    }

    public void setWorkDay(String workDay) {
        this.workDay = workDay;
    }

    public float getHour() {
        return hour;
    }

    public void setHour(float hour) {
        this.hour = hour;
    }

    public int getSignNum() {
        return signNum;
    }

    public void setSignNum(int signNum) {
        this.signNum = signNum;
    }

    public AttendanceStart getStart() {
        return start;
    }

    public void setStart(AttendanceStart start) {
        this.start = start;
    }

    public AttendanceEnd getEnd() {
        return end;
    }

    public void setEnd(AttendanceEnd end) {
        this.end = end;
    }
}
