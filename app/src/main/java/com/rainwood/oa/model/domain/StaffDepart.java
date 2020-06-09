package com.rainwood.oa.model.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/22 10:56
 * @Desc: 员工的部门
 */
public final class StaffDepart implements Serializable {

    /**
     * 部门名称
     */
    private String name;

    /**
     * 是否被选中
     */
    private boolean selected;

    /**
     * 部门下级数组
     */
    private List<StaffPost> array;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public List<StaffPost> getArray() {
        return array;
    }

    public void setArray(List<StaffPost> array) {
        this.array = array;
    }
}
