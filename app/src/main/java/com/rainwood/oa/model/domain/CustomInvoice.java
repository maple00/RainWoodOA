package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: sxs
 * @Time: 2020/5/30 17:04
 * @Desc: 客户开票记录
 */
public final class CustomInvoice implements Serializable {

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 纳税人识别号
     */
    private String companyNum;

    /**
     * 开户银行
     */
    private String companyBank;

    /**
     * 开户银行对公账号
     */
    private String companyBankNum;

    /**
     * 开票地址
     */
    private String companyAddress;

    /**
     * 座机
     */
    private String landline;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyNum() {
        return companyNum;
    }

    public void setCompanyNum(String companyNum) {
        this.companyNum = companyNum;
    }

    public String getCompanyBank() {
        return companyBank;
    }

    public void setCompanyBank(String companyBank) {
        this.companyBank = companyBank;
    }

    public String getCompanyBankNum() {
        return companyBankNum;
    }

    public void setCompanyBankNum(String companyBankNum) {
        this.companyBankNum = companyBankNum;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getLandline() {
        return landline;
    }

    public void setLandline(String landline) {
        this.landline = landline;
    }
}
