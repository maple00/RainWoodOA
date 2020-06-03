package com.rainwood.contactslibrary;

import com.rainwood.contactslibrary.IndexBar.bean.BaseIndexPinyinBean;

/**
 * Created by zhangxutong .
 * Date: 16/08/28
 */

public class ContactsBean extends BaseIndexPinyinBean {

    private String contact;//城市名字

    public ContactsBean() {
    }

    public ContactsBean(String contact) {
        this.contact = contact;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    @Override
    public String getTarget() {
        return contact;
    }
}
