package com.rainwood.oa.view;

import com.rainwood.oa.base.IBaseCallback;
import com.rainwood.oa.model.domain.Contact;

import java.util.List;
import java.util.Map;

/**
 * create by a797s in 2020/5/15 16:50
 *
 * @Description : 联系人
 * @Usage :
 **/
public interface IContactCallbacks extends IBaseCallback {

    /**
     * 返回联系人
     */
    void getAllContact(Map<String, List> contactData);
}
