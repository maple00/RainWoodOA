package com.rainwood.oa.presenter.impl;

import com.rainwood.oa.model.domain.Contact;
import com.rainwood.oa.presenter.IContactPresenter;
import com.rainwood.oa.view.IContactCallbacks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * create by a797s in 2020/5/15 16:51
 *
 * @Description : 联系人
 * @Usage :
 **/
public final class ContactImpl implements IContactPresenter {

    private IContactCallbacks mIContactCallback;

    @Override
    public void getAllContact() {
        Map<String, List> contactData = new HashMap<>();

        List<Contact> contactList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Contact contact = new Contact();
            contact.setName("伊泽瑞尔");
            contact.setPosition("极限冒险家");
            contact.setTel("135 1525 0074");
            contact.setPhone("400 0000 0000");
            contact.setWeChat("chen0074");
            contact.setQq("1175039036");
            contact.setText("肖主管专门负责和乙方公司的对接，上午9点到晚上11点都可以随时联系，每周五下午例会时间不能及时回复。");
            contactList.add(contact);
        }

        contactData.put("contact", contactList);

        mIContactCallback.getAllContact(contactData);
    }

    @Override
    public void registerViewCallback(IContactCallbacks callback) {
        this.mIContactCallback = callback;
    }

    @Override
    public void unregisterViewCallback(IContactCallbacks callback) {
        mIContactCallback = null;
    }
}
