package com.rainwood.oa.presenter;

import com.rainwood.oa.base.IBasePresenter;
import com.rainwood.oa.view.ICustomCallbacks;

/**
 * @Author: a797s
 * @Date: 2020/5/18 14:46
 * @Desc:
 */
public interface ICustomPresenter extends IBasePresenter<ICustomCallbacks> {

    /**
     * 请求跟进状态
     */
    void requestFollowStatus();

    /**
     * 请求客户来源
     */
    void requestCustomOrigin();

    /**
     * 新增客户
     */
    void createCustomData(String userName, String tel, String wxNum, String qqNum, String post,
                          String followStatus, String origin, String note, String preMoney, String industry,
                          String demand, String company, String taxNum, String emailAddress,
                          String invoiceAddress, String landLine, String bankName, String bankNo);

    /**
     * 请求客户列表
     */
    void getALlCustomData(int page);

    /**
     * 查询状态
     */
    void getStatus();

    /**
     * 客户详情中的固定module数据
     */
    void getDetailData();

    /**
     * 通过客户id查询客户详情信息
     */
    void requestCustomDetailById(String khid);

    /**
     * 通过客户id查询其下的联系人列表
     * @param khid 客户id
     */
    void requestContactListByCustomId(String khid);

    /**
     * 创建联系人
     */
    void requestCreateContact(String customId, String contactId, String post, String name, String tel, String lineTel, String qqNum,
                              String wxNum, String note);

    /**
     * 请求客户详情模块中的员工列表
     */
    void requestCustomStaff();

    /**
     * 添加协作人
     */
    void requestPlusAssociates(String customId, String staffId);

    /**
     * 删除协作人
     * @param customId 客户id
     * @param staffId 员工id
     */
    void requestDeleteAssociates(String customId, String staffId);

    /**
     * 转让客户
     * @param customId 客户id
     * @param staffId 被转让的员工ID
     */
    void requestRevCustom(String customId, String staffId);
}
