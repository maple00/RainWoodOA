package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/6/22 14:11
 * @Desc: 部门分类
 */
public final class DepartType implements Serializable {

    /**
     * 部门名称
     */
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
