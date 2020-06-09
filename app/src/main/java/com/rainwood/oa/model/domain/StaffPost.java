package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/5/22 10:58
 * @Desc: 员工职位
 */
public final class StaffPost implements Serializable {

    /**
     * id
     */
    private String id;

    /**
     * 职位名称
     */
    private String name;

    /**
     * 是否被选中
     */
    private boolean selected;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
}
