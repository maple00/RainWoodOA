package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/6/29 9:23
 * @Desc: 收支曲线 -- Y轴数据
 */
public final class BalanceItem implements Serializable {

    /**
     *金额
     */
    private float money;

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }
}
