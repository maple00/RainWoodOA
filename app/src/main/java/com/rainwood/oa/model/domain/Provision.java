package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/5/19 14:10
 * @Desc: 费用计提
 */
public final class Provision implements Serializable {

    /**
     * 费用
     */
    private String money;

    /**
     * 用处
     */
    private String used;

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getUsed() {
        return used;
    }

    public void setUsed(String used) {
        this.used = used;
    }
}
