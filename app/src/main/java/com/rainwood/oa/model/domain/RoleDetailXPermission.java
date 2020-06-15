package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/5/21 13:31
 * @Desc: 具体权限
 */
public final class RoleDetailXPermission implements Serializable {

    /**
     * 具体权限
     */
    private String name;

    /**
     * 是否拥有这个权限
     */
    private boolean checked;

    /**
     * 关键词
     */
    private String key;

    /**
     * 是否拥有这个权限
     */
    private String hook;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getHook() {
        return hook;
    }

    public void setHook(String hook) {
        this.hook = hook;
    }
}
