package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/5/25 18:19
 * @Desc: 员工结算账户
 */
public final class StaffSettlement implements Serializable {

    /**
     * id
     */
    private String id;

    /**
     * 方式
     */
    private String direction;

    /**
     * 金额
     */
    private String money;

    /**
     * 余额
     */
    private String balance;

    /**
     * 备注
     */
    private String text;

    /**
     * 发生时间
     */
    private String time;

    /**
     * 凭证
     */
    private String ico;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIco() {
        return ico;
    }

    public void setIco(String ico) {
        this.ico = ico;
    }
}

