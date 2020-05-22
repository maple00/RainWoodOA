package com.rainwood.oa.model.domain;

/**
 * @Author: a797s
 * @Date: 2020/5/22 9:25
 * @Desc: 职位
 */
public final class Post {
    /**
     * 职位名称
     */
    private String post;

    /**
     * 所属部门
     */
    private String depart;

    /**
     * 所属角色
     */
    private String role;

    /**
     * 基本工资
     */
    private String baseSalary;

    /**
     * 岗位津贴
     */
    private String postAllowance;

    /**
     * 津贴说明
     */
    private String allowanceDesc;

    /**
     * 职位描述
     */
    private String postDesc;

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getDepart() {
        return depart;
    }

    public void setDepart(String depart) {
        this.depart = depart;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(String baseSalary) {
        this.baseSalary = baseSalary;
    }

    public String getPostAllowance() {
        return postAllowance;
    }

    public void setPostAllowance(String postAllowance) {
        this.postAllowance = postAllowance;
    }

    public String getAllowanceDesc() {
        return allowanceDesc;
    }

    public void setAllowanceDesc(String allowanceDesc) {
        this.allowanceDesc = allowanceDesc;
    }

    public String getPostDesc() {
        return postDesc;
    }

    public void setPostDesc(String postDesc) {
        this.postDesc = postDesc;
    }
}
