package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/6/28 11:10
 * @Desc: 收支平衡
 */
public final class BalanceRecord implements Serializable {

    /**
     * id
     */
    private String id;

    /**
     * 来源
     */
    private String target;

    /**
     * 方向
     */
    private String direction;

    /**
     * 分类
     */
    private String type;

    /**
     * 金额
     */
    private String money;

    /**
     * 支付凭证
     */
    private String ico;

    /**
     * 备注
     */
    private String text;

    /**
     * 日期时间
     */
    private String payDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
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
