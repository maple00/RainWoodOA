package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/6/16 14:29
 * @Desc: 首页工资曲线
 */
public final class HomeSalary implements Serializable {

    /**
     * 基本工资
     */
    private String basePay;

    /**
     * 岗位津贴
     */
    private String subsidy;

    /**
     * 会计账户
     */
    private String money;

    /**
     * 结算账户
     */
    private String lastMoney;

    public String getBasePay() {
        return basePay;
    }

    public void setBasePay(String basePay) {
        this.basePay = basePay;
    }

    public String getSubsidy() {
        return subsidy;
    }

    public void setSubsidy(String subsidy) {
        this.subsidy = subsidy;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getLastMoney() {
        return lastMoney;
    }

    public void setLastMoney(String lastMoney) {
        this.lastMoney = lastMoney;
    }
}
