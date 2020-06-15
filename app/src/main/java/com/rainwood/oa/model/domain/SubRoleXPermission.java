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
    private String menuTwo;

    /**
     * 关键词
     */
    private String key;

    /**
     * 是否拥有某个权限
     */
    private boolean checked;

    /**
     * 某个权限下的具体权限
     */
    private List<RoleDetailXPermission> array;

    public String getMenuTwo() {
        return menuTwo;
    }

    public void setMenuTwo(String menuTwo) {
        this.menuTwo = menuTwo;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public List<RoleDetailXPermission> getArray() {
        return array;
    }

    public void setArray(List<RoleDetailXPermission> array) {
        this.array = array;
    }
}
