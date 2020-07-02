package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/6/19 17:04
 * @Desc: 记录成果(加班成果 、)
 */
public final class RecordResults implements Serializable {

    /**
     * 记录对象
     */
    private String target;

    /**
     * 记录时长
     */
    private String hour;

    /**
     * 记录成果
     */
    private String text;

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
