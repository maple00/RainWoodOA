package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/6/4 10:31
 * @Desc: 订单任务分配
 */
public final class OrderTask implements Serializable {

    /**
     * 任务名称
     */
    private String name;

    /**
     * 任务负责人
     */
    private String staffName;

    /**
     * 拨付比例
     */
    private String rate;

    /**
     * 预计奖金
     */
    private String money;

    /**
     * 已经拨付
     */
    private String pay;

    /**
     * 奖金余额
     */
    private String balance;

    /**
     * 任务介绍
     */
    private String summary;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
