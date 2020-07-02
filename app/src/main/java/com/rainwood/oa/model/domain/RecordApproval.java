package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/6/19 17:02
 * @Desc: 审批记录
 */
public final class RecordApproval implements Serializable {

    /**
     * 审批内容
     */
    private String text;

    /**
     * 审批时间
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
