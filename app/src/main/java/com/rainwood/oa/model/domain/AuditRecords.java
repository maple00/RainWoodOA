package com.rainwood.oa.model.domain;

/**
 * @Author: a797s
 * @Date: 2020/5/26 14:25
 * @Desc: 审核记录
 */
public final class AuditRecords {

    /**
     * 审核记录
     */
    private String content;

    /**
     * 审核时间
     */
    private String time;

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
