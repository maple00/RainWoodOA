package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/6/28 14:27
 * @Desc: 分类统计
 */
public final class ClassificationStatics implements Serializable {
    /**
     * 类型
     */
    private String type;

    /**
     * 金额
     */
    private String money;

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
}
