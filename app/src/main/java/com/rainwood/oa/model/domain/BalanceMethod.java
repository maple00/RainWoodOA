package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/6/28 17:42
 * @Desc: 收支平衡方式
 */
public final class BalanceMethod implements Serializable {

    /**
     * 方式
     */
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
