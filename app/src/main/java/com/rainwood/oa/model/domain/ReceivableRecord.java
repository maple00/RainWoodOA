package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: sxs
 * @Time: 2020/5/30 12:26
 * @Desc: 回款记录
 */
public final class ReceivableRecord implements Serializable {

    /**
     * 回款记录id
     */
    private String id;

    /**
     * 回款金额
     */
    private String money;

    /**
     * 回款日期
     */
    private String payDate;

    /**
     * 备注
     */
    private String text;

    /**
     * 回款记录凭证
     */
    private String ico;

    public String getIco() {
        return ico;
    }

    public void setIco(String ico) {
        this.ico = ico;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
