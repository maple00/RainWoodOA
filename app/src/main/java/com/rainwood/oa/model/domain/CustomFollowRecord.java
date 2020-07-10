package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/5/29 13:32
 * @Desc: 客户跟进记录
 */
public final class CustomFollowRecord implements Serializable {

    /**
     * 跟进记录id
     */
    private String id;

    /**
     * 跟进人
     */
    private String staffName;

    /**
     * 跟进内容
     */
    private String text;

    /**
     * 跟进时间
     */
    private String time;

    /**
     * 是否被选中
     */
    private boolean selected;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
