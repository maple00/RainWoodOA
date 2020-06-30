package com.rainwood.oa.presenter.impl;

import android.text.TextUtils;

import com.rainwood.oa.model.domain.SelectedItem;
import com.rainwood.oa.model.domain.Staff;
import com.rainwood.oa.model.domain.StaffAccount;
import com.rainwood.oa.model.domain.StaffAccountType;
import com.rainwood.oa.model.domain.StaffDepart;
import com.rainwood.oa.model.domain.StaffDetail;
import com.rainwood.oa.model.domain.StaffExperience;
import com.rainwood.oa.model.domain.StaffPhoto;
import com.rainwood.oa.model.domain.StaffSettlement;
import com.rainwood.oa.network.json.JsonParser;
import com.rainwood.oa.network.okhttp.HttpResponse;
import com.rainwood.oa.network.okhttp.OkHttp;
import com.rainwood.oa.network.okhttp.OnHttpListener;
import com.rainwood.oa.network.okhttp.RequestParams;
import com.rainwood.oa.presenter.IStaffPresenter;
import com.rainwood.oa.utils.Constants;
import com.rainwood.oa.utils.ListUtils;
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
    }

    /**
     * 请求查询条件
     */
    @Override
    public void requestQueryCondition() {
        RequestParams params = new RequestParams();
        OkHttp.post(Constants.BASE_URL + "cla=staff&fun=search", params, this);
    }

    /**
     * 通过职位id查询该职位下的员工
     *
     * @param postId
     */
    @Override
    public void requestAllStaff(String postId, String name, String sex, String social, String gateKey,
                                String orderBy) {
        RequestParams params = new RequestParams();
        params.add("departmentId", postId);
        params.add("name", name);
        params.add("sex", sex);
        params.add("socialSecurity", social);
        params.add("gateKey", gateKey);
        params.add("orderBy", orderBy);
        OkHttp.post(Constants.BASE_URL + "cla=staff&fun=home", params, this);
    }

    /**
     * 员工详情数据
     *
     * @param staffId
     */
    @Override
    public void requestStaffData(String staffId) {
        RequestParams params = new RequestParams();
        params.add("id", staffId);
        OkHttp.post(Constants.BASE_URL + "cla=staff&fun=detail", params, this);
    }

    @Override
    public void requestExperienceById(String experienceId) {
        // 模拟工作经历
        List<StaffExperience> experienceList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            StaffExperience experience = new StaffExperience();
            experience.setCompanyName("重庆雨木科技有限公司");
            experience.setPosition("Android研发工程师");
            experience.setEntryTime("2020.05.25");
            experience.setDepartureTime("2020.06.25");
            experience.setContent("负责公司移动客户端UI界面设计，为公司新产品与新功能提供创意及设计方案，参与设计讨论，和开发团队共同创建用户界面，跟踪设计效果，提出设计优化方案...");
            experience.setCause("公司离家太远，无法照顾家庭");
            experienceList.add(experience);
        }
        mStaffCallbacks.getStaffExperience(experienceList);
    }

    private String[] accountTypes = {"全部", "收入", "支出"};

    @Override
    public void requestAccountType() {
        // 模拟员工类型
        List<StaffAccountType> typeList = new ArrayList<>();
        for (String accountType : accountTypes) {
            StaffAccountType type = new StaffAccountType();
            type.setTitle(accountType);
            typeList.add(type);
        }
        mStaffCallbacks.getAccountTypes(typeList);
    }

    /**
     * 员工会计账户
     *
     * @param type
     */
    @Override
    public void requestAllAccountData(String type) {
        RequestParams params = new RequestParams();
        OkHttp.post(Constants.BASE_URL + "cla=staff&fun=" + type, params, this);
    }

    /**
     * 员工结算账户
     *
     * @param type
     */
    @Override
    public void requestAllSettlementData(String type) {
        RequestParams params = new RequestParams();
        OkHttp.post(Constants.BASE_URL + "cla=staff&fun=" + type, params, this);
    }

    /**
     * 员工账户详情
     *
     * @param accountId
     */
    @Override
    public void requestStaffAccountDetailById(String accountId) {
        RequestParams params = new RequestParams();
        params.add("id", accountId);
        OkHttp.post(Constants.BASE_URL + "cla=staff&fun=accountDetail", params, this);
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
        if (result.url().contains("cla=staff&fun=department")) {
            try {
                List<StaffDepart> departmentList = JsonParser.parseJSONArray(StaffDepart.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("department"));
                if (ListUtils.getSize(departmentList) != 0) {
                    // 设置默认选中的值--目前是只有通过
                    for (int i = 0; i < departmentList.size(); ) {
                        departmentList.get(i).setSelected(true);
                        for (int j = 0; j < departmentList.get(i).getArray().size(); ) {
                            departmentList.get(i).getArray().get(j).setSelected(true);
                            break;
                        }
                        break;
                    }
                }
                mStaffCallbacks.getAllDepart(departmentList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 员工查询条件
        else if (result.url().contains("cla=staff&fun=search")) {
            try {
                // 默认排序
                List<SelectedItem> defaultSortList = JsonParser.parseJSONArray(SelectedItem.class,
                        JsonParser.parseJSONObjectString(JsonParser.parseJSONObjectString(
                                result.body()).getString("search")).getString("list"));
                // 性别
                List<SelectedItem> sexList = JsonParser.parseJSONArray(SelectedItem.class,
                        JsonParser.parseJSONObjectString(JsonParser.parseJSONObjectString(
                                result.body()).getString("search")).getString("sex"));
                // 是否购买社保
                List<SelectedItem> socialList = JsonParser.parseJSONArray(SelectedItem.class,
                        JsonParser.parseJSONObjectString(JsonParser.parseJSONObjectString(
                                result.body()).getString("search")).getString("socialSecurity"));
                // 是否有大门钥匙
                List<SelectedItem> gateList = JsonParser.parseJSONArray(SelectedItem.class,
                        JsonParser.parseJSONObjectString(JsonParser.parseJSONObjectString(
                                result.body()).getString("search")).getString("gateKey"));
                mStaffCallbacks.getQueryCondition(defaultSortList, sexList, socialList, gateList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 职位下的员工
        else if (result.url().contains("cla=staff&fun=home")) {
            try {
                List<Staff> staffList = JsonParser.parseJSONArray(Staff.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("staff"));
                mStaffCallbacks.getAllStaff(staffList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 员工资料详情
        else if (result.url().contains("cla=staff&fun=detail")) {
            try {
                StaffDetail staffDetail = JsonParser.parseJSONObject(StaffDetail.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("staff"));
                // 数据处理
                List<StaffPhoto> staffPhotoList = new ArrayList<>();
                StaffPhoto photo;
                if (!TextUtils.isEmpty(staffDetail.getIco())) {
                    photo = new StaffPhoto();
                    photo.setOrigin(staffDetail.getIco());
                    photo.setDesc("头像");
                    staffPhotoList.add(photo);
                }
                if (!TextUtils.isEmpty(staffDetail.getIDCardFront())) {
                    photo = new StaffPhoto();
                    photo.setOrigin(staffDetail.getIDCardFront());
                    photo.setDesc("身份证正面");
                    staffPhotoList.add(photo);
                }
                if (!TextUtils.isEmpty(staffDetail.getIDCardBack())) {
                    photo = new StaffPhoto();
                    photo.setOrigin(staffDetail.getIDCardBack());
                    photo.setDesc("身份证背面");
                    staffPhotoList.add(photo);
                }
                if (!TextUtils.isEmpty(staffDetail.getDiploma())) {
                    photo = new StaffPhoto();
                    photo.setOrigin(staffDetail.getDiploma());
                    photo.setDesc("毕业证扫描件");
                    staffPhotoList.add(photo);
                }
                if (!TextUtils.isEmpty(staffDetail.getBankIco())) {
                    photo = new StaffPhoto();
                    photo.setOrigin(staffDetail.getBankIco());
                    photo.setDesc("银行卡扫描件");
                    staffPhotoList.add(photo);
                }
                mStaffCallbacks.getStaffPhoto(staffPhotoList);
                mStaffCallbacks.getStaffDetailData(staffDetail);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 员工会计账户--全部、收入、支出
        else if (result.url().contains("cla=staff&fun=accountAll") || result.url().contains("cla=staff&fun=accountIn")
                || result.url().contains("cla=staff&fun=accountOut")) {
            try {
                List<StaffAccount> accountList = JsonParser.parseJSONArray(StaffAccount.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("account"));
                mStaffCallbacks.getAccountData(accountList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 员工结算账户-- 全部、收入、支出
        else if (result.url().contains("cla=staff&fun=settleAccountAll") || result.url().contains("cla=staff&fun=settleAccountIn")
                || result.url().contains("cla=staff&fun=settleAccountOut")) {
            try {
                List<StaffSettlement> accountList = JsonParser.parseJSONArray(StaffSettlement.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("account"));
                mStaffCallbacks.getSettlementData(accountList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 员工账户详情
        else if (result.url().contains("cla=staff&fun=accountDetail")) {
            try {
                StaffAccount accountDetail = JsonParser.parseJSONObject(StaffAccount.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("detail"));
                mStaffCallbacks.getStaffAccountDetail(accountDetail);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
