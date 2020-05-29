package com.rainwood.oa.view;

import com.rainwood.oa.base.IBaseCallback;
import com.rainwood.oa.model.domain.Contact;
import com.rainwood.oa.model.domain.CustomStaff;

import java.util.List;
import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/5/18 14:45
 * @Desc:
 */
public interface ICustomCallbacks extends IBaseCallback {

    /**
     * 客户选择列表请求
     * 返回客户跟进状态数据
     */
    default void getFollowData(List<String> followList){};

    /**
     * 客户来源
     * @param originList
     */
    default void getCustomOrigin(List<String> originList){};

    /**
     * 创建客户
     */
    default void createCustomData(boolean isSuccess, String warn){ }

    /**
     * 获取客户列表
     *
     * @param customList
     */
    default void getAllCustomList(List customList){};

    /**
     * 获取所有的状态
     */
    default void getALlStatus(Map statusMap){};

    /**
     * 客户详情数据回调
     * 固定页面数据
     */
    default void getCustomDetailData(Map<String, List> contentMap){}

    /**
     * 客户详情数据
     */
    default void getCustomDetailValues(Map dataMap){}

    /**
     * 获取客户下的联系人列表
     */
    default void getCustomContactList(List<Contact> contactList){}

    /**
     * 创建客户下的联系人
     */
    default void createContactData(boolean success){}

    /**
     * 客户下的员工列表
      * @param customStaffList
     */
    default void getCustomOfStaff(List<CustomStaff> customStaffList){}

}
