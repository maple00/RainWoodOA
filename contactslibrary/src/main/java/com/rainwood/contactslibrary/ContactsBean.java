package com.rainwood.contactslibrary;

import com.rainwood.contactslibrary.IndexBar.bean.BaseIndexPinyinBean;

import java.io.Serializable;

/**
 * Created by zhangxutong .
 * Date: 16/08/28
 */

public class ContactsBean extends BaseIndexPinyinBean implements Serializable {

    /**
     * 员工id
     */
    private String stid;

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

    public ContactsBean() {
    }

    public String getStid() {
        return stid;
    }

    public void setStid(String stid) {
        this.stid = stid;
    }

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

    /**
     * 拼音化
     * @return
     */
    @Override
    public String getTarget() {
        return name;
    }
}
