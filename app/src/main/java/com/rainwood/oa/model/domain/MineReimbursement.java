package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/6/4 14:26
 * @Desc: 我的费用报销
 */
public final class MineReimbursement implements Serializable {

    /**
     * id
     */
    private String id;

    /**
     * 费用类型
     */
    private String type;

    /**
     * 是否有费用报销凭证
     */
    private String ico;

    /**
     * 付款方
     */
    private String payer;

    /**
     * 金额
     */
    private String money;

    /**
     * 备注
     */
    private String text;

    /**
     * 日期
     */
    private String payDate;

    /**
     * 费用是否报销
     */
    private String pay;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIco() {
        return ico;
    }

    public void setIco(String ico) {
        this.ico = ico;
    }

    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer;
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

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }
}

