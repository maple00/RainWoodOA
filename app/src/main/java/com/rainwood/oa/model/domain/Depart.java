package com.rainwood.oa.model.domain;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/21 17:11
 * @Desc: 部门
 */
public final class Depart {

    /**
     * 部门名称
     */
    private String depart;

    /**
     * 是否被选中
     */
    private boolean hasSelected;

    /**
     * 项目组
     */
    private List<ProjectGroup> mGroups;

    public String getDepart() {
        return depart;
    }

    public void setDepart(String depart) {
        this.depart = depart;
    }

    public boolean isHasSelected() {
        return hasSelected;
    }

    public void setHasSelected(boolean hasSelected) {
        this.hasSelected = hasSelected;
    }

    public List<ProjectGroup> getGroups() {
        return mGroups;
    }

    public void setGroups(List<ProjectGroup> groups) {
        mGroups = groups;
    }
}
