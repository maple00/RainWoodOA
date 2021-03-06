package com.rainwood.oa.presenter.impl;

import com.rainwood.oa.model.domain.BlockLog;
import com.rainwood.oa.network.json.JsonParser;
import com.rainwood.oa.network.okhttp.HttpResponse;
import com.rainwood.oa.network.okhttp.OkHttp;
import com.rainwood.oa.network.okhttp.OnHttpListener;
import com.rainwood.oa.network.okhttp.RequestParams;
import com.rainwood.oa.presenter.IBlockLogPresenter;
import com.rainwood.oa.utils.Constants;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.oa.view.IBlockLogCallbacks;

import org.json.JSONException;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/ 6/17 14:39
 * @Desc: 待办事项逻辑类
 */
public final class BlockLogImpl implements IBlockLogPresenter, OnHttpListener {

    private IBlockLogCallbacks mBlockLogCallbacks;
    /*
    待办事项状态
     */
    private String[] states = {"待处理", "已完成"};

    @Override
    public void requestStateData() {
        // 请求状态
        mBlockLogCallbacks.getBlockState(Arrays.asList(states));
    }

    /**
     * 待办事项详情
     *
     * @param blockId
     */
    @Override
    public void requestBlockDetail(String blockId) {
        RequestParams params = new RequestParams();
        params.add("life", Constants.life);
        params.add("id", blockId);
        OkHttp.post(Constants.BASE_URL + "cla=backlog&fun=detail", params, this);
    }

    @Override
    public void requestBlockLogList() {
        RequestParams params = new RequestParams();
        params.add("life", Constants.life);
        OkHttp.post(Constants.BASE_URL + "cla=backlog&fun=home", params, this);
    }

    @Override
    public void registerViewCallback(IBlockLogCallbacks callback) {
        mBlockLogCallbacks = callback;
    }

    @Override
    public void unregisterViewCallback(IBlockLogCallbacks callback) {
        mBlockLogCallbacks = null;
    }

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        LogUtils.d("sxs", "result ---- " + result.body());
        if (!(result.code() == 200)) {
            mBlockLogCallbacks.onError();
            return;
        }
        try {
            String warn = JsonParser.parseJSONObjectString(result.body()).getString("warn");
            if (!"success".equals(warn)) {
                mBlockLogCallbacks.onError(warn);
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (result.url().contains("cla=backlog&fun=detail")) {
            try {
                BlockLog blockLog = JsonParser.parseJSONObject(BlockLog.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("backlog"));
                mBlockLogCallbacks.getBlockLogDetail(blockLog);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 待办事项列表
        else if (result.url().contains("cla=backlog&fun=home")) {
            try {
                List<BlockLog> blockLogList = JsonParser.parseJSONArray(BlockLog.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("backlog"));
                mBlockLogCallbacks.getBlockContent(blockLogList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
