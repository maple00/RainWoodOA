package com.rainwood.oa.model.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/19 17:00
 * @Desc: 记录详情(加班记录 、)
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
     * 预计时间
     */
    private String expectTime;

    /**
     * 实际时间
     */
    private String time;

    /**
     * 补卡时间
     */
    private String signTime;

    /**
     * 事由
     */
    private String text;

    /**
     * 类型
     */
    private String type;
    /**
     * 审批记录
     */
    private List<RecordApproval> auditingFollow;

    /**
     * 加班成果
     */
    private List<RecordResults> workAddFruit;

    /**
     * 外出成果
     */
    private List<RecordResults> workOutFruit;


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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<RecordResults> getWorkOutFruit() {
        return workOutFruit;
    }

    public void setWorkOutFruit(List<RecordResults> workOutFruit) {
        this.workOutFruit = workOutFruit;
    }

    public String getSignTime() {
        return signTime;
    }

    public void setSignTime(String signTime) {
        this.signTime = signTime;
    }
}
