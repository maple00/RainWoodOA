package com.rainwood.oa.presenter;

import com.rainwood.oa.base.IBasePresenter;
import com.rainwood.oa.view.ISystemSettingCallbacks;

/**
 * @Author: a797s
 * @Date: 2020/5/28 9:44
 * @Desc: 系统设置
 */
public interface ISystemSettingPresenter extends IBasePresenter<ISystemSettingCallbacks> {

    /**
     * 请求获取系统日志
     */
    void requestLogcatData();
}
