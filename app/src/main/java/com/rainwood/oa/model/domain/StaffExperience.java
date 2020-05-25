package com.rainwood.oa.model.domain;

/**
 * @Author: a797s
 * @Date: 2020/5/25 10:40
 * @Desc: 员工工作经历
 */
public final class StaffExperience {

    /**
     * 公司
     */
    private String company;

    /**
     * 职位
     */
    private String post;

    /**
     * 入职时间
     */
    private String entryTime;

    /**
     * 离职时间
     */
    private String departureTime;

    /**
     * 职责
     */
    private String responsibility;

    /**
     * 离职原因
     */
    private String reason;

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
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

    public String getResponsibility() {
        return responsibility;
    }

    public void setResponsibility(String responsibility) {
        this.responsibility = responsibility;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
