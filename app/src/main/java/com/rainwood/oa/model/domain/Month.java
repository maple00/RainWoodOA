package com.rainwood.oa.model.domain;

/**
 * @Author: a797s
 * @Date: 2020/6/10 13:29
 * @Desc: 月份
 */
public final class Month {

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
