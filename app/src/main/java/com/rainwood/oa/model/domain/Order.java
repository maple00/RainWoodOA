package com.rainwood.oa.model.domain;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/20 10:28
 * @Desc: 订单
 */
public final class Order {

    /**
     * 订单编号
     */
    private String no;
    /**
     * 订单状态
     */
    private String status;

    /**
     * 订单名称
     */
    private String orderName;
    /**
     * 订单金额
     */
    private String money;

    /**
     * 订单属性
     */
    private List<OrderStatics> natureList;

    /**
     * 负责人姓名
     */
    private String chargeName;

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

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getChargeName() {
        return chargeName;
    }

    public void setChargeName(String chargeName) {
        this.chargeName = chargeName;
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

    public List<OrderStatics> getNatureList() {
        return natureList;
    }

    public void setNatureList(List<OrderStatics> natureList) {
        this.natureList = natureList;
    }

    @Override
    public String toString() {
        return "Order{" +
                "no='" + no + '\'' +
                ", status='" + status + '\'' +
                ", orderName='" + orderName + '\'' +
                ", money='" + money + '\'' +
                ", natureList=" + natureList +
                ", chargeName='" + chargeName + '\'' +
                ", timeLimit='" + timeLimit + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}