package com.rainwood.oa.model.domain;

/**
 * @Author: a797s
 * @Date: 2020/5/25 10:40
 * @Desc: 员工工作经历
 */
public final class StaffExperience {

    /**
     * 工作经历id
     */
    private String id;

    /**
     * 公司
     */
    private String companyName;

    /**
     * 职位
     */
    private String position;

    /**
     * 入职时间
     */
    private String entryTime;

    /**
     * 离职时间
     */
    private String departureTime;

    /**
     * 入职离职时间
     */
    private String dayStart;

    /**
     * 职责
     */
    private String content;

    /**
     * 离职原因
     */
    private String cause;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDayStart() {
        return dayStart;
    }

    public void setDayStart(String dayStart) {
        this.dayStart = dayStart;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(String entryTime) {
        this.entryTime = entryTime;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }
}
