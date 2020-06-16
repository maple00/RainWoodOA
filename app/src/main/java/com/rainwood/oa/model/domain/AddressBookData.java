package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/6/16 17:36
 * @Desc: 通讯录员工
 */
public final class AddressBookData implements Serializable {

    /**
     * 名字
     */
    private String name;

    /**
     * 职位
     */
    private String job;

    /**
     * 电话
     */
    private String tel;

    /**
     * qq
     */
    private String qq;

    /**
     * 头像
     */
    private String ico;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getIco() {
        return ico;
    }

    public void setIco(String ico) {
        this.ico = ico;
    }
}
