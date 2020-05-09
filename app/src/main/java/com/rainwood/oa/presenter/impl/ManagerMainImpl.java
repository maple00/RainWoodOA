package com.rainwood.oa.presenter.impl;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.IconAndFont;
import com.rainwood.oa.model.domain.ManagerMain;
import com.rainwood.oa.network.okhttp.HttpResponse;
import com.rainwood.oa.network.okhttp.OnHttpListener;
import com.rainwood.oa.presenter.IManagerPresenter;
import com.rainwood.oa.view.IManagerCallbacks;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/4/28 11:51
 * @Desc: 管理界面逻辑层
 */
public class ManagerMainImpl implements IManagerPresenter, OnHttpListener {

    private IManagerCallbacks mCallback;
    private String[] data = {"行政人事", "财务管理", "客户管理", "系统设置"};
    // 行政人事
    private String[] personnels = {"职位管理", "员工管理", "工作日", "通讯录", "管理制度", "加班记录",
            "请假记录", "外出记录", "考勤记录", "补卡记录", "行政处罚"};

    @Override
    public void getManagerData() {
        if (mCallback != null) {
            mCallback.onLoading();
        }
        // 模拟数据
        List<ManagerMain> mainList1 = new ArrayList<>();
        for (int i = 0; i < data.length; i++) {
            ManagerMain managerMain = new ManagerMain();
            managerMain.setTitle(data[i]);
            if (i == 0){
                managerMain.setHasSelected(true);
            }
            List<IconAndFont> moduleItemList = new ArrayList<>();
            for (int j = 0; j < personnels.length; j++) {
                IconAndFont andFont = new IconAndFont();
                andFont.setDesc(personnels[j]);
                andFont.setLocalMipmap(R.mipmap.ic_temp_module);
                moduleItemList.add(andFont);
            }
            managerMain.setIconAndFontList(moduleItemList);
            mainList1.add(managerMain);
        }
        mCallback.getMainManagerData(mainList1);
    }


    @Override
    public void registerViewCallback(IManagerCallbacks callback) {
        this.mCallback = callback;
    }

    @Override
    public void unregisterViewCallback(IManagerCallbacks callback) {
        mCallback = null;
    }

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @Override
    public void onHttpSucceed(HttpResponse result) {

    }
}
