package com.rainwood.oa.view;

import com.rainwood.oa.base.IBaseCallback;
import com.rainwood.oa.model.domain.Contact;
import com.rainwood.oa.model.domain.CustomArea;
import com.rainwood.oa.model.domain.CustomInvoice;
import com.rainwood.oa.model.domain.CustomScreenAll;
import com.rainwood.oa.model.domain.CustomStaff;
import com.rainwood.oa.model.domain.SelectedItem;

import java.util.List;
import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/5/18 14:45
 * @Desc: 客户callbacks
 */
public interface ICustomCallbacks extends IBaseCallback {

    /**
     * 客户选择列表请求
     * 返回客户跟进状态数据
     */
    default void getFollowData(List<String> followList) {
    }

    ;

    /**
     * 客户来源
     *
     * @param originList
     */
    default void getCustomOrigin(List<String> originList) {
    }

    ;

    /**
     * 创建客户
     */
    default void createCustomData(boolean isSuccess, String warn) {
    }

    /**
     * 获取温馨提示
     */
    default void getWarnPrompt(String warnPrompt) {
    }

    /**
     * 新建介绍客户
     */
    default void getIntroduceCreateData(Map<String, String> createMap) {
    }

    /**
     * 获取客户列表
     *
     * @param customList
     */
    default void getAllCustomList(List customList) {
    }

    ;

    /**
     * 客户列表状态
     */
    default void getStateCondition(List<SelectedItem> stateList) {
    }

    /**
     * 获取所有的状态
     */
    default void getALlStatus(Map statusMap) {
    }

    ;

    /**
     * 客户详情数据回调
     * 固定页面数据
     */
    default void getCustomDetailData(Map<String, List> contentMap) {
    }

    /**
     * 客户详情数据
     */
    default void getCustomDetailValues(Map dataMap) {
    }

    /**
     * 获取客户下的联系人列表
     */
    default void getCustomContactList(List<Contact> contactList) {
    }

    /**
     * 创建客户下的联系人
     */
    default void createContactData(boolean success) {
    }

    /**
     * 客户下的员工列表
     *
     * @param customStaffList
     */
    default void getCustomOfStaff(List<CustomStaff> customStaffList) {
    }

    /**
     * 客户下的开票信息
     */
    default void getCustomInvoice(CustomInvoice invoice) {
    }

    /**
     * 客户列表筛选条件
     *
     * @param customListCondition
     */
    default void getCustomListCondition(List<CustomScreenAll> customListCondition) {
    }

    /**
     * 客户列表condition -- province
     *
     * @param provinceList
     */
    default void getCustomListProvince(List<CustomArea> provinceList) {
    }

    /**
     * 客户列表condition -- city
     *
     * @param cityList
     */
    default void getCustomCity(List<CustomArea> cityList) {
    }

    /**
     * 客户列表condition -- area
     *
     * @param areaList
     */
    default void getCustomArea(List<CustomArea> areaList) {
    }

    /**
     * 添加协作人
     *
     * @param success
     * @param warn
     */
    default void getAssociatesResult(boolean success, String warn) {
    }

    /**
     * 删除协作人
     * @param success
     * @param warn
     */
    default void getDeleteAssociatesResult(boolean success, String warn){}

    /**
     * 转让客户
     * @param success
     * @param warn
     */
    default void getMoveCustomResult(boolean success, String warn){}

    /**
     * 跟进记录标签
     * @param selectedList
     */
    default void getFollowLabels(List<SelectedItem> selectedList){}

    /**
     * 新增跟进记录结果
     * @param warn
     */
    default void getFollowLabelResult(String warn){}
}
