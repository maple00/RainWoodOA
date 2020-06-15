package com.rainwood.oa.model.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/11 16:15
 * @Desc: 个人中心数据
 */
public final class MineData implements Serializable {

    /**
     * 员工姓名
     */
    private String staffName;

    /**
     * 工作职位
     */
    private String job;

    /**
     * 手机号
     */
    private String tel;

    /**
     * 入职时间
     */
    private String entryTime;

    /**
     * 在岗状态
     */
    private String state;

    /**
     * 工作天数
     */
    private String entryDay;

    /**
     * 会计账户金额
     */
    private String money;

    /**
     * 结算金额
     */
    private String lastMoney;

    /**
     * 团队基金
     */
    private String teamFund;

    /**
     * 基本工资
     */
    private String basePay;

    /**
     * 岗位津贴
     */
    private String subsidy;

    /**
     * 员工头像
     */
    private String ico;

    /**
     * 个人中心模块
     */
    private List<IconAndFont> button;

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
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

    public String getEntryDay() {
        return entryDay;
    }

    public void setEntryDay(String entryDay) {
        this.entryDay = entryDay;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getLastMoney() {
        return lastMoney;
    }

    public void setLastMoney(String lastMoney) {
        this.lastMoney = lastMoney;
    }

    public String getTeamFund() {
        return teamFund;
    }

    public void setTeamFund(String teamFund) {
        this.teamFund = teamFund;
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

    public String getIco() {
        return ico;
    }

    public void setIco(String ico) {
        this.ico = ico;
    }

    public List<IconAndFont> getButton() {
        return button;
    }

    public void setButton(List<IconAndFont> button) {
        this.button = button;
    }
}
