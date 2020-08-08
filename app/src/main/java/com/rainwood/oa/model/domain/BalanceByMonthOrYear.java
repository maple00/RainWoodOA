package com.rainwood.oa.model.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/29 9:21
 * @Desc: 收支曲线 -- 按月
 */
public final class BalanceByMonthOrYear implements Serializable {

    @Override
    public String toString() {
        return "BalanceByMonthOrYear{" +
                "name='" + name + '\'' +
                ", data=" + data +
                '}';
    }

    /**
     * 名称
     */
    private String name;

    /**
     * 数据
     */
    private List<BalanceItem> data;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<BalanceItem> getData() {
        return data;
    }

    public void setData(List<BalanceItem> data) {
        this.data = data;
    }
}
