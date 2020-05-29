package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/5/25 19:56
 * @Desc: 加班记录
 */
public final class OvertimeRecord implements Serializable {

    /**
     * 加班申请人
     */
    private String staffName;

    /**
     * 记录状态
     */
    private String workFlow;

    /**
     * 加班预计时间
     */
    private String time;

    /**
     * 加班类型
     */
    private String type;

    /**
     * 加班内容
     */
    private String text;

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getWorkFlow() {
        return workFlow;
    }

    public void setWorkFlow(String workFlow) {
        this.workFlow = workFlow;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
