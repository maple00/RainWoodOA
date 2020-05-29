package com.rainwood.oa.model.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/29 10:18
 * @Desc: 客户订单
 */
public final class CustomOrder implements Serializable {

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
     * 订单属性值
     */
    private List<FontAndFont> valueList;

    public List<FontAndFont> getValueList() {
        return valueList;
    }

    public void setValueList(List<FontAndFont> valueList) {
        this.valueList = valueList;
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
