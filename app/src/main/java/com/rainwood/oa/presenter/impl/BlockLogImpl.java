package com.rainwood.oa.presenter.impl;

import com.rainwood.oa.model.domain.BlockLog;
import com.rainwood.oa.network.json.JsonParser;
import com.rainwood.oa.network.okhttp.HttpResponse;
import com.rainwood.oa.network.okhttp.OkHttp;
import com.rainwood.oa.network.okhttp.OnHttpListener;
import com.rainwood.oa.network.okhttp.RequestParams;
import com.rainwood.oa.presenter.IBlockLogPresenter;
import com.rainwood.oa.utils.Constants;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.oa.view.IBlockLogCallbacks;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/ 6/17 14:39
 * @Desc: 待办事项逻辑类
 */
public final class BlockLogImpl implements IBlockLogPresenter, OnHttpListener {

    private List<IBlockLogCallbacks> mBlockLogCallbacks = new ArrayList<>();

    /*
    待办事项状态
     */
    private String[] states = {"待处理", "已完成"};

    @Override
    public void requestStateData() {
        // 请求状态
        for (IBlockLogCallbacks blockLogCallback : mBlockLogCallbacks) {
            blockLogCallback.getBlockState(Arrays.asList(states));
        }
    }

    /**
     * 请求状态内容
     *
     * @param state 状态
     */
    @Override
    public void requestBlockData(String state) {
        LogUtils.d("sxs", "----标题--- " + state);
        for (IBlockLogCallbacks blockLogCallback : mBlockLogCallbacks) {
            if (state.equals(blockLogCallback.getState())) {
                LogUtils.d("sxs", "--状态-- " + blockLogCallback.getState());
                blockLogCallback.onLoading();
            }
        }
        RequestParams params = new RequestParams();
        params.add("workFlow", state);
        OkHttp.post(Constants.BASE_URL + "cla=backlog&fun=home", params, this);
    }

    @Override
    public void registerViewCallback(IBlockLogCallbacks callback) {
        if (!mBlockLogCallbacks.contains(callback)) {
            mBlockLogCallbacks.add(callback);
        }
    }

    @Override
    public void unregisterViewCallback(IBlockLogCallbacks callback) {
        mBlockLogCallbacks.remove(callback);
    }

    @Override
    public void onHttpFailure(HttpResponse result) {
        for (IBlockLogCallbacks blockLogCallback : mBlockLogCallbacks) {
            if (result == null) {
                blockLogCallback.onError();
            }
        }
    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
       // LogUtils.d("sxs", "result ---- " + result.body());
        for (IBlockLogCallbacks blockLogCallback : mBlockLogCallbacks) {
            if (!(result.code() == 200)) {
                blockLogCallback.onError();
                break;
            }
        }
        try {
            String warn = JsonParser.parseJSONObjectString(result.body()).getString("warn");
            for (IBlockLogCallbacks blockLogCallback : mBlockLogCallbacks) {
                if (!"success".equals(warn)) {
                    blockLogCallback.onError(warn);
                    return;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 待办事项列表
        if (result.url().contains("cla=backlog&fun=home")) {
            try {
                List<BlockLog> blockLogList = JsonParser.parseJSONArray(BlockLog.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("backlog"));
                for (IBlockLogCallbacks blockLogCallback : mBlockLogCallbacks) {
                    if (ListUtils.getSize(blockLogList) == 0) {
                        blockLogCallback.onEmpty();
                    } else {
                        blockLogCallback.getBlockContent(blockLogList);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
