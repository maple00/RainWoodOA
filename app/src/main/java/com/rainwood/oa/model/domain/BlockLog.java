package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/6/17 16:58
 * @Desc: 待办事项
 */
public final class BlockLog implements Serializable {

    /**
     * 事件id
     */
    private String id;

    /**
     * 对象id
     */
    private String typeId;
    /**
     * 类型
     */
    private String type;

    /**
     * 状态
     */
    private String workFlow;

    /**
     * 事件内容
     */
    private String text;

    /**
     * 提示日期
     */
    private String startDay;
    /**
     * 事件时间
     */
    private String time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWorkFlow() {
        return workFlow;
    }

    public void setWorkFlow(String workFlow) {
        this.workFlow = workFlow;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getStartDay() {
        return startDay;
    }

    public void setStartDay(String startDay) {
        this.startDay = startDay;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
