package com.rainwood.oa.presenter.impl;

import com.rainwood.oa.model.domain.ManagerSystem;
import com.rainwood.oa.presenter.IManagerSystemPresenter;
import com.rainwood.oa.view.IManagerSystemCallbacks;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/25 19:04
 * @Desc: 管理制度逻辑类
 */
public class ManagerSystemImpl implements IManagerSystemPresenter {

    private IManagerSystemCallbacks mSystemCallbacks;

    @Override
    public void requestAllData() {
        // 模拟所有的管理制度
        List<ManagerSystem> systemList = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            ManagerSystem system = new ManagerSystem();
            system.setTitle("第"+i+"章：岗位职责");
            system.setContent("第一条、产品经理。1、通过公客池、招聘网站、猪八戒网、百度360搜狗搜索引擎客户咨询下发、同行业等渠道获取商...");
            systemList.add(system);
        }
        mSystemCallbacks.getAllData(systemList);
    }

    @Override
    public void registerViewCallback(IManagerSystemCallbacks callback) {
        mSystemCallbacks = callback;
    }

    @Override
    public void unregisterViewCallback(IManagerSystemCallbacks callback) {
        mSystemCallbacks = null;
    }
}
