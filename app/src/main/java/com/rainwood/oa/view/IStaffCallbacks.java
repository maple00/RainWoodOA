package com.rainwood.oa.view;

import com.rainwood.oa.base.IBaseCallback;
import com.rainwood.oa.model.domain.Staff;
import com.rainwood.oa.model.domain.StaffAccount;
import com.rainwood.oa.model.domain.StaffAccountType;
import com.rainwood.oa.model.domain.StaffDepart;
import com.rainwood.oa.model.domain.StaffExperience;
import com.rainwood.oa.model.domain.StaffPhoto;
import com.rainwood.oa.model.domain.StaffSettlement;
import com.rainwood.oa.utils.ListUtils;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/22 11:32
 * @Desc: 员工管理
 */
public interface IStaffCallbacks extends IBaseCallback {

    /**
     * 获取所有的部门
     */
    default void getAllDepart(List<StaffDepart> departList){};


    /**
     * 获取所有的员工
     */
    default void getAllStaff(List<Staff> staffList){};

    /**
     * 获取员工详情的所有的照片
     */
    default void getStaffPhoto(List<StaffPhoto> photoList){};

    /**
     * 获取员工详情的工作经历
     * @param experienceList
     */
    default void getStaffExperience(List<StaffExperience> experienceList){};

    /**
     * 员工账户类型
     * @param accountTypes
     */
    default void getAccountTypes(List<StaffAccountType> accountTypes){};

    /**
     * 员工会计账户content
     * @param accountList
     */
    default void getAccountData(List<StaffAccount> accountList){};


    default void getSettlementData(List<StaffSettlement> settlementList){};
}
