package com.rainwood.oa.model.domain;

/**
 * @Author: a797s
 * @Date: 2020/5/19 14:10
 * @Desc: 费用计提
 */
public final class Provision {

    /**
     * 费用
     */
    private int money;

    /**
     * 用处
     */
    private String used;

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getUsed() {
        return used;
    }

    public void setUsed(String used) {
        this.used = used;
    }
}
