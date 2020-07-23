package com.rainwood.oa.presenter;

import com.rainwood.oa.base.IBasePresenter;
import com.rainwood.oa.view.IStaffCallbacks;

/**
 * @Author: a797s
 * @Date: 2020/5/22 11:34
 * @Desc: 员工管理
 */
public interface IStaffPresenter extends IBasePresenter<IStaffCallbacks> {

    /**
     * 请求所有的部门信息
     */
    void requestAllDepartData();

    /**
     * 请求查询条件
     */
    void requestQueryCondition();

    /**
     * 查询员工
     *
     * @param postId  职位id
     * @param keyWord 搜索关键字
     * @param sex     性别
     * @param social  是否购买社保
     * @param gateKey 是否有大门钥匙
     * @param orderBy 排序方式
     * @param page
     */
    void requestAllStaff(String postId, String keyWord, String sex, String social, String gateKey,
                         String orderBy, int page);

    /**
     * 请求员工资料data
     */
    void requestStaffData(String staffId);

    /**
     * 请求员工的所有的工作经历
     */
    void requestExperienceById(String experienceId);

    /**
     * 请求员工账户类型
     */
    void requestAccountType();

    /**
     * 获取员工的所有的会计账户
     */
    void requestAllAccountData(String type, String keyWord, String startTime, String endTime, int pageCount);

    /**
     * 获取员工的所有的计算账户的信息
     */
    void requestAllSettlementData(String type, String keyWord, String startTime, String endTime, int pageCount);

    /**
     * 获取员工账户的详情
     */
    void requestStaffAccountDetailById(String accountId);
}
