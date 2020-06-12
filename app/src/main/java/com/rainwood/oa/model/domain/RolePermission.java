package com.rainwood.oa.model.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/21 13:25
 * @Desc: 角色权限 --0
 */
public final class RolePermission implements Serializable {

    /**
     * 角色id
     */
    private String id;

    /**
     * 角色名字
     */
     private String name;

    /**
     * 角色权限描述
     */
    private String text;

    /**
     * 角色拥有的模块权限
     */
    private List<SubRoleXModule> power;

    /**
     * 是否被选择
     */
    private boolean hasSelected;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isHasSelected() {
        return hasSelected;
    }

    public void setHasSelected(boolean hasSelected) {
        this.hasSelected = hasSelected;
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

    public List<SubRoleXModule> getPower() {
        return power;
    }

    public void setPower(List<SubRoleXModule> power) {
        this.power = power;
    }
}

