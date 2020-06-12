package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/6/12 14:57
 * @Desc: 审核记录
 */
public final class AuditRecord implements Serializable {

    /**
     * 内容
     */
    private String text;

    /**
     * 时间
     */
    private String time;

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
