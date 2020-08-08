package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/6/16 16:06
 * @Desc: 首页工资描述
 */
public final class HomeSalaryDesc implements Serializable {

    /**
     * 圆点得颜色
     */
    private int color;

    /**
     * 内容
     */
    private float content;

    /**
     * 描述
     */
    private String desc;

    public HomeSalaryDesc(float content, String desc) {
        this.content = content;
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public float getContent() {
        return content;
    }

    public void setContent(float content) {
        this.content = content;
    }
}
