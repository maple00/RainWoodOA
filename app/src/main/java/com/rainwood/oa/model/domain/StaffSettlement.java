package com.rainwood.oa.model.domain;

/**
 * @Author: a797s
 * @Date: 2020/5/25 18:19
 * @Desc: 员工结算账户
 */
public final class StaffSettlement {

    /**
     * 结算事件
     */
    private String event;

    /**
     * 结算金额
     */
    private String money;

    /**
     * 结算时间
     */
    private String time;

    /**
     * 支付凭证【图片】
     */
    private String voucher;

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVoucher() {
        return voucher;
    }

    public void setVoucher(String voucher) {
        this.voucher = voucher;
    }
}

