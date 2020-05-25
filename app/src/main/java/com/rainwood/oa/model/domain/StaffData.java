package com.rainwood.oa.model.domain;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/22 14:44
 * @Desc: 员工详细资料
 */
public final class StaffData {

    /**
     * 员工姓名
     */
    private String name;

    /**
     * 员工部门
     */
    private String depart;

    /**
     * 员工职位
     */
    private String post;

    /**
     * 员工性别
     */
    private String sex;

    /**
     * 入职时间
     */
    private String entryTime;

    /**
     * 是否在岗
     */
    private String station;
    /*
    是否有社保，是否有办公室钥匙
     */

    /**
     * 员工电话
     */
    private String tel;

    /**
     * 员工QQ
     */
    private String qq;

    /**
     * 员工id
     */
    private String id;

    /**
     * 员工用户名
     */
    private String userName;

    /**
     * 员工登录密码
     */
    private String loginPwd;

    /**
     * 员工直属上级
     */
    private String directSupervisor;

    /**
     * 毕业院校
     */
    private String school;

    /**
     * 专业
     */
    private String major;

    /**
     * 毕业时间
     */
    private String graduationDate;

    /**
     * 工资卡银行
     */
    private String salaryBank;

    /**
     * 银行卡号
     */
    private String bankNo;

    /**
     * 社保号
     */
    private String socialSecurity;

    /**
     * 钉钉id
     */
    private String dingNo;

    /**
     * 备注
     */
    private String note;

    /**
     * 辅助信息
     */
    private List<StaffPhoto> photoList;

    /**
     * 工作经历
     */
    private List<StaffExperience> experiences;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(String entryTime) {
        this.entryTime = entryTime;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLoginPwd() {
        return loginPwd;
    }

    public void setLoginPwd(String loginPwd) {
        this.loginPwd = loginPwd;
    }

    public String getDirectSupervisor() {
        return directSupervisor;
    }

    public void setDirectSupervisor(String directSupervisor) {
        this.directSupervisor = directSupervisor;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getGraduationDate() {
        return graduationDate;
    }

    public void setGraduationDate(String graduationDate) {
        this.graduationDate = graduationDate;
    }

    public String getSalaryBank() {
        return salaryBank;
    }

    public void setSalaryBank(String salaryBank) {
        this.salaryBank = salaryBank;
    }

    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }

    public String getSocialSecurity() {
        return socialSecurity;
    }

    public void setSocialSecurity(String socialSecurity) {
        this.socialSecurity = socialSecurity;
    }

    public String getDingNo() {
        return dingNo;
    }

    public void setDingNo(String dingNo) {
        this.dingNo = dingNo;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<StaffPhoto> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(List<StaffPhoto> photoList) {
        this.photoList = photoList;
    }

    public List<StaffExperience> getExperiences() {
        return experiences;
    }

    public void setExperiences(List<StaffExperience> experiences) {
        this.experiences = experiences;
    }
}

