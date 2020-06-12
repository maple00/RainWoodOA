package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/5/25 19:56
 * @Desc: 行政人事---外出记录记录
 */
public final class AdminLeaveOut implements Serializable {

    /**
     * 记录id
     */
    private String id;

    /**
     * 员工
     */
    private String staffName;

    /**
     * 预计时间
     */
    private String expectTime;

    /**
     * 实际时间
     */
    private String time;

    /**
     * 外出内容
     */
    private String text;

    /**
     * 状态
     */
    private String workFlow;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
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

    public String getWorkFlow() {
        return workFlow;
    }

    public void setWorkFlow(String workFlow) {
        this.workFlow = workFlow;
    }
}
