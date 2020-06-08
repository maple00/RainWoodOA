package com.rainwood.oa.model.domain;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/21 17:11
 * @Desc: 部门
 */
public final class Depart {

    /**
     * 部门名称
     */
    private String name;

    /**
     * 是否被选中
     */
    private boolean hasSelected;

    /**
     * 项目组
     */
    private List<ProjectGroup> array;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isHasSelected() {
        return hasSelected;
    }

    public void setHasSelected(boolean hasSelected) {
        this.hasSelected = hasSelected;
    }

    public List<ProjectGroup> getArray() {
        return array;
    }

    public void setArray(List<ProjectGroup> array) {
        this.array = array;
    }
}
