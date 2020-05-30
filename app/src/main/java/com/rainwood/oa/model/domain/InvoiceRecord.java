package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: sxs
 * @Time: 2020/5/30 20:10
 * @Desc: 开票记录
 */
public final class InvoiceRecord implements Serializable {

    /**
     * 发票id
     */
    private String invoiceNum;

    /**
     * 发票类型
     */
    private String type;

    /**
     * 发票开票状态
     * 是/否
     */
    private String open;

    /**
     * 开票方名称
     */
    private String company;

    /**
     * 开票金额
     */
    private String money;

    /**
     * 开票时间
     */
    private String openDate;

    public String getInvoiceNum() {
        return invoiceNum;
    }

    public void setInvoiceNum(String invoiceNum) {
        this.invoiceNum = invoiceNum;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getOpenDate() {
        return openDate;
    }

    public void setOpenDate(String openDate) {
        this.openDate = openDate;
    }
}
