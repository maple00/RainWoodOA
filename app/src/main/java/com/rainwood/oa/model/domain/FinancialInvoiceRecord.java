package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: sxs
 * @Time: 2020/5/30 20:10
 * @Desc: 财务管理----开票记录
 */
public final class FinancialInvoiceRecord implements Serializable {

    /**
     * 记录
     */
    private String id;

    /**
     * 员工
     */
    private String stffName;

    /**
     * 开票类型
     */
    private String type;

    /**
     * 开票金额
     */
    private String money;

    /**
     * 开票单号
     */
    private String invoiceNum;

    /**
     * 开票日期
     */
    private String openDate;
    /**
     * 是否已开票
     */
    private String open;

    /**
     * 开票对象
     */
    private String companyName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStffName() {
        return stffName;
    }

    public void setStffName(String stffName) {
        this.stffName = stffName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getInvoiceNum() {
        return invoiceNum;
    }

    public void setInvoiceNum(String invoiceNum) {
        this.invoiceNum = invoiceNum;
    }

    public String getOpenDate() {
        return openDate;
    }

    public void setOpenDate(String openDate) {
        this.openDate = openDate;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
