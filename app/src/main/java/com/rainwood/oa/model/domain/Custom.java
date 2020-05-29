package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * create by a797s in 2020/5/15 13:06
 *
 * @Description : 客户
 * @Usage :
 **/
public final class Custom implements Serializable {

    /**
     * 客户id
     */
    private String khid;

    /**
     * 客户负责人名字
     */
    private String staff;

    /**
     * 协作人
     */
    private String edit;

    /**
     * 客户公司名称
     */
    private String companyName;

    /**
     * 签约状态
     */
    private String workFlow;

    /**
     * 联系人
     */
    private String contactName;

    /**
     * 联系人电话
     */
    private String contactTel;

    /**
     * 客户需求大概描述
     */
    private String text;

    /**
     * 创建时间
     */
    private String time;

    /**
     * 选中
     */
    private boolean selected;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactTel() {
        return contactTel;
    }

    public void setContactTel(String contactTel) {
        this.contactTel = contactTel;
    }

    public String getEdit() {
        return edit;
    }

    public void setEdit(String edit) {
        this.edit = edit;
    }

    public String getKhid() {
        return khid;
    }

    public void setKhid(String khid) {
        this.khid = khid;
    }

    public String getStaff() {
        return staff;
    }

    public void setStaff(String staff) {
        this.staff = staff;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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

    @Override
    public String toString() {
        return "Custom{" +
                "khid='" + khid + '\'' +
                ", staff='" + staff + '\'' +
                ", edit='" + edit + '\'' +
                ", companyName='" + companyName + '\'' +
                ", workFlow='" + workFlow + '\'' +
                ", contactName='" + contactName + '\'' +
                ", contactTel='" + contactTel + '\'' +
                ", text='" + text + '\'' +
                ", time='" + time + '\'' +
                ", selected=" + selected +
                '}';
    }
}
