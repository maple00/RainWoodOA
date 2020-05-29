package com.rainwood.oa.model.domain;

/**
 * @Author: a797s
 * @Date: 2020/5/27 9:06
 * @Desc: 补卡记录
 */
public final class CardRecord {

    /**
     * 申请人
     */
    private String name;

    /**
     * 状态
     */
    private String status;

    /**
     * 补卡时间
     */
    private String time;

    /**
     * 补卡内容
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
