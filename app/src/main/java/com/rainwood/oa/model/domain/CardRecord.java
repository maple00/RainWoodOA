package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/5/27 9:06
 * @Desc: 补卡记录
 */
public final class CardRecord implements Serializable {

    /**
     * id
     */
    private String id;
    /**
     * 申请人
     */
    private String staffName;

    /**
     * 状态
     */
    private String workFlow;

    /**
     * 补卡时间
     */
    private String signTime;

    /**
     * 补卡内容
     */
    private String text;

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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSignTime() {
        return signTime;
    }

    public void setSignTime(String signTime) {
        this.signTime = signTime;
    }
}
