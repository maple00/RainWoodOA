package com.rainwood.oa.view;

import com.rainwood.oa.base.IBaseCallback;
import com.rainwood.oa.model.domain.Staff;
import com.rainwood.oa.model.domain.StaffDepart;

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
    void getAllDepart(List<StaffDepart> departList);


    /**
     * 获取所有的员工
     */
    void getAllStaff(List<Staff> staffList);
}
