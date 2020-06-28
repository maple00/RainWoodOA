package com.rainwood.oa.model.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/28 17:41
 * @Desc: 收支平衡 类型
 */
public final class BalanceType implements Serializable {

    /**
     * 类型
     */
    private String name;

    private List<BalanceMethod> array;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<BalanceMethod> getArray() {
        return array;
    }

    public void setArray(List<BalanceMethod> array) {
        this.array = array;
    }
}
