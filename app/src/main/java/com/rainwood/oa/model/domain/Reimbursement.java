package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/6/4 14:26
 * @Desc: 费用报销
 */
public final class Reimbursement implements Serializable {

    /**
     * 费用类型
     */
    private String type;

    /**
     * 费用金额
     */
    private String money;

    /**
     * 是否有报销凭证
     */
    private boolean voucher;

    /**
     * 报销人
     */
    private String name;

    /**
     * 报销事由
     */
    private String content;

    /**
     * 是否已拨付
     */
    private String allocated;

    /**
     * 拨付时间
     */
    private String time;

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

    public boolean isVoucher() {
        return voucher;
    }

    public void setVoucher(boolean voucher) {
        this.voucher = voucher;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAllocated() {
        return allocated;
    }

    public void setAllocated(String allocated) {
        this.allocated = allocated;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

