package com.rainwood.oa.view;

import com.rainwood.oa.base.IBaseCallback;

import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/6/3 17:25
 * @Desc: 联系人---
 */
public interface IContactsCallbacks extends IBaseCallback {

    void getAddressBookData(Map dataMap);
}
