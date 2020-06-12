package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/6/11 11:56
 * @Desc: 行政人事--加班记录
 */
public final class AdminOverTime implements Serializable {

    /**
     * 记录id
     */
    private String id;

    /**
     * 记录状态
     */
    private String workFlow;

    /**
     * 预计时间
     */
    private String expectTime;

    /**
     * 实际时间
     */
    private String time;

    /**
     * 加班内容
     */
    private String text;

    /**
     * 加班申请人
     */
    private String staffName;

    /**
     * 加班时长
     */
    private String hour;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWorkFlow() {
        return workFlow;
    }

    public void setWorkFlow(String workFlow) {
        this.workFlow = workFlow;
    }

    public String getExpectTime() {
        return expectTime;
    }

    public void setExpectTime(String expectTime) {
        this.expectTime = expectTime;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }
}
