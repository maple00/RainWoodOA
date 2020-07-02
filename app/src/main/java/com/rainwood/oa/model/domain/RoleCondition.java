package com.rainwood.oa.model.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/22 11:27
 * @Desc: 角色列表 条件筛选
 */
public final class RoleCondition implements Serializable {

    /**
     * 模块名称
     */
    private String name;

    private List<RoleSecondCondition> array;

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

    public List<RoleSecondCondition> getArray() {
        return array;
    }

    public void setArray(List<RoleSecondCondition> array) {
        this.array = array;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return "RoleCondition{" +
                "name='" + name + '\'' +
                ", array=" + array +
                ", selected=" + selected +
                '}';
    }
}
