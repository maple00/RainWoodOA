package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/5/22 9:25
 * @Desc: 职位
 */
public final class Post implements Serializable {

    /**
     * 职位id
     */

    private String id;
    /**
     * 职位名称
     */
    private String name;

    /**
     * 部门名称
     */
    private String departmentName;

    /**
     * 部门分类
     */
    private String departmentType;

    /**
     * 所属角色
     */
    private String roleName;

    /**
     * 基本工资
     */
    private String basePay;

    /**
     * 岗位津贴
     */
    private String subsidy;

    /**
     * 津贴说明
     */
    private String text;

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

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDepartmentType() {
        return departmentType;
    }

    public void setDepartmentType(String departmentType) {
        this.departmentType = departmentType;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getBasePay() {
        return basePay;
    }

    public void setBasePay(String basePay) {
        this.basePay = basePay;
    }

    public String getSubsidy() {
        return subsidy;
    }

    public void setSubsidy(String subsidy) {
        this.subsidy = subsidy;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
