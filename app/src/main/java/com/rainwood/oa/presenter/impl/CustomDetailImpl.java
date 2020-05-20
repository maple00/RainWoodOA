package com.rainwood.oa.presenter.impl;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.Associates;
import com.rainwood.oa.model.domain.Contact;
import com.rainwood.oa.model.domain.Custom;
import com.rainwood.oa.model.domain.IconAndFont;
import com.rainwood.oa.presenter.ICustomDetailPresenter;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.oa.view.ICustomDetailCallbacks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * create by a797s in 2020/5/15 10:38
 *
 * @Description : 客户详情逻辑impl
 * @Usage :
 **/
public final class CustomDetailImpl implements ICustomDetailPresenter {

    private ICustomDetailCallbacks mCallbacks;
    private String[] customModules = {"客户附件", "订单列表", "跟进记录", "外出记录", "加班记录", "汇款记录",
            "开票信息", "开票记录"};

    @Override
    public void getDetailData() {
        if (mCallbacks != null) {
            mCallbacks.onLoading();
        }
        Map<String, List> contentMap = new HashMap<>();
        List<IconAndFont> moduleItemList = new ArrayList<>();
        for (String customModule : customModules) {
            IconAndFont moduleCustom = new IconAndFont();
            moduleCustom.setDesc(customModule);
            moduleCustom.setLocalMipmap(R.drawable.bg_monkey_king);
            moduleItemList.add(moduleCustom);
        }
        List<Associates> associatesList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Associates associates = new Associates();
            associates.setHeadSrc("");
            associates.setName("邵雪松");
            associates.setDepart("技术部");
            associates.setPost("Android工程师");
            associates.setHeadSrc("https://www.baidu.com/img/bd_logo.png");
            associatesList.add(associates);
        }

        List<Contact> contactList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Contact contact = new Contact();
            contact.setName("德莱联盟");
            contact.setPost("诺克萨斯");
            contact.setTelNum("15847251880");
            contactList.add(contact);
        }

        List<Custom> customList = new ArrayList<>();
        for (int i = 0; i < 22; i++) {
            Custom custom = new Custom();
            if (i == 0){
                custom.setName("公客");
            }else if (i % 3 ==1){
                custom.setName("尹天仇");
            }else {
                custom.setName("柳飘飘");
            }
            customList.add(custom);
        }
        // 模拟模块信息
        contentMap.put("module", moduleItemList);
        // 模拟协作人
        contentMap.put("associates", associatesList);
        // 模拟联系人
        contentMap.put("contact", contactList);
        // 模拟客户列表
        contentMap.put("custom", customList);

        mCallbacks.getCustomDetailData(contentMap);
    }

    @Override
    public void getCustomDetailById(String id) {
        // TODO: 根据客户id查询客户详情

    }

    @Override
    public void registerViewCallback(ICustomDetailCallbacks callback) {
        this.mCallbacks = callback;
    }

    @Override
    public void unregisterViewCallback(ICustomDetailCallbacks callback) {
        mCallbacks = null;
    }
}
