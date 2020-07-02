package com.rainwood.oa.presenter.impl;

import com.rainwood.oa.model.domain.RolePermission;
import com.rainwood.oa.network.json.JsonParser;
import com.rainwood.oa.network.okhttp.HttpResponse;
import com.rainwood.oa.network.okhttp.OkHttp;
import com.rainwood.oa.network.okhttp.OnHttpListener;
import com.rainwood.oa.network.okhttp.RequestParams;
import com.rainwood.oa.presenter.ISearchPresenter;
import com.rainwood.oa.utils.Constants;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.oa.view.ISearchCallback;

import org.json.JSONException;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/30 15:20
 * @Desc: 搜索逻辑
 */
public final class SearchImpl implements ISearchPresenter, OnHttpListener {

    private ISearchCallback mSearchCallback;

    @Override
    public void requestSearchRoleList(String roleName, String moduleOne, String moduleTwo) {
        RequestParams params = new RequestParams();
        params.add("name", roleName);
        params.add("powerOne", moduleOne);
        params.add("powerTwo", moduleTwo);
        OkHttp.post(Constants.BASE_URL + "cla=role&fun=home&page=" + 0, params, this);

    }

    @Override
    public void registerViewCallback(ISearchCallback callback) {
        mSearchCallback = callback;
    }

    @Override
    public void unregisterViewCallback(ISearchCallback callback) {
        mSearchCallback = null;
    }

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        LogUtils.d("sxs", "result ---- " + result.body());
        if (!(result.code() == 200)) {
            mSearchCallback.onError();
            return;
        }
        try {
            String warn = JsonParser.parseJSONObjectString(result.body()).getString("warn");
            if (!"success".equals(warn)) {
                mSearchCallback.onError(warn);
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 角色列表
        if (result.url().contains("cla=role&fun=home&page")) {
            try {
                List<RolePermission> rolePermissionList = JsonParser.parseJSONArray(RolePermission.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("role"));
                mSearchCallback.getRoleManagerList(rolePermissionList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
