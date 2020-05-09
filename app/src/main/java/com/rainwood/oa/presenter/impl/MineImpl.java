package com.rainwood.oa.presenter.impl;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.IconAndFont;
import com.rainwood.oa.model.domain.TempAppMine;
import com.rainwood.oa.model.domain.TempMineAccount;
import com.rainwood.oa.network.okhttp.HttpResponse;
import com.rainwood.oa.network.okhttp.OnHttpListener;
import com.rainwood.oa.presenter.IMinePresenter;
import com.rainwood.oa.utils.AppCacheManager;
import com.rainwood.oa.view.IMineCallbacks;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/4/28 16:00
 * @Desc: 我的---逻辑层
 */
public class MineImpl implements IMinePresenter, OnHttpListener {

    private IMineCallbacks mCallback;

    private List<TempMineAccount> mAccountList;
    private List<IconAndFont> mFontList;
    private String[] accountTitle = {"结算账户(元)", "团队基金(元)", "基本工资(元)", "岗位津贴(元)"};
    private String[] accountData = {"0", "600.42", "2000", "1000"};
    private String[] mineManager = {"我的考勤", "补卡记录", "请假记录", "加班记录", "外出记录","费用报销", "开票记录"};

    @Override
    public void getMineData() {
        if (mCallback != null) {
            mCallback.onLoading();
        }
        // 模拟数据
        // 会计账户
        mAccountList = new ArrayList<>();
        for (int i = 0; i < accountTitle.length; i++) {
            TempMineAccount account = new TempMineAccount();
            account.setTitle(accountTitle[i]);
            account.setLabel(accountData[i]);
            mAccountList.add(account);
        }
        // 我的管理
        mFontList = new ArrayList<>();
        for (int i = 0; i < mineManager.length; i++) {
            IconAndFont iconAndFont = new IconAndFont();
            iconAndFont.setDesc(mineManager[i]);
            iconAndFont.setLocalMipmap(R.drawable.bg_monkey_king);
            mFontList.add(iconAndFont);
        }

         mCallback.getMenuData(mAccountList, mFontList);
    }

    @Override
    public void registerViewCallback(IMineCallbacks callback) {
        this.mCallback = callback;
    }

    @Override
    public void unregisterViewCallback(IMineCallbacks callback) {
        mCallback = null;
    }

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @Override
    public void onHttpSucceed(HttpResponse result) {

    }

}
