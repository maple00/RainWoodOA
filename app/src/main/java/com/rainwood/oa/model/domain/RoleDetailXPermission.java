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
    private String detailX;

    /**
     * 是否拥有这个权限
     */
    private boolean checked;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getDetailX() {
        return detailX;
    }

    public void setDetailX(String detailX) {
        this.detailX = detailX;
    }
}
