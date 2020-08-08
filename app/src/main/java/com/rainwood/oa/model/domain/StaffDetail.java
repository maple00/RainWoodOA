package com.rainwood.oa.model.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/10 19:24
 * @Desc: 员工详情
 */
public final class StaffDetail implements Serializable {

    /**
     * 员工姓名
     */
    private String name = "无";

    /**
     * 员工职位
     */
    private String job = "无";

    /**
     * 性别
     */
    private String sex = "无";

    /**
     * 入职时间
     */
    private String entryTime = "无";

    /**
     * 是否在岗
     */
    private String state = "无";

    /**
     * 是否有社保
     */
    private String socialSecurity = "无";

    /**
     * 是否有门钥匙
     */
    private String gateKey = "无";

    /**
     * 电话
     */
    private String tel = "无";

    /**
     * QQ
     */
    private String qq = "无";

    /**
     * 员工id
     */
    private String stid = "无";

    /**
     * 登陆名字
     */
    private String loginName = "无";

    /**
     * 直属上级
     */
    private String manager = "无";

    /**
     * 毕业院校
     */
    private String school = "无";

    /**
     * 所学专业
     */
    private String schoolMajor;

    /**
     * 毕业时间
     */
    private String schoolEnd = "无";

    /**
     * 工资卡银行
     */
    private String bankName = "无";

    /**
     * 银行卡号
     */
    private String bankNum = "无";

    /**
     * 社保号
     */
    private String socialSecurityNum = "无";

    /**
     * 钉钉id
     */
    private String dingtalkId = "无";

    /**
     * 备注
     */
    private String text = "无";

    /**
     * 头像
     */
    private String ico;

    /**
     * 身份证正面
     */
    private String IDCardFront;

    /**
     * 身份证反面
     */
    private String IDCardBack ;

    /**
     * 毕业证扫描证件码
     */
    private String diploma;

    /**
     * 银行卡
     */
    private String bankIco;

    /**
     * 工作经历
     */
    private List<StaffExperience> jobHistory;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSocialSecurity() {
        return socialSecurity;
    }

    public void setSocialSecurity(String socialSecurity) {
        this.socialSecurity = socialSecurity;
    }

    public String getGateKey() {
        return gateKey;
    }

    public void setGateKey(String gateKey) {
        this.gateKey = gateKey;
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

    public String getStid() {
        return stid;
    }

    public void setStid(String stid) {
        this.stid = stid;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getSchoolMajor() {
        return schoolMajor;
    }

    public void setSchoolMajor(String schoolMajor) {
        this.schoolMajor = schoolMajor;
    }

    public String getSchoolEnd() {
        return schoolEnd;
    }

    public void setSchoolEnd(String schoolEnd) {
        this.schoolEnd = schoolEnd;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankNum() {
        return bankNum;
    }

    public void setBankNum(String bankNum) {
        this.bankNum = bankNum;
    }

    public String getSocialSecurityNum() {
        return socialSecurityNum;
    }

    public void setSocialSecurityNum(String socialSecurityNum) {
        this.socialSecurityNum = socialSecurityNum;
    }

    public String getDingtalkId() {
        return dingtalkId;
    }

    public void setDingtalkId(String dingtalkId) {
        this.dingtalkId = dingtalkId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIco() {
        return ico;
    }

    public void setIco(String ico) {
        this.ico = ico;
    }

    public String getIDCardFront() {
        return IDCardFront;
    }

    public void setIDCardFront(String IDCardFront) {
        this.IDCardFront = IDCardFront;
    }

    public String getIDCardBack() {
        return IDCardBack;
    }

    public void setIDCardBack(String IDCardBack) {
        this.IDCardBack = IDCardBack;
    }

    public String getDiploma() {
        return diploma;
    }

    public void setDiploma(String diploma) {
        this.diploma = diploma;
    }

    public String getBankIco() {
        return bankIco;
    }

    public void setBankIco(String bankIco) {
        this.bankIco = bankIco;
    }

    public List<StaffExperience> getJobHistory() {
        return jobHistory;
    }

    public void setJobHistory(List<StaffExperience> jobHistory) {
        this.jobHistory = jobHistory;
    }
}
