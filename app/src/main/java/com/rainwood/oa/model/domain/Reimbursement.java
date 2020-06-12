package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/6/4 14:26
 * @Desc: 费用报销
 */
public final class Reimbursement implements Serializable {

    /**
     * id
     */
    private String id;
    /**
     * 费用类型
     */
    private String type;

    /**
     * 费用金额
     */
    private String money;

    /**
     * 是否有报销凭证
     */
    private boolean ico;

    /**
     * 报销人
     */
    private String staffName;

    /**
     * 报销事由
     */
    private String text;

    /**
     * 是否已拨付
     */
    private String pay;

    /**
     * 拨付时间
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

    public boolean isIco() {
        return ico;
    }

    public void setIco(boolean ico) {
        this.ico = ico;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }
}

