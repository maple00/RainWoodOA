package com.rainwood.oa.presenter.impl;

import com.rainwood.oa.model.domain.Staff;
import com.rainwood.oa.model.domain.StaffDepart;
import com.rainwood.oa.model.domain.StaffPost;
import com.rainwood.oa.presenter.IStaffPresenter;
import com.rainwood.oa.view.IStaffCallbacks;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/22 11:35
 * @Desc:
 */
public final class StaffImpl implements IStaffPresenter {

    private IStaffCallbacks mStaffCallbacks;

    @Override
    public void requestAllDepartData() {
        // 模拟员工部门
        List<StaffDepart> staffDepartList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            StaffDepart staffDepart = new StaffDepart();
            staffDepart.setDepart("全部乘员哈");
            if (i == 0) {
                staffDepart.setSelected(true);
            }
            List<StaffPost> postList = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                StaffPost staffPost = new StaffPost();
                staffPost.setPost("PHP研发工程师");
                if (j == 0) {
                    staffPost.setSelected(true);
                }
                postList.add(staffPost);
            }
            staffDepart.setPostList(postList);
            staffDepartList.add(staffDepart);
        }

        mStaffCallbacks.getAllDepart(staffDepartList);
    }

    @Override
    public void requestAllStaff() {
        // 模拟所有的员工
        List<Staff> staffList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Staff staff = new Staff();
            staff.setName("邵雪松");
            staff.setTelNum("15847251880");
            staff.setAllowance("1500");
            staff.setBaseSalary("2000");
            staff.setDepart("研发部");
            staff.setPost("Android工程师");
            staffList.add(staff);
        }

        mStaffCallbacks.getAllStaff(staffList);
    }

    @Override
    public void registerViewCallback(IStaffCallbacks callback) {
        mStaffCallbacks = callback;
    }

    @Override
    public void unregisterViewCallback(IStaffCallbacks callback) {
        mStaffCallbacks = null;
    }
}
