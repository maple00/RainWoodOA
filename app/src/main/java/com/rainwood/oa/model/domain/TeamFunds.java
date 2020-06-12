package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/6/5 10:49
 * @Desc: 团队基金、个人中心会计账户
 */
public final class TeamFunds implements Serializable {

    /**
     * id
     */
    private String id;

    /**
     * 类型
     */
    private String direction;

    /**
     * 余额
     */
    private String balance;

    /**
     * 事由内容
     */
    private String text;

    /**
     * 金额
     */
    private String money;

    /**
     * 发生时间
     */
    private String time;

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
