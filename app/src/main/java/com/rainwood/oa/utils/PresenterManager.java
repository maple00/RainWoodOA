package com.rainwood.oa.utils;

import com.rainwood.oa.presenter.IAttachmentPresenter;
import com.rainwood.oa.presenter.IContactPresenter;
import com.rainwood.oa.presenter.ICustomDetailPresenter;
import com.rainwood.oa.presenter.IHomePresenter;
import com.rainwood.oa.presenter.IManagerPresenter;
import com.rainwood.oa.presenter.IMinePresenter;
import com.rainwood.oa.presenter.impl.AttachmentImpl;
import com.rainwood.oa.presenter.impl.ContactImpl;
import com.rainwood.oa.presenter.impl.CustomDetailImpl;
import com.rainwood.oa.presenter.impl.HomeMainImpl;
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
    private final IHomePresenter mIHomePresenter;
    private final ICustomDetailPresenter mCustomDetailPresenter;
    private final IContactPresenter mIContactPresenter;
    private final IAttachmentPresenter mAttachmentPresenter;

    public IMinePresenter getIMinePresenter() {
        return mIMinePresenter;
    }

    public static PresenterManager getOurInstance() {
        return ourInstance;
    }

    public IManagerPresenter getIManagerPresenter() {
        return mIManagerPresenter;
    }

    public IHomePresenter getIHomePresenter() {
        return mIHomePresenter;
    }

    public ICustomDetailPresenter getCustomDetailPresenter() {
        return mCustomDetailPresenter;
    }

    public IContactPresenter getIContactPresenter() {
        return mIContactPresenter;
    }

    public IAttachmentPresenter getAttachmentPresenter() {
        return mAttachmentPresenter;
    }

    public PresenterManager() {
        mIManagerPresenter = new ManagerMainImpl();
        mIMinePresenter = new MineImpl();
        mIHomePresenter = new HomeMainImpl();
        mCustomDetailPresenter = new CustomDetailImpl();
        mIContactPresenter = new ContactImpl();
        mAttachmentPresenter = new AttachmentImpl();
    }
}
