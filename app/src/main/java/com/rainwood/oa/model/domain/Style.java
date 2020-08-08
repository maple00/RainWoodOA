package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/7/13 12:02
 * @Desc:
 */
public final class Style implements Serializable {

    /**
     * 一级标题
     */
    private String title1;

    /**
     * 二级标题
     */
    private String title2;

    /**
     * 正文样式
     */
    private String center;

    public String getTitle1() {
        return title1;
    }

    public void setTitle1(String title1) {
        this.title1 = title1;
    }

    public String getTitle2() {
        return title2;
    }

    public void setTitle2(String title2) {
        this.title2 = title2;
    }

    public String getCenter() {
        return center;
    }

    public void setCenter(String center) {
        this.center = center;
    }
}
