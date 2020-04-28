package com.rainwood.oa.model.domain;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/4/28 10:51
 * @Desc: 管理页面图标
 */
public final class ManagerMain {

    private String title;           // 标题
    private boolean hasSelected = false;    // 是否选中
    private List<IconAndFont> mIconAndFontList;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isHasSelected() {
        return hasSelected;
    }

    public void setHasSelected(boolean hasSelected) {
        this.hasSelected = hasSelected;
    }

    public List<IconAndFont> getIconAndFontList() {
        return mIconAndFontList;
    }

    public void setIconAndFontList(List<IconAndFont> iconAndFontList) {
        mIconAndFontList = iconAndFontList;
    }
}
