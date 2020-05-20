package com.rainwood.oa.presenter.impl;

import com.rainwood.oa.model.domain.Custom;
import com.rainwood.oa.model.domain.SelectedItem;
import com.rainwood.oa.presenter.ICustomListPresenter;
import com.rainwood.oa.view.ICustomListCallbacks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Author: a797s
 * @Date: 2020/5/18 14:49
 * @Desc: 客户列表逻辑类
 */
public class CustomListImpl implements ICustomListPresenter {

    private ICustomListCallbacks mCustomListCallback;

    @Override
    public void getALlCustomData() {
        // 模拟客户列表
        List<Custom> customList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Custom custom = new Custom();
            custom.setId(UUID.randomUUID() + "");
            custom.setName("韩梅梅");
            custom.setCompany("重庆佰仕合达企业佰仕合达企业营销策划有限公司");
            custom.setOrigin("官网");
            custom.setStatus("优质");
            custom.setTrade("汽车制造业");
            custom.setBudget(40000);
            custom.setDemand("需要一套制造业ERP软件");
            customList.add(custom);
        }

        Map<String, List<Custom>> customValues = new HashMap<>();
        customValues.put("customList", customList);
        mCustomListCallback.getAllCustomList(customValues);
    }

    @Override
    public void getStatus() {
        // 模拟所有的状态
        List<SelectedItem> selectedList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            SelectedItem item = new SelectedItem();
            item.setHasSelected(false);
            switch (i) {
                case 0:
                    item.setData("全部");
                    break;
                case 1:
                    item.setData("已签约");
                    break;
                case 2:
                    item.setData("优质");
                    break;
                case 3:
                    item.setData("一般");
                    break;
                case 4:
                    item.setData("很差");
                    break;
                case 5:
                    item.setData("待联系");
                    break;
                case 6:
                    item.setData("已放弃");
                    break;
            }
            selectedList.add(item);
        }

        Map<String, List<SelectedItem>> selectedMap = new HashMap<>();
        selectedMap.put("selectedItem", selectedList);

        mCustomListCallback.getALlStatus(selectedMap);
    }

    @Override
    public void registerViewCallback(ICustomListCallbacks callback) {
        this.mCustomListCallback = callback;
    }

    @Override
    public void unregisterViewCallback(ICustomListCallbacks callback) {
        mCustomListCallback = null;
    }
}
