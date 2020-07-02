package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/5/21 17:12
 * @Desc: 项目组
 */
public final class ProjectGroup implements Serializable {

    /**
     * 项目组id
     */
    private String id;
    /**
     * 项目组名称
     */
    private String name;

    /**
     * 部门
     */
    private String type;

    /**
     * 职务
     */
    private String text;

    /**
     * 被选中
     */
    private boolean selected;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
