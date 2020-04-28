package com.rainwood.oa.utils;

import com.rainwood.oa.presenter.IManagerPresenter;
import com.rainwood.oa.presenter.IMinePresenter;
import com.rainwood.oa.presenter.impl.ManagerMainImpl;
import com.rainwood.oa.presenter.impl.MineImpl;

/**
 * @Author: a797s
 * @Date: 2020/4/28 11:56
 * @Desc: presenter管理器
 */
public final class PresenterManager {

    private static final PresenterManager ourInstance = new PresenterManager();
    private final IManagerPresenter mIManagerPresenter;
    private final IMinePresenter mIMinePresenter;

    public IMinePresenter getIMinePresenter() {
        return mIMinePresenter;
    }

    public static PresenterManager getOurInstance() {
        return ourInstance;
    }

    public IManagerPresenter getIManagerPresenter() {
        return mIManagerPresenter;
    }

    public PresenterManager() {
        mIManagerPresenter = new ManagerMainImpl();
        mIMinePresenter = new MineImpl();
    }
}
