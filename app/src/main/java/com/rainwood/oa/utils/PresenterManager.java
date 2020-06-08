package com.rainwood.oa.utils;

import com.rainwood.oa.presenter.IAdministrativePresenter;
import com.rainwood.oa.presenter.IArticlePresenter;
import com.rainwood.oa.presenter.IAttachmentPresenter;
import com.rainwood.oa.presenter.ICommonPresenter;
import com.rainwood.oa.presenter.IContactPresenter;
import com.rainwood.oa.presenter.ICustomPresenter;
import com.rainwood.oa.presenter.IFinancialPresenter;
import com.rainwood.oa.presenter.IHomePresenter;
import com.rainwood.oa.presenter.ILogcatPresenter;
import com.rainwood.oa.presenter.ILoginAboutPresenter;
import com.rainwood.oa.presenter.IManagerPresenter;
import com.rainwood.oa.presenter.IMinePresenter;
import com.rainwood.oa.presenter.IOrderPresenter;
import com.rainwood.oa.presenter.IRecordManagerPresenter;
import com.rainwood.oa.presenter.IStaffPresenter;
import com.rainwood.oa.presenter.impl.AdministrativeImpl;
import com.rainwood.oa.presenter.impl.ArticleImpl;
import com.rainwood.oa.presenter.impl.AttachmentImpl;
import com.rainwood.oa.presenter.impl.CommonImpl;
import com.rainwood.oa.presenter.impl.ContactImpl;
import com.rainwood.oa.presenter.impl.CustomImpl;
import com.rainwood.oa.presenter.impl.FinancialImpl;
import com.rainwood.oa.presenter.impl.HomeMainImpl;
import com.rainwood.oa.presenter.impl.LogcatImpl;
import com.rainwood.oa.presenter.impl.LoginAboutImpl;
import com.rainwood.oa.presenter.impl.ManagerMainImpl;
import com.rainwood.oa.presenter.impl.MineImpl;
import com.rainwood.oa.presenter.impl.OrderImpl;
import com.rainwood.oa.presenter.impl.RecordManagerImpl;
import com.rainwood.oa.presenter.impl.StaffImpl;

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
    private final IContactPresenter mIContactPresenter;
    private final IAttachmentPresenter mAttachmentPresenter;
    private final ICustomPresenter mCustomPresenter;
    private final ILoginAboutPresenter mLoginAboutPresenter;
    private final IOrderPresenter mOrderPresenter;
    private final IAdministrativePresenter mAdministrativePresenter;
    private final IStaffPresenter mStaffPresenter;
    private final IRecordManagerPresenter mRecordManagerPresenter;
    private final ICommonPresenter mCommonPresenter;
    private final ILogcatPresenter mLogcatPresenter;
    private final IFinancialPresenter mFinancialPresenter;
    private final IArticlePresenter mArticlePresenter;

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


    public IContactPresenter getIContactPresenter() {
        return mIContactPresenter;
    }

    public IAttachmentPresenter getAttachmentPresenter() {
        return mAttachmentPresenter;
    }

    public ICustomPresenter getCustomPresenter() {
        return mCustomPresenter;
    }

    public ILoginAboutPresenter getLoginAboutPresenter() {
        return mLoginAboutPresenter;
    }

    public IOrderPresenter getOrderPresenter() {
        return mOrderPresenter;
    }

    public IStaffPresenter getStaffPresenter() {
        return mStaffPresenter;
    }

    public IAdministrativePresenter getAdministrativePresenter() {
        return mAdministrativePresenter;
    }

    public IRecordManagerPresenter getRecordManagerPresenter() {
        return mRecordManagerPresenter;
    }

    public ICommonPresenter getCommonPresenter() {
        return mCommonPresenter;
    }

    public ILogcatPresenter getLogcatPresenter() {
        return mLogcatPresenter;
    }

    public IFinancialPresenter getFinancialPresenter() {
        return mFinancialPresenter;
    }

    public IArticlePresenter getArticlePresenter() {
        return mArticlePresenter;
    }

    public PresenterManager() {
        mIManagerPresenter = new ManagerMainImpl();
        mIMinePresenter = new MineImpl();
        mIHomePresenter = new HomeMainImpl();
        mIContactPresenter = new ContactImpl();
        mAttachmentPresenter = new AttachmentImpl();
        mCustomPresenter = new CustomImpl();
        mLoginAboutPresenter = new LoginAboutImpl();
        mOrderPresenter = new OrderImpl();
        mAdministrativePresenter = new AdministrativeImpl();
        mStaffPresenter = new StaffImpl();
        mRecordManagerPresenter = new RecordManagerImpl();
        mCommonPresenter = new CommonImpl();
        mLogcatPresenter = new LogcatImpl();
        mFinancialPresenter = new FinancialImpl();
        mArticlePresenter = new ArticleImpl();
    }
}
