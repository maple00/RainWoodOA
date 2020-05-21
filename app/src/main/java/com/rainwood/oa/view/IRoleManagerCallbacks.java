package com.rainwood.oa.view;

import com.rainwood.oa.base.IBaseCallback;
import com.rainwood.oa.model.domain.RolePermission;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/21 14:08
 * @Desc: 角色管理
 */
public interface IRoleManagerCallbacks extends IBaseCallback {

    /**
     * 角色管理列表所有数据
     */
    void getAllData2List(List<RolePermission> rolePermissions);

}
