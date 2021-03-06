package com.rainwood.oa.presenter.impl;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.IconAndFont;
import com.rainwood.oa.model.domain.ManagerMain;
import com.rainwood.oa.network.json.JsonParser;
import com.rainwood.oa.network.okhttp.HttpResponse;
import com.rainwood.oa.network.okhttp.OkHttp;
import com.rainwood.oa.network.okhttp.OnHttpListener;
import com.rainwood.oa.network.okhttp.RequestParams;
import com.rainwood.oa.presenter.IManagerPresenter;
import com.rainwood.oa.utils.Constants;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.oa.view.IManagerCallbacks;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/4/28 11:51
 * @Desc: 管理界面逻辑层
 */
public class ManagerMainImpl implements IManagerPresenter, OnHttpListener {

    private IManagerCallbacks mCallback;
    private String[] data = {"行政人事", "财务管理", "客户管理", "知识管理", "系统设置"};
    // 行政人事
    private String[] personals = {"角色管理", "部门管理", "职位管理", "员工管理", "工作日", "通讯录", "管理制度", "加班记录",
            "请假记录", "外出记录", "考勤记录", "补卡记录", "行政处罚"};
    //财务管理
    private String[] financialManager = {"收支平衡", "费用报销", "开票记录", "团队基金"};
    // 客户管理
    private String[] customManager = {"新建客户", "介绍客户", "客户列表", "新建订单", "订单列表", "沟通技巧"};
    // 知识管理
    private String[] knowledgeManager = {"办公文件", "附件管理", "跟进记录"};
    // 系统设置
    private String[] systemSetting = {"系统日志", "帮助中心"};

    @Override
    public void getManagerData() {
        if (mCallback != null) {
            mCallback.onLoading();
        }
        // 模拟数据
        List<ManagerMain> mainList1 = new ArrayList<>();
        for (int i = 0; i < data.length; i++) {
            ManagerMain managerMain = new ManagerMain();
            managerMain.setName(data[i]);
            // 设置默认打开
            if (i == 0) {
            }
            managerMain.setHasSelected(true);
            switch (i) {
                case 0:     // 行政人事
                    List<IconAndFont> moduleItemList = new ArrayList<>();
                    for (String personal : personals) {
                        IconAndFont andFont = new IconAndFont();
                        andFont.setName(personal);
                        andFont.setLocalMipmap(R.mipmap.ic_temp_module);
                        moduleItemList.add(andFont);
                    }
                    managerMain.setArray(moduleItemList);
                    break;
                case 1:     // 财务管理
                    List<IconAndFont> financialList = new ArrayList<>();
                    for (String financial : financialManager) {
                        IconAndFont andFont = new IconAndFont();
                        andFont.setName(financial);
                        andFont.setLocalMipmap(R.mipmap.ic_temp_module);
                        financialList.add(andFont);
                    }
                    managerMain.setArray(financialList);
                    break;
                case 2:     // 客户管理
                    List<IconAndFont> customList = new ArrayList<>();
                    for (String custom : customManager) {
                        IconAndFont andFont = new IconAndFont();
                        andFont.setName(custom);
                        andFont.setLocalMipmap(R.mipmap.ic_temp_module);
                        customList.add(andFont);
                    }
                    managerMain.setArray(customList);
                    break;
                case 3:     // 知识管理
                    List<IconAndFont> knowledgeList = new ArrayList<>();
                    for (String knowledge : knowledgeManager) {
                        IconAndFont andFont = new IconAndFont();
                        andFont.setName(knowledge);
                        andFont.setLocalMipmap(R.mipmap.ic_temp_module);
                        knowledgeList.add(andFont);
                    }
                    managerMain.setArray(knowledgeList);
                    break;
                case 4:     // 系统设置
                    List<IconAndFont> systemList = new ArrayList<>();
                    for (String system : systemSetting) {
                        IconAndFont andFont = new IconAndFont();
                        andFont.setName(system);
                        andFont.setLocalMipmap(R.mipmap.ic_temp_module);
                        systemList.add(andFont);
                    }
                    managerMain.setArray(systemList);
                    break;
            }
            mainList1.add(managerMain);
        }

        RequestParams params = new RequestParams();
        params.add("life", Constants.life);
        OkHttp.post(Constants.BASE_URL + "cla=manage&fun=menu", params, this);


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
        mCallback.onError();
    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        LogUtils.d("sxs", "result ---- " + result.body());
        if (!(result.code() == 200)) {
            mCallback.onError();
            return;
        }
        try {
            String warn = JsonParser.parseJSONObjectString(result.body()).getString("warn");
            if (!"success".equals(warn)) {
                mCallback.onError(warn);
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (result.url().contains("cla=manage&fun=menu")) {
            try {
                List<ManagerMain> menuList = JsonParser.parseJSONArray(ManagerMain.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("menu"));
                mCallback.getMainManagerData(menuList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
