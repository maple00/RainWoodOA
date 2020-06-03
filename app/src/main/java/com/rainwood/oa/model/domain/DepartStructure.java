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
    private String departNum;

    /**
     * 被选中
     */
    private boolean selected;

    private List<StaffStructure> staffList;

    public List<StaffStructure> getStaffList() {
        return staffList;
    }

    public void setStaffList(List<StaffStructure> staffList) {
        this.staffList = staffList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartNum() {
        return departNum;
    }

    public void setDepartNum(String departNum) {
        this.departNum = departNum;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
