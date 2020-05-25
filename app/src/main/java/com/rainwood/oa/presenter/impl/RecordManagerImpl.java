package com.rainwood.oa.presenter.impl;

import com.rainwood.oa.presenter.IRecordManagerPresenter;
import com.rainwood.oa.view.IRecordCallbacks;

/**
 * @Author: a797s
 * @Date: 2020/5/25 19:59
 * @Desc:
 */
public final class RecordManagerImpl implements IRecordManagerPresenter {

    private IRecordCallbacks mRecordCallbacks;

    @Override
    public void requestOvertimeRecord() {
        // 模拟请求加班记录
    }

    @Override
    public void requestLeaveRecord() {
        // 模拟请假记录
    }

    @Override
    public void requestGoOutRecord() {
        // 模拟外出记录
    }

    @Override
    public void requestReissueRecord() {
        // 模拟补卡记录
    }


    @Override
    public void registerViewCallback(IRecordCallbacks callback) {
        mRecordCallbacks = callback;
    }

    @Override
    public void unregisterViewCallback(IRecordCallbacks callback) {
        mRecordCallbacks = null;
    }
}
