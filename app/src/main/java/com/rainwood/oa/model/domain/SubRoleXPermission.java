package com.rainwood.oa.model.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/21 13:30
 * @Desc: 权限列表
 */
public final class SubRoleXPermission implements Serializable {

    /**
     * 某个权限
     */
    private String XPermission;

    /**
     * 是否拥有某个权限
     */
    private boolean checked;

    /**
     * 某个权限下的具体权限
     */
    private List<RoleDetailXPermission> detailXPermissions;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getXPermission() {
        return XPermission;
    }

    public void setXPermission(String XPermission) {
        this.XPermission = XPermission;
    }

    public List<RoleDetailXPermission> getDetailXPermissions() {
        return detailXPermissions;
    }

    public void setDetailXPermissions(List<RoleDetailXPermission> detailXPermissions) {
        this.detailXPermissions = detailXPermissions;
    }
}
