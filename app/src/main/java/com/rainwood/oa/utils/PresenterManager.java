package com.rainwood.oa.utils;

import com.rainwood.oa.presenter.IAttachmentPresenter;
import com.rainwood.oa.presenter.ICommonPresenter;
import com.rainwood.oa.presenter.ICommunicationPresenter;
import com.rainwood.oa.presenter.IContactPresenter;
import com.rainwood.oa.presenter.ICustomPresenter;
import com.rainwood.oa.presenter.IDepartPresenter;
import com.rainwood.oa.presenter.IHomePresenter;
import com.rainwood.oa.presenter.ILoginAboutPresenter;
import com.rainwood.oa.presenter.IManagerPresenter;
import com.rainwood.oa.presenter.IManagerSystemPresenter;
import com.rainwood.oa.presenter.IMinePresenter;
import com.rainwood.oa.presenter.IOrderPresenter;
import com.rainwood.oa.presenter.IPostPresenter;
import com.rainwood.oa.presenter.IRecordManagerPresenter;
import com.rainwood.oa.presenter.IRoleManagerPresenter;
import com.rainwood.oa.presenter.IStaffPresenter;
import com.rainwood.oa.presenter.ISystemSettingPresenter;
import com.rainwood.oa.presenter.impl.AttachmentImpl;
import com.rainwood.oa.presenter.impl.CommonImpl;
import com.rainwood.oa.presenter.impl.CommunicationImpl;
import com.rainwood.oa.presenter.impl.ContactImpl;
import com.rainwood.oa.presenter.impl.CustomImpl;
import com.rainwood.oa.presenter.impl.DepartImpl;
import com.rainwood.oa.presenter.impl.HomeMainImpl;
import com.rainwood.oa.presenter.impl.LoginAboutImpl;
import com.rainwood.oa.presenter.impl.ManagerMainImpl;
import com.rainwood.oa.presenter.impl.ManagerSystemImpl;
import com.rainwood.oa.presenter.impl.MineImpl;
import com.rainwood.oa.presenter.impl.OrderImpl;
import com.rainwood.oa.presenter.impl.PostImpl;
import com.rainwood.oa.presenter.impl.RecordManagerImpl;
import com.rainwood.oa.presenter.impl.RoleManagerImpl;
import com.rainwood.oa.presenter.impl.StaffImpl;
import com.rainwood.oa.presenter.impl.SystemSettingImpl;

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
    private final ICommunicationPresenter mSkillPresenter;
    private final IRoleManagerPresenter mRoleManagerPresenter;
    private final IDepartPresenter mDepartPresenter;
    private final IPostPresenter mPostPresenter;
    private final IStaffPresenter mStaffPresenter;
    private final IManagerSystemPresenter mSystemPresenter;
    private final IRecordManagerPresenter mRecordManagerPresenter;
    private final ICommonPresenter mCommonPresenter;
    private final ISystemSettingPresenter mSystemSettingPresenter;

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

    public ICommunicationPresenter getSkillPresenter() {
        return mSkillPresenter;
    }

    public IRoleManagerPresenter getRoleManagerPresenter() {
        return mRoleManagerPresenter;
    }

    public IDepartPresenter getDepartPresenter() {
        return mDepartPresenter;
    }

    public IPostPresenter getPostPresenter() {
        return mPostPresenter;
    }

    public IStaffPresenter getStaffPresenter() {
        return mStaffPresenter;
    }

    public IManagerSystemPresenter getSystemPresenter() {
        return mSystemPresenter;
    }

    public IRecordManagerPresenter getRecordManagerPresenter() {
        return mRecordManagerPresenter;
    }

    public ICommonPresenter getCommonPresenter() {
        return mCommonPresenter;
    }

    public ISystemSettingPresenter getSystemSettingPresenter() {
        return mSystemSettingPresenter;
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
        mSkillPresenter = new CommunicationImpl();
        mRoleManagerPresenter = new RoleManagerImpl();
        mDepartPresenter = new DepartImpl();
        mPostPresenter = new PostImpl();
        mStaffPresenter = new StaffImpl();
        mSystemPresenter = new ManagerSystemImpl();
        mRecordManagerPresenter = new RecordManagerImpl();
        mCommonPresenter = new CommonImpl();
        mSystemSettingPresenter = new SystemSettingImpl();
    }
}
