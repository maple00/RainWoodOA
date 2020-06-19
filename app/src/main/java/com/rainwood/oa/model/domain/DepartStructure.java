package com.rainwood.oa.model.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/3 16:48
 * @Desc: 部门组织结构
 */
public final class DepartStructure implements Serializable {

    /**
     * 部门名称
     */
    private String name;

    /**
     * 部门总人数
     */
    private String num;

    /**
     * 被选中
     */
    private boolean selected;

    private List<StaffStructure> array;

    public List<StaffStructure> getArray() {
        return array;
    }

    public void setArray(List<StaffStructure> array) {
        this.array = array;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
