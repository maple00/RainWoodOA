package com.rainwood.oa.presenter.impl;

import com.rainwood.oa.model.domain.CommunicationSkills;
import com.rainwood.oa.presenter.ICommunicationPresenter;
import com.rainwood.oa.view.ICommunicationCallbacks;
import com.rainwood.tools.utils.DateTimeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/21 11:50
 * @Desc: d沟通技巧逻辑类
 */
public final class CommunicationImpl implements ICommunicationPresenter {

    private ICommunicationCallbacks mCallbacks;

    @Override
    public void getAllData() {
        // 模拟所有的沟通技巧
        List<CommunicationSkills> mSkillList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            CommunicationSkills skills = new CommunicationSkills();
            skills.setTitle("如果文章标题像这样比较长的也要展示完换行显示");
            skills.setContent("一、说话必须简明扼要。1、当我们和客户见面时，无论是自我介绍还是介绍产品，都要简明，最好在两句话内完成，语速一定要缓慢不拖沓，说话时一定要看着对方的眼睛面..." +
                    "一、说话必须简明扼要。1、当我们和客户见面时，无论是自我介绍还是介绍产品，都要简明，最好在两句话内完成，语速一定要缓慢不拖沓，说话时一定要看着对方的眼睛面...");
            skills.setScreenNum("229");
            skills.setSort("16");
            skills.setTime(DateTimeUtils.getNowDate(DateTimeUtils.DatePattern.ONLY_MINUTE));
            mSkillList.add(skills);
        }
        mCallbacks.getAllData(mSkillList);
    }

    @Override
    public void registerViewCallback(ICommunicationCallbacks callback) {
        mCallbacks = callback;
    }

    @Override
    public void unregisterViewCallback(ICommunicationCallbacks callback) {
        mCallbacks = null;
    }
}
