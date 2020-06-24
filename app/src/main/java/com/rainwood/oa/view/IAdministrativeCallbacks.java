package com.rainwood.oa.view;

import com.rainwood.oa.base.IBaseCallback;
import com.rainwood.oa.model.domain.Depart;
import com.rainwood.oa.model.domain.Post;
import com.rainwood.oa.model.domain.ProjectGroup;
import com.rainwood.oa.model.domain.RoleCondition;
import com.rainwood.oa.model.domain.RolePermission;
import com.rainwood.oa.model.domain.SelectedItem;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/8 13:41
 * @Desc: 行政事务----角色管理、部门管理、职位管理、工作日、通讯录
 */
public interface IAdministrativeCallbacks extends IBaseCallback {

    /**
     * 角色管理列表所有数据
     */
    default void getAllData2List(List<RolePermission> rolePermissions) {
    }

    /**
     * 角色详情
     */
    default void getRoleXPermissionDetail(RolePermission rolePermission) {
    }

    /**
     * 角色筛选分类
     */
    default void getRoleScreenData(List<RoleCondition> roleConditionList) {
    }

    /**
     * 部门管理列表data
     */
    default void getDepartListData(List<Depart> departList) {
    }

    /**
     * 部门分类
     */
    default void getDepartTypeData(List<SelectedItem> departTypeList) {
    }

    /**
     * 部门详情
     *
     * @param depart
     */
    default void getDepartDetailData(ProjectGroup depart) {
    }


    /**
     * 职位列表
     *
     * @param postList
     */
    default void getPostListData(List<Post> postList) {
    }

    /**
     * 职位管理--- 角色信息
     */
    default void getPostRoleData(List<SelectedItem> selectedList) {
    }

    /**
     * 职位详情
     *
     * @param post
     */
    default void getPostDetailData(Post post) {
    }
}
