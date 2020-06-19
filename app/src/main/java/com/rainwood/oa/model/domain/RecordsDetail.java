package com.rainwood.oa.model.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/19 17:00
 * @Desc: 记录详情(加班记录、)
 */
public final class RecordsDetail implements Serializable {

    /**
     * 申请人
     */
    private String staffName;

    /**
     * 状态
     */
    private String workFlow;

    /**
     * 审批人
     */
    private String examine;

    /**
     * 预计加班时间
     */
    private String expectTime;

    /**
     * 实际加班时间
     */
    private String time;

    /**
     * 审批记录
     */
    private List<RecordApproval> auditingFollow;

    /**
     * 记录成果
     */
    private List<RecordResults> workAddFruit;

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getWorkFlow() {
        return workFlow;
    }

    public void setWorkFlow(String workFlow) {
        this.workFlow = workFlow;
    }

    public String getExamine() {
        return examine;
    }

    public void setExamine(String examine) {
        this.examine = examine;
    }

    public String getExpectTime() {
        return expectTime;
    }

    public void setExpectTime(String expectTime) {
        this.expectTime = expectTime;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<RecordApproval> getAuditingFollow() {
        return auditingFollow;
    }

    public void setAuditingFollow(List<RecordApproval> auditingFollow) {
        this.auditingFollow = auditingFollow;
    }

    public List<RecordResults> getWorkAddFruit() {
        return workAddFruit;
    }

    public void setWorkAddFruit(List<RecordResults> workAddFruit) {
        this.workAddFruit = workAddFruit;
    }
}
