package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/6/10 13:29
 * @Desc: 月份
 */
public final class Month implements Serializable {

    /**
     * 月份
     */
    private String month;

    /**
     * 设置选中
     */
    private boolean selected;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
