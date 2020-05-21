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
     * 角色名字
     */
     private String name;

    /**
     * 角色权限描述
     */
    private String desc;

    /**
     * 角色拥有的模块权限
     */
    private List<SubRoleXModule> roleXModules;

    /**
     * 是否被选择
     */
    private boolean hasSelected;

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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<SubRoleXModule> getRoleXModules() {
        return roleXModules;
    }

    public void setRoleXModules(List<SubRoleXModule> roleXModules) {
        this.roleXModules = roleXModules;
    }
}

