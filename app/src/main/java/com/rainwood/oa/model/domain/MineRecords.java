package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/6/12 13:09
 * @Desc: 我的记录（补卡记录、请假记录）
 */
public final class MineRecords implements Serializable {

    /**
     * id
     */
    private String id;

    /**
     * 状态
     */
    private String workFlow;

    /**
     * 时间
     */
    private String signTime;

    /**
     * 时间1
     */
    private String time;
    /**
     * 内容
     */
    private String text;

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

    public String getSignTime() {
        return signTime;
    }

    public void setSignTime(String signTime) {
        this.signTime = signTime;
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
}
