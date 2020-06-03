package com.rainwood.oa.model.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/20 10:28
 * @Desc: 订单
 */
public final class Order implements Serializable {

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
                '}';
    }
}