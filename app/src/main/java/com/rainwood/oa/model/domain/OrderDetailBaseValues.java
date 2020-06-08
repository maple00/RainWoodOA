package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/6/5 16:07
 * @Desc: 订单详情基本数据
 */
public final class OrderDetailBaseValues implements Serializable {

    /**
     * 订单号
     */
    private String id;

    /**
     * 订单名称
     */
    private String name;

    /**
     * 订单状态
     */
    private String workFlow;

    /**
     * 立项日期
     */
    private String signDay;

    /**
     * 交付日期
     */
    private String endDay;

    /**
     * 合同金额
     */
    private String money;

    /**
     * 费用计提
     */
    private String cost;

    /**
     * 合同净值
     */
    private String netWorthOrder;

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
    private String moneyWait;

    /**
     * 剩余净值
     */
    private String netWorthWait;

    /**
     *工期
     */
    private String cycle;

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWorkFlow() {
        return workFlow;
    }

    public void setWorkFlow(String workFlow) {
        this.workFlow = workFlow;
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

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getNetWorthOrder() {
        return netWorthOrder;
    }

    public void setNetWorthOrder(String netWorthOrder) {
        this.netWorthOrder = netWorthOrder;
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

    public String getMoneyWait() {
        return moneyWait;
    }

    public void setMoneyWait(String moneyWait) {
        this.moneyWait = moneyWait;
    }

    public String getNetWorthWait() {
        return netWorthWait;
    }

    public void setNetWorthWait(String netWorthWait) {
        this.netWorthWait = netWorthWait;
    }
}
