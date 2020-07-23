package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/7/22 9:19
 * @Desc: 开票记录详情
 */
public final class InvoiceDetailData implements Serializable {

    /**
     * 销售方
     */
    private String company;

    /**
     * 开票人
     */
    private String stffName;

    /**
     *购买方
     */
    private String companyName;

    /**
     * 发票类型
     */
    private String type;

    /***
     * 价税合计
     */
    private double money;

    /**
     * 开票金额
     */
    private double moneyInvoice;

    /**
     * 纳税金额
     */
    private double moneyTax;

    /**
     * 备注信息
     */
    private String text;

    /**
     * 发票编号
     */
    private String invoiceNum;

    /**
     * 发票
     */
    private String ico;

    public String getStffName() {
        return stffName;
    }

    public void setStffName(String stffName) {
        this.stffName = stffName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public double getMoneyInvoice() {
        return moneyInvoice;
    }

    public void setMoneyInvoice(double moneyInvoice) {
        this.moneyInvoice = moneyInvoice;
    }

    public double getMoneyTax() {
        return moneyTax;
    }

    public void setMoneyTax(double moneyTax) {
        this.moneyTax = moneyTax;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getInvoiceNum() {
        return invoiceNum;
    }

    public void setInvoiceNum(String invoiceNum) {
        this.invoiceNum = invoiceNum;
    }

    public String getIco() {
        return ico;
    }

    public void setIco(String ico) {
        this.ico = ico;
    }
}