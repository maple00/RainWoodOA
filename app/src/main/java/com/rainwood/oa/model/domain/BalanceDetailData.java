package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/7/22 11:22
 * @Desc: 收支记录详情
 */
public final class BalanceDetailData implements Serializable {

    /**
     * 来源
     */
    private String target;

    /**
     * 方向
     */
    private String direction;

    /**
     * 类型
     */
    private String type;

    /**
     * 金额
     */
    private double money;

    /**
     * 凭证
     */
    private String ico;
    /**
     * 备注信息
     */
    private String text;

    /**
     * 时间
     */
    private String payDate;

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getIco() {
        return ico;
    }

    public void setIco(String ico) {
        this.ico = ico;
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
