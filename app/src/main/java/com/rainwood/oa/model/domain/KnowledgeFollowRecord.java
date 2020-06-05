package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/6/5 14:07
 * @Desc: 知识库管理的跟进记录
 */
public final class KnowledgeFollowRecord implements Serializable {

    /**
     * 记录类型
     * 如果是客户，可查看该客户详情
     * 如果是订单，可查看该订单详情
     */
    private String type;

    /**
     * 跟进记录名字
     */
    private String name;

    /**
     * 记录内容
     */
    private String content;

    /**
     * 跟进人
     */
    private String staffName;

    /**
     * 跟进时间
     */
    private String time;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
