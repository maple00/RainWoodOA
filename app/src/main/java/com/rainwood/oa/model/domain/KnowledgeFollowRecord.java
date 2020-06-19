package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/6/5 14:07
 * @Desc: 知识库管理的跟进记录
 */
public final class KnowledgeFollowRecord implements Serializable {

    /**
     * 对象id
     */
    private String targetId;
    /**
     * 记录类型
     * 如果是客户，可查看该客户详情
     * 如果是订单，可查看该订单详情
     */
    private String target;

    /**
     * 跟进记录名字
     */
    private String targetName;

    /**
     * 记录内容
     */
    private String text;

    /**
     * 跟进人
     */
    private String staffName;

    /**
     * 跟进时间
     */
    private String time;

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
