package com.rainwood.oa.presenter.impl;

import com.rainwood.oa.model.domain.BlockLog;
import com.rainwood.oa.network.json.JsonParser;
import com.rainwood.oa.network.okhttp.HttpResponse;
import com.rainwood.oa.network.okhttp.OkHttp;
import com.rainwood.oa.network.okhttp.OnHttpListener;
import com.rainwood.oa.network.okhttp.RequestParams;
import com.rainwood.oa.presenter.IBlockLogPagerPresenter;
import com.rainwood.oa.utils.Constants;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.oa.view.IBlockLogPagerCallbacks;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/19 10:18
 * @Desc:
 */
public final class BlockLogPagerImpl implements IBlockLogPagerPresenter {

    private List<IBlockLogPagerCallbacks> mIBlockLogPagerCallbacks = new ArrayList<>();

    /**
     * 请求状态内容
     *
     * @param state 状态
     */
    @Override
    public void requestBlockData(String state) {
        for (IBlockLogPagerCallbacks blockLogCallback : mIBlockLogPagerCallbacks) {
            if (state.equals(blockLogCallback.getState())) {
                blockLogCallback.onLoading();
            }
        }
        // request
        RequestParams params = new RequestParams();
        params.add("workFlow", state);
        OkHttp.post(Constants.BASE_URL + "cla=backlog&fun=home", params, new OnHttpListener() {
            @Override
            public void onHttpFailure(HttpResponse result) {
                for (IBlockLogPagerCallbacks blockLogCallback : mIBlockLogPagerCallbacks) {
                    if (result == null) {
                        blockLogCallback.onError();
                    }
                }
            }

            @Override
            public void onHttpSucceed(HttpResponse result) {
                LogUtils.d("sxs", "----result---- " + result.body());
                for (IBlockLogPagerCallbacks blockLogCallback : mIBlockLogPagerCallbacks) {
                    if (!(result.code() == 200)) {
                        blockLogCallback.onError();
                        return;
                    }
                }
                try {
                    String warn = JsonParser.parseJSONObjectString(result.body()).getString("warn");
                    for (IBlockLogPagerCallbacks blockLogCallback : mIBlockLogPagerCallbacks) {
                        if (!"success".equals(warn)) {
                            blockLogCallback.onError(warn);
                            break;
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
                        for (IBlockLogPagerCallbacks blockLogCallback : mIBlockLogPagerCallbacks) {
                            if (state.equals(blockLogCallback.getState())) {
                                if (ListUtils.getSize(blockLogList) == 0) {
                                    blockLogCallback.onEmpty();
                                } else {
                                    blockLogCallback.getBlockContent(blockLogList);
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void registerViewCallback(IBlockLogPagerCallbacks callback) {
        if (!mIBlockLogPagerCallbacks.contains(callback)) {
            mIBlockLogPagerCallbacks.add(callback);
        }
    }

    @Override
    public void unregisterViewCallback(IBlockLogPagerCallbacks callback) {
        mIBlockLogPagerCallbacks.remove(callback);
    }
}
