package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/6/4 10:51
 * @Desc: 订单回款记录
 */
public final class OrderReceivable implements Serializable {

    /**
     * 回款金额
     */
    private String money;

    /**
     * 本次分得
     */
    private String getMoney;

    /**
     * 回款备注
     */
    private String text;

    /**
     * 回款日期
     */
    private String payDate;

    public String getGetMoney() {
        return getMoney;
    }

    public void setGetMoney(String getMoney) {
        this.getMoney = getMoney;
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
