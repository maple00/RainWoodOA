package com.rainwood.oa.model.domain;

/**
 * @Author: a797s
 * @Date: 2020/5/25 19:56
 * @Desc: 加班记录
 */
public final class OvertimeRecord {

    /**
     * 加班申请人
     */
    private String name;

    /**
     * 记录状态
     */
    private String status;

    /**
     * 加班预计时间
     */
    private String preTime;

    /**
     * 加班类型
     */
    private String type;

    /**
     * 加班内容
     */
    private String content;

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

    public String getPreTime() {
        return preTime;
    }

    public void setPreTime(String preTime) {
        this.preTime = preTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
