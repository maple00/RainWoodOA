package com.rainwood.oa.model.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/21 13:27
 * @Desc: 某个模块的权限 -- 1
 */
public final class SubRoleXModule implements Serializable {

    /**
     * 某个模块
     */
    private String moduleX;

    /**
     * 某个模块被选择
     */
    private boolean hasSelected;

    /**
     * 模块下的所有权限
     */
    private List<SubRoleXPermission> permissions;

    public boolean isHasSelected() {
        return hasSelected;
    }

    public void setHasSelected(boolean hasSelected) {
        this.hasSelected = hasSelected;
    }

    public String getModuleX() {
        return moduleX;
    }

    public void setModuleX(String moduleX) {
        this.moduleX = moduleX;
    }

    public List<SubRoleXPermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<SubRoleXPermission> permissions) {
        this.permissions = permissions;
    }
}
