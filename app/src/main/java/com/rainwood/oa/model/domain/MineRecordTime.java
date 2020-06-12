package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/6/12 16:13
 * @Desc: 我的记录（加班记录、外出记录）
 */
public final class MineRecordTime implements Serializable {

    /**
     * id
     */
    private String id;

    /**
     * 预计时间
     */
    private String expectTime;

    /**
     * 内容
     */
    private String text;

    /**
     * 实际加班时间
     */
    private String time;

    /**
     * 状态
     */
    private String workFlow;

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

    public String getExpectTime() {
        return expectTime;
    }

    public void setExpectTime(String expectTime) {
        this.expectTime = expectTime;
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

    public String getWorkFlow() {
        return workFlow;
    }

    public void setWorkFlow(String workFlow) {
        this.workFlow = workFlow;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }
}
