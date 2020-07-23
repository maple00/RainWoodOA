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
    void createCustomData(String randomId, String userName, String tel, String wxNum, String qqNum, String post,
                          String followStatus, String origin, String note, String preMoney, String industry,
                          String demand, String company, String taxNum, String emailAddress,
                          String invoiceAddress, String landLine, String bankName, String bankNo);

    /**
     * 请求介绍客户中的温馨提示
     */
    void requestWarmPrompt();

    /**
     * 新增介绍客户
     */
    void createIntroduceCustom(String companyName, String contact, String tel, String demand, String origin,
                               String followState, String introduceObj);

    /**
     * 客户列表
     *
     * @param companyName 公司名称
     * @param headMan     负责人
     * @param references  介绍人
     * @param state       跟进状态
     * @param origin      客户来源
     * @param province    省
     * @param city        市
     * @param area        区
     * @param sorting     排序方式
     * @param page        页码
     */
    void requestALlCustomData(String companyName, String headMan, String references, String state,
                              String origin, String province, String city, String area, String sorting, int page);

    /**
     * 客户列表 -- condition
     */
    void requestCustomCondition();

    /**
     * 客户列表状态 --- condition
     */
    void requestStateCondition();

    /**
     * 客户列表 -- 省 condition
     */
    void requestProvinceCondition();

    /**
     * 客户列表 -- 通过 省 查询市
     */
    void requestCityByProvince(String province);

    /**
     * 通过省市查询区
     *
     * @param province
     * @param city
     */
    void requestAreaByProvinceCity(String province, String city);

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
     *
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
     *
     * @param customId 客户id
     * @param staffId  员工id
     */
    void requestDeleteAssociates(String customId, String staffId);

    /**
     * 转让客户
     *
     * @param customId 客户id
     * @param staffId  被转让的员工ID
     */
    void requestRevCustom(String customId, String staffId);

    /**
     * 获取客户下的开票信息
     */
    void requestCustomInvoice(String customId);

    /**
     * 请求跟进记录的标签
     */
    void requestFollowLabel();

    /**
     * 新增跟进记录
     * @param recordId
     * @param target
     * @param targetId
     * @param content
     * @param time
     */
    void createFollowRecord(String recordId, String target, String targetId, String content, String time);

    /**
     * 请求记录详情
     * @param recordsId
     */
    void requestCustomRecordDetailById(String recordsId);
}
