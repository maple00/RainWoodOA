package com.rainwood.oa.model.domain;

/**
 * @Author: a797s
 * @Date: 2020/6/29 15:09
 * @Desc: 员工数量列表
 */
public final class StaffCurveList {

    /**
     * 月份
     */
    private String month;
    /**
     * 数量
     */
    private String number;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
