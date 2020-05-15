package com.rainwood.oa.model.domain;

/**
 * create by a797s in 2020/5/15 13:06
 *
 * @Description : 客户
 * @Usage :
 **/
public final class Custom {

    /**
     * 客户id
     */
    private String id;

    /**
     * 客户名字
     */
    private String name;

    /**
     * 选中
     */
    private boolean selected;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
