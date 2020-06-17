package com.rainwood.oa.presenter.impl;

import com.rainwood.oa.model.domain.BlockLog;
import com.rainwood.oa.presenter.IBlockLogPresenter;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.oa.view.IBlockLogCallbacks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/ 6/17 14:39
 * @Desc: 待办事项逻辑类
 */
public final class BlockLogImpl implements IBlockLogPresenter {

    private IBlockLogCallbacks mBlockLogCallbacks;

    private String[] states = {"待处理", "已完成"};

    @Override
    public void requestStateData() {
        // 请求状态
        mBlockLogCallbacks.getBlockState(Arrays.asList(states));
    }

    /**
     * 请求状态内容
     *
     * @param state 状态
     */
    @Override
    public void requestBlockData(String state) {
        LogUtils.d("sxs", "----标题--- " + state);
        List<BlockLog> blockLogList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            BlockLog blockLog = new BlockLog();
            blockLog.setTime("2020-06-17 ---" + state);
            blockLog.setContent("【韩梅梅】的外出申请需要您批准。2020-03-24【韩梅梅】的外出申请需要您批准。2020-03-24...");
            blockLogList.add(blockLog);
        }
        mBlockLogCallbacks.getBlockContent(blockLogList);
    }

    @Override
    public void registerViewCallback(IBlockLogCallbacks callback) {
        mBlockLogCallbacks = callback;
    }

    @Override
    public void unregisterViewCallback(IBlockLogCallbacks callback) {
        mBlockLogCallbacks = null;
    }
}
