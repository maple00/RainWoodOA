package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/6/12 18:02
 * @Desc: 我的开票记录
 */
public final class MineInvoice implements Serializable {

    /**
     * id
     */
    private String id;

    /**
     * 客户公司名称
     */
    private String clientCompanyName;

    /**
     * 发票类型
     */
    private String type;

    /**
     * 金额
     */
    private String money;

    /**
     * 发票编号
     */
    private String invoiceNum;

    /**
     * 开票日期
     */
    private String openDate;

    /**
     * 是否开票
     */
    private String open;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientCompanyName() {
        return clientCompanyName;
    }

    public void setClientCompanyName(String clientCompanyName) {
        this.clientCompanyName = clientCompanyName;
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
}
