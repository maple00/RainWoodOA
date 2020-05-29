package com.rainwood.oa.model.domain;

/**
 * @Author: a797s
 * @Date: 2020/5/27 9:06
 * @Desc: 请假记录
 */
public final class LeaveRecord {

    /**
     * 申请人
     */
    private String name;

    /**
     * 状态
     */
    private String status;

    /**
     * 请假内容
     */
    private String content;

    /**
     * 请假时间
     */
    private String time;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
