package com.rainwood.oa.model.domain;

/**
 * @Author: a797s
 * @Date: 2020/5/21 17:12
 * @Desc: 项目组
 */
public final class ProjectGroup {

    /**
     * 项目组名称
     */
    private String group;

    /**
     * 职务
     */
    private String duty;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }
}
