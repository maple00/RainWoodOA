package com.rainwood.oa.view;

import com.rainwood.oa.base.IBaseCallback;
import com.rainwood.oa.model.domain.RolePermission;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/30 15:19
 * @Desc: 搜索 callback
 */
public interface ISearchCallback extends IBaseCallback {

    /**
     * 角色列表
     *
     * @param rolePermissionList
     */
    default void getRoleManagerList(List<RolePermission> rolePermissionList) {
    }
}
