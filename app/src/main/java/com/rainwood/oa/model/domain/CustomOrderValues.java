package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/5/29 10:42
 * @Desc: 订单中的value
 */
public final class CustomOrderValues implements Serializable {

    /**
     * 订单id
     */
    private String id;

    /**
     * 订单状态
     */
    private String workFlow;

    /**
     * 订单名称
     */
    private String name;

    /**
     * 合同金额
     */
    private String money;


    /**
     * 立项日
     */
    private String signDay;

    /**
     * 交付日
     */
    private String endDay;

    /**
     * 工期
     */
    private String cycle;

    /**
     * 费用计提
     */
    private String cost;

    /**
     * 合同净值
     */
    private String netWorth;

    /**
     * 已回款
     */
    private String moneyIn;

    /**
     * 已付费用
     */
    private String moneyOut;

    /**
     * 净回款
     */
    private String netWorthIn;

    /**
     * 合同应收
     */
    private String receivable;

    /**
     * 剩余净值
     */
    private String netWorthWait;

    /**
     * 拨付比例
     */
    private String ratio;

    /**
     * 预计奖金
     */
    private String buyCarAllotMoney;

    /**
     * 已拨付
     */
    private String buyCarAllotPay;

    /**
     * 奖金余额
     */
    private String buyCarAllotSurplus;

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getNetWorth() {
        return netWorth;
    }

    public void setNetWorth(String netWorth) {
        this.netWorth = netWorth;
    }

    public String getMoneyIn() {
        return moneyIn;
    }

    public void setMoneyIn(String moneyIn) {
        this.moneyIn = moneyIn;
    }

    public String getMoneyOut() {
        return moneyOut;
    }

    public void setMoneyOut(String moneyOut) {
        this.moneyOut = moneyOut;
    }

    public String getNetWorthIn() {
        return netWorthIn;
    }

    public void setNetWorthIn(String netWorthIn) {
        this.netWorthIn = netWorthIn;
    }

    public String getReceivable() {
        return receivable;
    }

    public void setReceivable(String receivable) {
        this.receivable = receivable;
    }

    public String getNetWorthWait() {
        return netWorthWait;
    }

    public void setNetWorthWait(String netWorthWait) {
        this.netWorthWait = netWorthWait;
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }

    public String getBuyCarAllotMoney() {
        return buyCarAllotMoney;
    }

    public void setBuyCarAllotMoney(String buyCarAllotMoney) {
        this.buyCarAllotMoney = buyCarAllotMoney;
    }

    public String getBuyCarAllotPay() {
        return buyCarAllotPay;
    }

    public void setBuyCarAllotPay(String buyCarAllotPay) {
        this.buyCarAllotPay = buyCarAllotPay;
    }

    public String getBuyCarAllotSurplus() {
        return buyCarAllotSurplus;
    }

    public void setBuyCarAllotSurplus(String buyCarAllotSurplus) {
        this.buyCarAllotSurplus = buyCarAllotSurplus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWorkFlow() {
        return workFlow;
    }

    public void setWorkFlow(String workFlow) {
        this.workFlow = workFlow;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getSignDay() {
        return signDay;
    }

    public void setSignDay(String signDay) {
        this.signDay = signDay;
    }

    public String getEndDay() {
        return endDay;
    }

    public void setEndDay(String endDay) {
        this.endDay = endDay;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }
}
