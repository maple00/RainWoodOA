package com.rainwood.oa.model.domain;

/**
 * @Author: a797s
 * @Date: 2020/5/22 14:44
 * @Desc: 员工信息
 */
public final class Staff {

    /**
     * 员工姓名
     */
    private String name;

    /**
     * 员工头像
     */
    private String headPhoto;

    /**
     * 基本工资
     */
    private String baseSalary;

    /**
     * 岗位津贴
     */
    private String allowance;

    /**
     * 员工所属部门
     */
    private String depart;

    /**
     * 员工职位
     */
    private String post;

    /**
     * 员工手机号
     */
    private String telNum;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadPhoto() {
        return headPhoto;
    }

    public void setHeadPhoto(String headPhoto) {
        this.headPhoto = headPhoto;
    }

    public String getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(String baseSalary) {
        this.baseSalary = baseSalary;
    }

    public String getAllowance() {
        return allowance;
    }

    public void setAllowance(String allowance) {
        this.allowance = allowance;
    }

    public String getDepart() {
        return depart;
    }

    public void setDepart(String depart) {
        this.depart = depart;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getTelNum() {
        return telNum;
    }

    public void setTelNum(String telNum) {
        this.telNum = telNum;
    }
}
