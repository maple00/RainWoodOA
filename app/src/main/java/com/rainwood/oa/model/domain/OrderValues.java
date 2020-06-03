package com.rainwood.oa.model.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/3 19:12
 * @Desc: 订单列表结果
 */
public final class OrderValues implements Serializable {
    /**
     * 订单编号
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
     * 订单金额
     */
    private String money;

    /**
     * 订单属性
     */
    private List<FontAndFont> natureList;

    /**
     * 负责人姓名
     */
    private String staffName;

    /**
     * 工期
     */
    private String timeLimit;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 费用计提
     */
    private String cost;

    /**
     *合同净值
     */
    private String netWorthOrder;

    /**
     * 合同应收
     */
    private String moneyWait;

    /**
     * 净回款
     */
    private String netWorthIn;

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

    public String getNetWorthOrder() {
        return netWorthOrder;
    }

    public void setNetWorthOrder(String netWorthOrder) {
        this.netWorthOrder = netWorthOrder;
    }

    public String getMoneyWait() {
        return moneyWait;
    }

    public void setMoneyWait(String moneyWait) {
        this.moneyWait = moneyWait;
    }

    public String getNetWorthIn() {
        return netWorthIn;
    }

    public void setNetWorthIn(String netWorthIn) {
        this.netWorthIn = netWorthIn;
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

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(String timeLimit) {
        this.timeLimit = timeLimit;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public List<FontAndFont> getNatureList() {
        return natureList;
    }

    public void setNatureList(List<FontAndFont> natureList) {
        this.natureList = natureList;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", workFlow='" + workFlow + '\'' +
                ", name='" + name + '\'' +
                ", money='" + money + '\'' +
                ", natureList=" + natureList +
                ", staffName='" + staffName + '\'' +
                ", timeLimit='" + timeLimit + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", cost='" + cost + '\'' +
                ", netWorthOrder='" + netWorthOrder + '\'' +
                ", moneyWait='" + moneyWait + '\'' +
                ", netWorthIn='" + netWorthIn + '\'' +
                ", netWorthWait='" + netWorthWait + '\'' +
                ", ratio='" + ratio + '\'' +
                ", buyCarAllotMoney='" + buyCarAllotMoney + '\'' +
                ", buyCarAllotPay='" + buyCarAllotPay + '\'' +
                ", buyCarAllotSurplus='" + buyCarAllotSurplus + '\'' +
                '}';
    }
}