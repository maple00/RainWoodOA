package com.rainwood.oa.presenter.impl;

import com.rainwood.oa.model.domain.Depart;
import com.rainwood.oa.model.domain.ProjectGroup;
import com.rainwood.oa.presenter.IDepartPresenter;
import com.rainwood.oa.view.IDepartCallbacks;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/21 17:40
 * @Desc: 部门管理逻辑类
 */
public final class DepartImpl implements IDepartPresenter {

    private IDepartCallbacks mDepartCallbacks;

    @Override
    public void getAllDepartData() {
        // 模拟所有的部门
        List<Depart> departList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Depart depart = new Depart();
            depart.setDepart("研发" + i + "部");
            // 项目组
            List<ProjectGroup> groupList = new ArrayList<>();
            for (int j = 0; j < 7; j++) {
                ProjectGroup group = new ProjectGroup();
                group.setGroup("项目" + j + "组");
                group.setDuty("技术部项目小组负责产品部原型实现，项目经理以项目为...");
                groupList.add(group);
            }
            depart.setGroups(groupList);
            departList.add(depart);
        }

        mDepartCallbacks.getAllDepartData(departList);
    }

    @Override
    public void registerViewCallback(IDepartCallbacks callback) {
        mDepartCallbacks = callback;
    }

    @Override
    public void unregisterViewCallback(IDepartCallbacks callback) {
        mDepartCallbacks = null;
    }
}
