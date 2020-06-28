package com.rainwood.oa.model.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/4/28 10:51
 * @Desc: 管理页面图标
 */
public final class ManagerMain implements Serializable {

    private String name;           // 标题
    private boolean hasSelected = true;    // 是否选中
    private List<IconAndFont> array;

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

    public List<IconAndFont> getArray() {
        return array;
    }

    public void setArray(List<IconAndFont> array) {
        this.array = array;
    }
}
