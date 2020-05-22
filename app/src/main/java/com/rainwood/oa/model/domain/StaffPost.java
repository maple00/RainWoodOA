package com.rainwood.oa.model.domain;

/**
 * @Author: a797s
 * @Date: 2020/5/22 10:58
 * @Desc: 员工职位
 */
public final class StaffPost {

    /**
     * 职位
     */
    private String post;

    /**
     * 是否被选中
     */
    private boolean selected;

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
