package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/6/4 11:01
 * @Desc: 订单详情已付费用
 */
public final class OrderPayed implements Serializable {

    /**
     * 费用类型
     */
    private String type;

    /**
     * 费用金额
     */
    private String money;

    /**
     * 备注
     */
    private String text;

    /**
     * 支付日期
     */
    private String payDate;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

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

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }
}
