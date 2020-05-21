package com.rainwood.oa.presenter.impl;

import com.rainwood.oa.model.domain.RoleDetailXPermission;
import com.rainwood.oa.model.domain.RolePermission;
import com.rainwood.oa.model.domain.SubRoleXModule;
import com.rainwood.oa.model.domain.SubRoleXPermission;
import com.rainwood.oa.presenter.IRoleManagerPresenter;
import com.rainwood.oa.view.IRoleManagerCallbacks;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/21 14:10
 * @Desc: 角色管理逻辑类
 */
public class RoleManagerImpl implements IRoleManagerPresenter {

    private IRoleManagerCallbacks mRoleManagerCallbacks;

    @Override
    public void requestAllRole() {
        // 模拟角色列表
        List<RolePermission> permissionList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            RolePermission permission = new RolePermission();
            permission.setName("超级管理员");
            permission.setDesc("该角色可查看所有权限");
            List<SubRoleXModule> modules = new ArrayList<>();
            for (int j = 0; j < 7; j++) {
                SubRoleXModule module = new SubRoleXModule();
                module.setModuleX("行政人事");
                List<SubRoleXPermission> xPermissions = new ArrayList<>();
                // 模块拥有的权限
                for (int k = 0; k < 5; k++) {
                    SubRoleXPermission xPermission = new SubRoleXPermission();
                    xPermission.setXPermission("固定资产");
                    // 权限具体内容
                    List<RoleDetailXPermission> detailXPermissions = new ArrayList<>();
                    for (int m = 0; m < 6; m++) {
                        RoleDetailXPermission detailXPermission = new RoleDetailXPermission();
                        detailXPermission.setDetailX("编辑");
                        detailXPermissions.add(detailXPermission);
                    }
                    xPermission.setDetailXPermissions(detailXPermissions);
                    xPermissions.add(xPermission);
                }
                module.setPermissions(xPermissions);
                modules.add(module);
            }
            permission.setRoleXModules(modules);
            permissionList.add(permission);
        }

        mRoleManagerCallbacks.getAllData2List(permissionList);
    }

    @Override
    public void registerViewCallback(IRoleManagerCallbacks callback) {
        mRoleManagerCallbacks = callback;
    }

    @Override
    public void unregisterViewCallback(IRoleManagerCallbacks callback) {
        mRoleManagerCallbacks = null;
    }
}
