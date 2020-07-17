package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/4/29 10:49
 * @Desc: 文字+文字描述
 */
public final class FontAndFont implements Serializable {

    private String title;
    private String desc;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "FontAndFont{" +
                "title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
