package com.rainwood.oa.model.domain;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/22 10:56
 * @Desc: 员工的部门
 */
public final class StaffDepart {

    /**
     * 部门
     */
    private String depart;

    /**
     * 是否被选中
     */
    private boolean selected;

    /**
     * 部门下的员工
     */
    private List<StaffPost> postList;

    public String getDepart() {
        return depart;
    }

    public void setDepart(String depart) {
        this.depart = depart;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public List<StaffPost> getPostList() {
        return postList;
    }

    public void setPostList(List<StaffPost> postList) {
        this.postList = postList;
    }
}
