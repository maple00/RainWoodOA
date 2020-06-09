package com.rainwood.oa.model.domain;

/**
 * @Author: a797s
 * @Date: 2020/5/22 14:44
 * @Desc: 员工信息
 */
public final class Staff {

    /**
     * 员工id
     */
    private String id;

    /**
     * 员工姓名
     */
    private String name;

    /**
     * 员工头像
     */
    private String ico;

    /**
     * 基本工资
     */
    private String basePay;

    /**
     * 岗位津贴
     */
    private String subsidy;

    /**
     * 员工所属部门
     */
    private String depart;

    /**
     * 员工职位
     */
    private String post;

    /**
     * 部门职位
     */
    private String department;

    /**
     * 员工手机号
     */
    private String tel;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIco() {
        return ico;
    }

    public void setIco(String ico) {
        this.ico = ico;
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

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
