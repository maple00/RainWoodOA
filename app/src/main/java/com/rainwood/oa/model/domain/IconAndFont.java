package com.rainwood.oa.model.domain;

/**
 * @Author: a797s
 * @Date: 2020/4/28 10:52
 * @Desc: 图标文字
 */
public final class IconAndFont {

    private String desc;
    private int localMipmap;

    public int getLocalMipmap() {
        return localMipmap;
    }

    public void setLocalMipmap(int localMipmap) {
        this.localMipmap = localMipmap;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
