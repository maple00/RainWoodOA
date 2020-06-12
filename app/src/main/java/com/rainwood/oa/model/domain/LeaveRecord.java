package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/5/27 9:06
 * @Desc: 请假记录
 */
public final class LeaveRecord implements Serializable {

    /**
     * 记录id
     */
    private String id;

    /**
     * 申请人
     */
    private String staffName;

    /**
     * 状态
     */
    private String workFlow;

    /**
     * 请假类型
     */
    private String type;
    /**
     * 请假内容
     */
    private String text;

    /**
     * 请假时间
     */
    private String time;

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

    public String getWorkFlow() {
        return workFlow;
    }

    public void setWorkFlow(String workFlow) {
        this.workFlow = workFlow;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
