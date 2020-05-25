package com.rainwood.oa.presenter;

import com.rainwood.oa.base.IBasePresenter;
import com.rainwood.oa.view.IRecordCallbacks;

/**
 * @Author: a797s
 * @Date: 2020/5/25 19:58
 * @Desc: 记录管理
 */
public interface IRecordManagerPresenter extends IBasePresenter<IRecordCallbacks> {

    /**
     * 请求加班记录
     */
    void requestOvertimeRecord();

    /**
     * 请求请假记录
     */
    void requestLeaveRecord();

    /**
     * 请求外出记录
     */
    void requestGoOutRecord();

    /**
     * 请求补卡记录
     */
    void requestReissueRecord();
}
