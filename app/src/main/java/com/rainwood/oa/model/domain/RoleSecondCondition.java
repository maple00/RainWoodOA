package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/6/22 11:31
 * @Desc:
 */
public class RoleSecondCondition implements Serializable {
    /**
     * 次级模块
     */
    private String name;

    private String key;

    /**
     * 是否被选中
     */
    private boolean selected;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return "RoleSecondCondition{" +
                "name='" + name + '\'' +
                ", key='" + key + '\'' +
                ", selected=" + selected +
                '}';
    }
}