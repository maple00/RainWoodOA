package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/6/1 15:51
 * @Desc: 新建订单-通过关键字查询客户
 */
public final class PrimaryKey implements Serializable {

    /**
     * 客户id
     */
    private String khid;

    /**
     * 客户名称
     */
    private String companyName;

    public String getKhid() {
        return khid;
    }

    public void setKhid(String khid) {
        this.khid = khid;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
