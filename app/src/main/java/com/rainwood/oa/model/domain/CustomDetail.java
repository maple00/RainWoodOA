package com.rainwood.oa.model.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/28 10:36
 * @Desc: 客户详情
 */
public final class CustomDetail implements Serializable {

    /**
     * 负责人---所属员工
     */
    private CustomValues staff;

    /**
     * 分享人--- 介绍人
     */
    private CustomValues share;

    /**
     * 协作人
     */
    private List<CustomValues> edit;

    /**
     * 联系人
     */
    private List<Contact> kehuStaff;

    /**
     * 客户需求
     */
    private CustomDemand demand;

    /**
     * 客户信息
     */
    private Custom kehu;

    public CustomValues getStaff() {
        return staff;
    }

    public void setStaff(CustomValues staff) {
        this.staff = staff;
    }

    public CustomValues getShare() {
        return share;
    }

    public void setShare(CustomValues share) {
        this.share = share;
    }

    public List<CustomValues> getEdit() {
        return edit;
    }

    public void setEdit(List<CustomValues> edit) {
        this.edit = edit;
    }

    public List<Contact> getKehuStaff() {
        return kehuStaff;
    }

    public void setKehuStaff(List<Contact> kehuStaff) {
        this.kehuStaff = kehuStaff;
    }

    public CustomDemand getDemand() {
        return demand;
    }

    public void setDemand(CustomDemand demand) {
        this.demand = demand;
    }

    public Custom getKehu() {
        return kehu;
    }

    public void setKehu(Custom kehu) {
        this.kehu = kehu;
    }
}
