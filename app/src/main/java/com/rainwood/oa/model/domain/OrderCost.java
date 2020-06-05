package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/6/4 10:16
 * @Desc: 订单费用计提
 */
public final class OrderCost implements Serializable {

    /**
     * 计提金额
     */
    private String money;

    /**
     * 备注
     */
    private String text;

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
