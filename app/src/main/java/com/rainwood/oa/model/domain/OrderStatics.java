package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/5/20 9:51
 * @Desc: 订单统计
 */
public final class OrderStatics implements Serializable {

    /**
     * 统计
     */
    private String title;

    /**
     * values
     */
    private String values;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return "OrderStatics{" +
                "title='" + title + '\'' +
                ", values='" + values + '\'' +
                '}';
    }
}
