package com.rainwood.oa.presenter.impl;

import com.rainwood.oa.model.domain.SystemLogcat;
import com.rainwood.oa.presenter.ISystemSettingPresenter;
import com.rainwood.oa.view.ISystemSettingCallbacks;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/28 9:45
 * @Desc: 系统设置逻辑类
 */
public final class SystemSettingImpl implements ISystemSettingPresenter {

    private ISystemSettingCallbacks mSettingCallback;

    @Override
    public void requestLogcatData() {
        // 模拟获取系统日志
        List<SystemLogcat> logcatList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            SystemLogcat logcat = new SystemLogcat();
            logcat.setName("扎里博客");
            logcat.setLogcat("【王海】扣除了0个员工的社保：\n" +
                    "【王海】已经从上月工资中扣除社保了;\n" +
                    "【李想】已经从上月工资中扣除社保了;\n" +
                    "【李川】已经从上月工资中扣除社保了;\n" +
                    "【韩梅梅】扣除了0个员工的社保：\n" +
                    "【谢晓霞】已经从上月工资中扣除社保了;\n" +
                    "【李雷】已经从上月工资中扣除社保了;\n" +
                    "【张晓燕】已经从上月工资中扣除社保了;\n" +
                    "【于思为】扣除了0个员工的社保：\n" +
                    "【李朋朋】已经从上月工资中扣除社保了;\n" +
                    "【刘月】已经从上月工资中扣除社保了;\n" +
                    "【李思思】已经从上月工资中扣除社保了;");
            logcat.setOrigin("客户管理-订单列表");
            logcat.setTime("2020.04.23 09:12");
            logcatList.add(logcat);
        }

        mSettingCallback.getSystemLogcat(logcatList);

    }

    @Override
    public void registerViewCallback(ISystemSettingCallbacks callback) {
        mSettingCallback = callback;
    }

    @Override
    public void unregisterViewCallback(ISystemSettingCallbacks callback) {
        mSettingCallback = null;
    }
}
