package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/7/2 9:46
 * @Desc: 客户列表区域筛选
 */
public final class CustomArea implements Serializable {


    /**
     * id
     */
    private String id;

    /**
     * 地点名称
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
