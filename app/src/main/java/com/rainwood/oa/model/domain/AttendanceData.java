package com.rainwood.oa.model.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/29 17:27
 * @Desc: 考勤数据
 */
public final class AttendanceData implements Serializable {

    @Override
    public String toString() {
        return "AttendanceData{" +
                "calendar=" + calendar +
                ", ico='" + ico + '\'' +
                ", name='" + name + '\'' +
                ", job='" + job + '\'' +
                ", ymh='" + ymh + '\'' +
                ", basePay='" + basePay + '\'' +
                ", subsidy='" + subsidy + '\'' +
                ", money='" + money + '\'' +
                ", lastMoney='" + lastMoney + '\'' +
                ", hourAll='" + hourAll + '\'' +
                ", lawHour='" + lawHour + '\'' +
                ", workAddHour='" + workAddHour + '\'' +
                ", outHour='" + outHour + '\'' +
                ", lateNum='" + lateNum + '\'' +
                ", lateMoney='" + lateMoney + '\'' +
                ", leave='" + leave + '\'' +
                ", leaveMoney='" + leaveMoney + '\'' +
                ", basePayMoon='" + basePayMoon + '\'' +
                ", subsidyMoon='" + subsidyMoon + '\'' +
                ", fullAttendance='" + fullAttendance + '\'' +
                ", pay='" + pay + '\'' +
                ", workOvertimeText='" + workOvertimeText + '\'' +
                ", fullAttendanceText='" + fullAttendanceText + '\'' +
                '}';
    }

    /**
     * 日历数据
     */
   private List<AttendanceCalendarData> calendar;

    /**
     * 头像
     */
    private String ico;

    /**
     * 姓名
     */
    private String name;

    /**
     * 职位
     */
    private String job;

    /**
     * 平均工作小时数
     */
    private String ymh;

    /**
     * 基本工资
     */
    private String basePay;

    /**
     * 岗位津贴
     */
    private String subsidy;

    /**
     * 会计账户
     */
    private String money;

    /**
     * 结算账户
     */
    private String lastMoney;

    /**
     * 本月实到小时数
     */
    private String hourAll;

    /**
     * 本月应到小时数
     */
    private String lawHour;

    /**
     * 本月加班小时数量
     */
    private String workAddHour;

    /**
     * 本月超出法定小时数量
     */
    private String outHour;

    /**
     * 迟到次数
     */
    private String lateNum;

    /**
     * 迟到处罚金额
     */
    private String lateMoney;

    /**
     * 矿工小时数
     */
    private String leave;

    /**
     * 矿工处罚金额
     */
    private String leaveMoney;

    /**
     * 基本工资
     */
    private String basePayMoon;

    /**
     * 岗位津贴
     */
    private String subsidyMoon;

    /**
     * 全勤奖
     */
    private String fullAttendance;
    /**
     * 实得工资
     */
    private String pay;

    /**
     * 加班提示
     */
    private String workOvertimeText;

    /**
     * 全勤奖提示
     */
    private String fullAttendanceText;

    public List<AttendanceCalendarData> getCalendar() {
        return calendar;
    }

    public void setCalendar(List<AttendanceCalendarData> calendar) {
        this.calendar = calendar;
    }

    public String getIco() {
        return ico;
    }

    public void setIco(String ico) {
        this.ico = ico;
    }

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

    public String getYmh() {
        return ymh;
    }

    public void setYmh(String ymh) {
        this.ymh = ymh;
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

    public String getHourAll() {
        return hourAll;
    }

    public void setHourAll(String hourAll) {
        this.hourAll = hourAll;
    }

    public String getLawHour() {
        return lawHour;
    }

    public void setLawHour(String lawHour) {
        this.lawHour = lawHour;
    }

    public String getWorkAddHour() {
        return workAddHour;
    }

    public void setWorkAddHour(String workAddHour) {
        this.workAddHour = workAddHour;
    }

    public String getOutHour() {
        return outHour;
    }

    public void setOutHour(String outHour) {
        this.outHour = outHour;
    }

    public String getLateNum() {
        return lateNum;
    }

    public void setLateNum(String lateNum) {
        this.lateNum = lateNum;
    }

    public String getLateMoney() {
        return lateMoney;
    }

    public void setLateMoney(String lateMoney) {
        this.lateMoney = lateMoney;
    }

    public String getLeave() {
        return leave;
    }

    public void setLeave(String leave) {
        this.leave = leave;
    }

    public String getLeaveMoney() {
        return leaveMoney;
    }

    public void setLeaveMoney(String leaveMoney) {
        this.leaveMoney = leaveMoney;
    }

    public String getBasePayMoon() {
        return basePayMoon;
    }

    public void setBasePayMoon(String basePayMoon) {
        this.basePayMoon = basePayMoon;
    }

    public String getSubsidyMoon() {
        return subsidyMoon;
    }

    public void setSubsidyMoon(String subsidyMoon) {
        this.subsidyMoon = subsidyMoon;
    }

    public String getFullAttendance() {
        return fullAttendance;
    }

    public void setFullAttendance(String fullAttendance) {
        this.fullAttendance = fullAttendance;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    public String getWorkOvertimeText() {
        return workOvertimeText;
    }

    public void setWorkOvertimeText(String workOvertimeText) {
        this.workOvertimeText = workOvertimeText;
    }

    public String getFullAttendanceText() {
        return fullAttendanceText;
    }

    public void setFullAttendanceText(String fullAttendanceText) {
        this.fullAttendanceText = fullAttendanceText;
    }
}
