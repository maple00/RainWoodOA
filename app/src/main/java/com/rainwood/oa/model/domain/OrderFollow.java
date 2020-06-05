package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/6/4 10:22
 * @Desc: 订单跟进
 */
public final class OrderFollow implements Serializable {

    /**
     * 跟进内容
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
