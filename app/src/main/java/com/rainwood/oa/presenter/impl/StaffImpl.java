package com.rainwood.oa.presenter.impl;

import com.rainwood.oa.model.domain.Staff;
import com.rainwood.oa.model.domain.StaffAccount;
import com.rainwood.oa.model.domain.StaffAccountType;
import com.rainwood.oa.model.domain.StaffDepart;
import com.rainwood.oa.model.domain.StaffExperience;
import com.rainwood.oa.model.domain.StaffPhoto;
import com.rainwood.oa.model.domain.StaffPost;
import com.rainwood.oa.model.domain.StaffSettlement;
import com.rainwood.oa.network.json.JsonParser;
import com.rainwood.oa.network.okhttp.HttpResponse;
import com.rainwood.oa.network.okhttp.OkHttp;
import com.rainwood.oa.network.okhttp.OnHttpListener;
import com.rainwood.oa.network.okhttp.RequestParams;
import com.rainwood.oa.presenter.IStaffPresenter;
import com.rainwood.oa.utils.Constants;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.oa.view.IStaffCallbacks;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/22 11:35
 * @Desc:
 */
public final class StaffImpl implements IStaffPresenter, OnHttpListener {

    private IStaffCallbacks mStaffCallbacks;

    /**
     * 请求部门职位列表
     */
    @Override
    public void requestAllDepartData() {
        RequestParams params = new RequestParams();
        OkHttp.post(Constants.BASE_URL + "cla=staff&fun=department", params, this);


        // 模拟员工部门
        List<StaffDepart> staffDepartList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            StaffDepart staffDepart = new StaffDepart();
            staffDepart.setDepart("全部乘员哈");
            if (i == 0) {
                staffDepart.setSelected(true);
            }
            List<StaffPost> postList = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                StaffPost staffPost = new StaffPost();
                staffPost.setPost("PHP研发工程师");
                if (j == 0) {
                    staffPost.setSelected(true);
                }
                postList.add(staffPost);
            }
            staffDepart.setPostList(postList);
            staffDepartList.add(staffDepart);
        }

        mStaffCallbacks.getAllDepart(staffDepartList);
    }

    @Override
    public void requestAllStaff() {
        // 模拟所有的员工
        List<Staff> staffList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Staff staff = new Staff();
            staff.setName("邵雪松");
            staff.setTelNum("15847251880");
            staff.setAllowance("1500");
            staff.setBaseSalary("2000");
            staff.setDepart("研发部");
            staff.setPost("Android工程师");
            staffList.add(staff);
        }

        mStaffCallbacks.getAllStaff(staffList);
    }

    @Override
    public void requestStaffPhoto() {
        // 模拟员工详情中的照片
        List<StaffPhoto> photoList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            StaffPhoto photo = new StaffPhoto();
            photoList.add(photo);
        }
        mStaffCallbacks.getStaffPhoto(photoList);
    }

    @Override
    public void requestExperience() {
        // 模拟工作经历
        List<StaffExperience> experienceList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            StaffExperience experience = new StaffExperience();
            experience.setCompany("重庆雨木科技有限公司");
            experience.setPost("Android研发工程师");
            experience.setEntryTime("2020.05.25");
            experience.setDepartureTime("2020.06.25");
            experience.setResponsibility("负责公司移动客户端UI界面设计，为公司新产品与新功能提供创意及设计方案，参与设计讨论，和开发团队共同创建用户界面，跟踪设计效果，提出设计优化方案...");
            experience.setReason("公司离家太远，无法照顾家庭");
            experienceList.add(experience);
        }

        mStaffCallbacks.getStaffExperience(experienceList);
    }

    private String[] accountTypes = {"全部", "收入", "支出"};

    @Override
    public void requestAccountType() {
        // 模拟员工类型
        List<StaffAccountType> typeList = new ArrayList<>();
        for (int i = 0; i < accountTypes.length; i++) {
            StaffAccountType type = new StaffAccountType();
            type.setTitle(accountTypes[i]);
            typeList.add(type);
        }
        mStaffCallbacks.getAccountTypes(typeList);
    }

    @Override
    public void requestAllAccountData() {
        // 模拟会计账户的content
        List<StaffAccount> accountList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            StaffAccount account = new StaffAccount();
            account.setTitle("行政处罚---" + i);
            account.setMoney("-520.00");
            if (i % 3 == 0) {
                account.setMoney("+1520.00");
            }
            account.setTime("2020.03.17 15:56");
            accountList.add(account);
        }
        mStaffCallbacks.getAccountData(accountList);

    }

    @Override
    public void requestAllSettlementData() {
        // 模拟所有的结算账户信息
        List<StaffSettlement> staffSettlementList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            StaffSettlement staffSettlement = new StaffSettlement();
            staffSettlement.setEvent("2019-05社保扣款");
            staffSettlement.setMoney("-389.72");
            staffSettlement.setVoucher("有凭证");
            if (i % 6 == 0) {
                staffSettlement.setVoucher("");
                staffSettlement.setMoney("+389.72");
            }
            staffSettlement.setTime("2020.03.17 15:56");
            staffSettlementList.add(staffSettlement);
        }

        mStaffCallbacks.getSettlementData(staffSettlementList);
    }

    @Override
    public void registerViewCallback(IStaffCallbacks callback) {
        mStaffCallbacks = callback;
    }

    @Override
    public void unregisterViewCallback(IStaffCallbacks callback) {
        mStaffCallbacks = null;
    }

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        LogUtils.d("sxs", "result ---- " + result.body());
        if (!(result.code() == 200)) {
            mStaffCallbacks.onError();
            return;
        }
        try {
            String warn = JsonParser.parseJSONObjectString(result.body()).getString("warn");
            if (!"success".equals(warn)) {
                mStaffCallbacks.onError(warn);
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 部门职位列表
        if (result.url().contains("cla=staff&fun=department")){

        }
    }
}
