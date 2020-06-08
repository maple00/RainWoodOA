package com.rainwood.oa.presenter.impl;

import com.rainwood.oa.model.domain.Depart;
import com.rainwood.oa.model.domain.Post;
import com.rainwood.oa.model.domain.ProjectGroup;
import com.rainwood.oa.model.domain.RolePermission;
import com.rainwood.oa.network.json.JsonParser;
import com.rainwood.oa.network.okhttp.HttpResponse;
import com.rainwood.oa.network.okhttp.OkHttp;
import com.rainwood.oa.network.okhttp.OnHttpListener;
import com.rainwood.oa.network.okhttp.RequestParams;
import com.rainwood.oa.presenter.IAdministrativePresenter;
import com.rainwood.oa.utils.Constants;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.oa.view.IAdministrativeCallbacks;

import org.json.JSONException;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/21 14:10
 * @Desc: 行政事务逻辑类----角色管理、部门管理、职位管理、工作日、通讯录
 */
public class AdministrativeImpl implements IAdministrativePresenter, OnHttpListener {

    private IAdministrativeCallbacks mAdministrativeCallbacks;

    /**
     * 角色管理列表
     */
    @Override
    public void requestAllRole() {
        RequestParams params = new RequestParams();
        OkHttp.post(Constants.BASE_URL + "cla=role&fun=home", params, this);
    }

    /**
     * 部门管理列表
     */
    @Override
    public void requestAllDepartData() {
        RequestParams params = new RequestParams();
        OkHttp.post(Constants.BASE_URL + "cla=department&fun=home", params, this);
    }

    /**
     * 查询部门详情
     * @param departId
     */
    @Override
    public void requestDepartDetailById(String departId) {
        RequestParams params = new RequestParams();
        params.add("id", departId);
        OkHttp.post(Constants.BASE_URL + "cla=department&fun=detail", params, this);
    }

    /**
     * 获取职位列表
     */
    @Override
    public void requestPostListData() {
        RequestParams params = new RequestParams();
        OkHttp.post(Constants.BASE_URL + "cla=job&fun=home", params, this);
    }

    /**
     * 通过id查询职位详情
     * @param postId
     */
    @Override
    public void requestPostDetailById(String postId) {
        RequestParams params = new RequestParams();
        params.add("id", postId);
        OkHttp.post(Constants.BASE_URL + "cla=job&fun=detail", params, this);
    }

    @Override
    public void registerViewCallback(IAdministrativeCallbacks callback) {
        mAdministrativeCallbacks = callback;
    }

    @Override
    public void unregisterViewCallback(IAdministrativeCallbacks callback) {
        mAdministrativeCallbacks = null;
    }

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        LogUtils.d("sxs", "result ---- " + result.body());
        if (!(result.code() == 200)) {
            mAdministrativeCallbacks.onError();
            return;
        }
        try {
            String warn = JsonParser.parseJSONObjectString(result.body()).getString("warn");
            if (!"success".equals(warn)) {
                mAdministrativeCallbacks.onError(warn);
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 角色列表
        if (result.url().contains("cla=role&fun=home")) {
            try {
                List<RolePermission> rolePermissionList = JsonParser.parseJSONArray(RolePermission.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("role"));
                 mAdministrativeCallbacks.getAllData2List(rolePermissionList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 部门列表
        else if (result.url().contains("cla=department&fun=home")){
            try {
                List<Depart> departList = JsonParser.parseJSONArray(Depart.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("department"));
                mAdministrativeCallbacks.getDepartListData(departList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 部门详情
        else if (result.url().contains("cla=department&fun=detail")){
            try {
                ProjectGroup depart = JsonParser.parseJSONObject(ProjectGroup.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("department"));
                mAdministrativeCallbacks.getDepartDetailData(depart);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 职位列表
        else if (result.url().contains("cla=job&fun=home")){
            try {
                List<Post> postList = JsonParser.parseJSONArray(Post.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("job"));
                mAdministrativeCallbacks.getPostListData(postList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 职位详情
        else if (result.url().contains("cla=job&fun=detail")){
            try {
                Post post = JsonParser.parseJSONObject(Post.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("job"));
                mAdministrativeCallbacks.getPostDetailData(post);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
