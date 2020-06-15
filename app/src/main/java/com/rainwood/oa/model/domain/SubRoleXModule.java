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
    private String menuOne;

    private String name;

    /**
     * 某个模块被选择
     */
    private boolean hasSelected;

    /**
     * 模块下的所有权限
     */
    private List<SubRoleXPermission> array;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isHasSelected() {
        return hasSelected;
    }

    public void setHasSelected(boolean hasSelected) {
        this.hasSelected = hasSelected;
    }

    public String getMenuOne() {
        return menuOne;
    }

    public void setMenuOne(String menuOne) {
        this.menuOne = menuOne;
    }

    public List<SubRoleXPermission> getArray() {
        return array;
    }

    public void setArray(List<SubRoleXPermission> array) {
        this.array = array;
    }
}
