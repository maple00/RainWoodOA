package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/5/29 17:01
 * @Desc: 客户员工
 */
public final class CustomStaff implements Serializable {

    /**
     * 员工id
     */
    private String stid;

    /**
     * 员工姓名
     */
    private String name;

    /**
     * 是否选中
     */
    private boolean selected;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getStid() {
        return stid;
    }

    public void setStid(String stid) {
        this.stid = stid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
