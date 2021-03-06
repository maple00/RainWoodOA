package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/4/28 10:52
 * @Desc: 图标文字
 */
public final class IconAndFont implements Serializable {

    /**
     * 模块名称
     */
    private String name;

    private int localMipmap;

    /**
     * 模块备注
     */
    private String menu;

    /**
     * 模块图标地址
     */
    private String ico;

    /**
     * 标识符
     */
    private String key;

    /**
     * 是否选中
     */
    private boolean selected;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLocalMipmap() {
        return localMipmap;
    }

    public void setLocalMipmap(int localMipmap) {
        this.localMipmap = localMipmap;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getIco() {
        return ico;
    }

    public void setIco(String ico) {
        this.ico = ico;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
