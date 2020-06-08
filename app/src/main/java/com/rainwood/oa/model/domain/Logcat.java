package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/5/28 9:48
 * @Desc: 系统日志
 */
public final class Logcat implements Serializable {

    /**
     * 姓名
     */
    private String staffName;

    /**
     * 内容
     */
    private String text;

    /**
     * 来源
     */
    private String type;

    /**
     * 时间
     */
    private String time;

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
