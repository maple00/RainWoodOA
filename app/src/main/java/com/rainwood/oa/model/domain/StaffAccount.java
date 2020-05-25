package com.rainwood.oa.model.domain;

/**
 * @Author: a797s
 * @Date: 2020/5/25 17:32
 * @Desc: 员工会计账户
 */
public final class StaffAccount {

    /**
     * 发生事件
     */
    private String title;

    /**
     * 奖惩金额
     */
    private String money;

    /**
     * 发生时间
     */
    private String time;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

