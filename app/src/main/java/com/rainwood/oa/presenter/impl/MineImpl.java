package com.rainwood.oa.presenter.impl;

import android.text.TextUtils;

import com.rainwood.contactslibrary.ContactsBean;
import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.AuditRecord;
import com.rainwood.oa.model.domain.Depart;
import com.rainwood.oa.model.domain.DepartStructure;
import com.rainwood.oa.model.domain.FontAndFont;
import com.rainwood.oa.model.domain.IconAndFont;
import com.rainwood.oa.model.domain.MineData;
import com.rainwood.oa.model.domain.MineInfo;
import com.rainwood.oa.model.domain.MineInvoice;
import com.rainwood.oa.model.domain.MineRecordTime;
import com.rainwood.oa.model.domain.MineRecords;
import com.rainwood.oa.model.domain.MineReimbursement;
import com.rainwood.oa.model.domain.SelectedItem;
import com.rainwood.oa.model.domain.TeamFunds;
import com.rainwood.oa.network.json.JsonParser;
import com.rainwood.oa.network.okhttp.HttpResponse;
import com.rainwood.oa.network.okhttp.OkHttp;
import com.rainwood.oa.network.okhttp.OnHttpListener;
import com.rainwood.oa.network.okhttp.RequestParams;
import com.rainwood.oa.presenter.IMinePresenter;
import com.rainwood.oa.utils.Constants;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.oa.view.IMineCallbacks;
import com.rainwood.tools.toast.ToastUtils;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/4/28 16:00
 * @Desc: 我的---逻辑层
 */
public class MineImpl implements IMinePresenter, OnHttpListener {

    private IMineCallbacks mMineCallbacks;

    private String[] mineManager = {"我的考勤", "我的补卡记录", "我的请假记录",
            "我的加班记录", "我的外出记录", "我的费用报销", "我的开票记录"};
    private int[] mineIcons = {R.drawable.ic_mine_attendece, R.drawable.ic_mine_reissue_card,
            R.drawable.ic_mine_ask_vacate, R.drawable.ic_mine_over_time,
            R.drawable.ic_mine_leave_out,};

    /**
     * 请求个人中心数据
     */
    @Override
    public void getMineData() {
        if (mMineCallbacks != null) {
            mMineCallbacks.onLoading();
        }
        RequestParams params = new RequestParams();
        OkHttp.post(Constants.BASE_URL + "cla=my&fun=home", params, this);
        List<IconAndFont> fontList = new ArrayList<>();
        for (String s : mineManager) {
            IconAndFont iconAndFont = new IconAndFont();
            iconAndFont.setName(s);
            iconAndFont.setLocalMipmap(R.drawable.bg_monkey_king);
            fontList.add(iconAndFont);
        }
    }

    /**
     * 我的个人资料
     */
    @Override
    public void requestMineInfo() {
        RequestParams params = new RequestParams();
        OkHttp.post(Constants.BASE_URL + "cla=my&fun=archive", params, this);
    }

    /**
     * 通讯录数据
     */
    @Override
    public void requestAddressBookData() {
        RequestParams params = new RequestParams();
        OkHttp.post(Constants.BASE_URL + "cla=my&fun=contacts", params, this);
    }

    /**
     * 部门职位
     */
    @Override
    public void requestAllDepartData() {
        RequestParams params = new RequestParams();
        OkHttp.post(Constants.BASE_URL + "cla=department&fun=home", params, this);
    }

    /**
     * 我的会计账户
     *
     * @param type 类型
     */
    @Override
    public void requestAccountingAccount(String type) {
        RequestParams params = new RequestParams();
        params.add("direction", type);
        OkHttp.post(Constants.BASE_URL + "cla=my&fun=account", params, this);
    }

    /**
     * 我的结算账户
     *
     * @param type 状态类型
     */
    @Override
    public void requestSettlementAccount(String type) {
        RequestParams params = new RequestParams();
        params.add("direction", type);
        OkHttp.post(Constants.BASE_URL + "cla=my&fun=settle", params, this);
    }

    /**
     * 我的补卡记录
     *
     * @param state     状态
     * @param startTime
     * @param endTime
     */
    @Override
    public void requestMineReissueCards(String state, String startTime, String endTime, int page) {
        RequestParams params = new RequestParams();
        params.add("workFlow", state);
        params.add("startDay", startTime);
        params.add("endDay", endTime);
        OkHttp.post(Constants.BASE_URL + "cla=my&fun=workSignAdd&page=" + page, params, this);
    }

    /**
     * 提交我的补卡申请
     */
    @Override
    public void requestMineReissueApply() {

    }

    /**
     * 审核记录
     *
     * @param recordId
     */
    @Override
    public void requestAuditRecords(String recordId) {
        RequestParams params = new RequestParams();
        params.add("id", recordId);
        OkHttp.post(Constants.BASE_URL + "cla=my&fun=workSignAddStory", params, this);
    }

    /**
     * 我的请假记录
     *
     * @param state     状态
     * @param type      类型
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param page      页码
     */
    @Override
    public void requestAskLeaveRecords(String state, String type, String startTime, String endTime, int page) {
        RequestParams params = new RequestParams();
        params.add("workFlow", state);
        params.add("type", type);
        params.add("startDay", startTime);
        params.add("endDay", endTime);
        OkHttp.post(Constants.BASE_URL + "cla=my&fun=work&page=" + page, params, this);
    }

    @Override
    public void requestMineLeaveCondition() {
        RequestParams params = new RequestParams();
        OkHttp.post(Constants.BASE_URL + "cla=work&fun=search", params, this);
    }

    /**
     * 我的加班记录
     */
    @Override
    public void requestMineOverTimeRecords() {
        RequestParams params = new RequestParams();
        OkHttp.post(Constants.BASE_URL + "cla=my&fun=workAdd", params, this);
    }

    /**
     * 我的外出记录
     */
    @Override
    public void requestMineLeaveOutRecords() {
        RequestParams params = new RequestParams();
        OkHttp.post(Constants.BASE_URL + "cla=my&fun=workOut", params, this);
    }

    /**
     * 我的费用报销
     */
    @Override
    public void requestMineReimburseData() {
        RequestParams params = new RequestParams();
        OkHttp.post(Constants.BASE_URL + "cla=my&fun=cost", params, this);
    }

    /**
     * 我的开票记录
     */
    @Override
    public void requestMineInvoiceRecords() {
        RequestParams params = new RequestParams();
        OkHttp.post(Constants.BASE_URL + "cla=my&fun=kehuInvoice", params, this);
    }

    /**
     * 请求短信验证码
     */
    @Override
    public void requestSmsVerifyCode() {
        RequestParams params = new RequestParams();
        OkHttp.post(Constants.BASE_URL + "cla=my&fun=sms", params, this);
    }

    /**
     * 验证短信验证码
     */
    @Override
    public void requestCheckedSms(String currentPwd, String newPwd, String confirmPwd, String verifyCode, String smsSecret) {
        RequestParams params = new RequestParams();
        params.add("oldPas", currentPwd);
        params.add("newPas", newPwd);
        params.add("surePas", confirmPwd);
        params.add("prove", verifyCode);
        params.add("smsid", smsSecret);
        OkHttp.post(Constants.BASE_URL + "cla=my&fun=pas", params, this);
    }

    /**
     * 退出登录
     */
    @Override
    public void requestLogout() {
        RequestParams params = new RequestParams();
        OkHttp.post(Constants.BASE_URL + "cla=my&fun=loginOut", params, this);
    }

    /**
     * 请假申请
     * @param leaveType 请假类型
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param leaveReason 请假事由
     */
    @Override
    public void createMineLeaveRecord(String id, String leaveType, String startTime, String endTime, String leaveReason) {
        RequestParams params = new RequestParams();
        params.add("id", id);
        params.add("type", leaveType);
        params.add("startTime", startTime);
        params.add("endTime", endTime);
        params.add("text", leaveReason);
        OkHttp.post(Constants.BASE_URL + "cla=my&fun=workHolidayEdit", params, this);
    }

    @Override
    public void registerViewCallback(IMineCallbacks callback) {
        this.mMineCallbacks = callback;
    }

    @Override
    public void unregisterViewCallback(IMineCallbacks callback) {
        mMineCallbacks = null;
    }

    @Override
    public void onHttpFailure(HttpResponse result) {
        mMineCallbacks.onError();
    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        LogUtils.d("sxs", "result ---- " + result.body());
        if (!(result.code() == 200)) {
            mMineCallbacks.onError();
            return;
        }
        try {
            String warn = JsonParser.parseJSONObjectString(result.body()).getString("warn");
            if (!"success".equals(warn)) {
                mMineCallbacks.onError(warn);
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // 个人中心
        if (result.url().contains("cla=my&fun=home")) {
            MineData mineData = JsonParser.parseJSONObject(MineData.class, result.body());
            List<FontAndFont> mineAccountList = new ArrayList<>();
            FontAndFont account;
            if (!TextUtils.isEmpty(mineData.getLastMoney())) {
                account = new FontAndFont();
                account.setTitle("结算账户(元)");
                account.setDesc(mineData.getLastMoney());
                mineAccountList.add(account);
            }
            if (!TextUtils.isEmpty(mineData.getTeamFund())) {
                account = new FontAndFont();
                account.setTitle("团队基金(元)");
                account.setDesc(mineData.getTeamFund());
                mineAccountList.add(account);
            }
            if (!TextUtils.isEmpty(mineData.getBasePay())) {
                account = new FontAndFont();
                account.setTitle("基本工资(元)");
                account.setDesc(mineData.getBasePay());
                mineAccountList.add(account);
            }
            if (!TextUtils.isEmpty(mineData.getSubsidy())) {
                account = new FontAndFont();
                account.setTitle("岗位津贴(元)");
                account.setDesc(mineData.getSubsidy());
                mineAccountList.add(account);
            }
            mMineCallbacks.getMenuData(mineData, mineAccountList);
        }
        // 我的资料
        else if (result.url().contains("cla=my&fun=archive")) {
            MineInfo mineInfo = JsonParser.parseJSONObject(MineInfo.class, result.body());
            mMineCallbacks.getMineInfo(mineInfo);
        }
        // 会计账户
        else if (result.url().contains("cla=my&fun=account")) {
            try {
                List<TeamFunds> accountList = JsonParser.parseJSONArray(TeamFunds.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("account"));
                String money = JsonParser.parseJSONObjectString(result.body()).getString("money");
                mMineCallbacks.getAccountListData(accountList, money);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 结算账户
        else if (result.url().contains("cla=my&fun=settle")) {
            try {
                List<TeamFunds> accountList = JsonParser.parseJSONArray(TeamFunds.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("account"));
                String money = JsonParser.parseJSONObjectString(result.body()).getString("lastMoney");
                mMineCallbacks.getAccountListData(accountList, money);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 我的补卡记录
        else if (result.url().contains("cla=my&fun=workSignAdd&page=")) {
            try {
                List<MineRecords> reissueList = JsonParser.parseJSONArray(MineRecords.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("add"));
                mMineCallbacks.getMineReissueRecords(reissueList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 补卡审核记录
        else if (result.url().equals(Constants.BASE_URL + "cla=my&fun=workSignAddStory")) {
            try {
                List<AuditRecord> auditRecordList = JsonParser.parseJSONArray(AuditRecord.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("story"));
                mMineCallbacks.getMineAuditRecords(auditRecordList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 请假记录
        else if (result.url().contains(Constants.BASE_URL + "cla=my&fun=work&page=")) {
            try {
                List<MineRecords> recordsList = JsonParser.parseJSONArray(MineRecords.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("work"));
                mMineCallbacks.getMineAskLeaveRecords(recordsList);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        // 请假记录 -- condition
        else if (result.url().contains("cla=work&fun=search")) {
            try {
                List<SelectedItem> stateList = JsonParser.parseJSONArray(SelectedItem.class,
                        JsonParser.parseJSONObjectString(JsonParser.parseJSONObjectString(
                                result.body()).getString("search")).getString("workFlow"));
                List<SelectedItem> leaveTypeList = JsonParser.parseJSONArray(SelectedItem.class,
                        JsonParser.parseJSONObjectString(JsonParser.parseJSONObjectString(
                                result.body()).getString("search")).getString("type"));
                mMineCallbacks.getMineLeaveRecords(stateList, leaveTypeList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 加班记录
        else if (result.url().equals(Constants.BASE_URL + "cla=my&fun=workAdd")) {
            try {
                List<MineRecordTime> recordsList = JsonParser.parseJSONArray(MineRecordTime.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("add"));
                mMineCallbacks.getMineOverTimeRecords(recordsList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 外出记录
        else if (result.url().equals(Constants.BASE_URL + "cla=my&fun=workOut")) {
            try {
                List<MineRecordTime> recordsList = JsonParser.parseJSONArray(MineRecordTime.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("out"));
                mMineCallbacks.getMineOverTimeRecords(recordsList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 费用报销
        else if (result.url().contains("cla=my&fun=cost")) {
            try {
                List<MineReimbursement> reimbursementList = JsonParser.parseJSONArray(MineReimbursement.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("cost"));
                mMineCallbacks.getMineReimbursementRecords(reimbursementList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 开票记录
        else if (result.url().contains("cla=my&fun=kehuInvoice")) {
            try {
                List<MineInvoice> reimbursementList = JsonParser.parseJSONArray(MineInvoice.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("invoice"));
                mMineCallbacks.getMineInvoiceRecords(reimbursementList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 获取短信验证码
        else if (result.url().contains("cla=my&fun=sms")) {
            try {
                String smsId = JsonParser.parseJSONObjectString(result.body()).getString("smsid");
                mMineCallbacks.getSmsVerifyCode(smsId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 验证短信验证码
        else if (result.url().contains("cla=my&fun=pas")) {
            try {
                String warn = JsonParser.parseJSONObjectString(result.body()).getString("warn");
                mMineCallbacks.getVerifySuccess("success".equals(warn));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 退出登录
        else if (result.url().contains("cla=my&fun=loginOut")) {
            try {
                String warn = JsonParser.parseJSONObjectString(result.body()).getString("warn");
                mMineCallbacks.getLogout("success".equals(warn));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 通讯录
        else if (result.url().contains("cla=my&fun=contacts")) {
            try {
                // 员工列表
                List<ContactsBean> contactsList = JsonParser.parseJSONArray(ContactsBean.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("staff"));
                // 部门列表
                List<DepartStructure> departStructureList = JsonParser.parseJSONArray(DepartStructure.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("department"));

                mMineCallbacks.getMineAddressBookData(contactsList);
                mMineCallbacks.getMineAddressBookDepartData(departStructureList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 部门职位列表
        else if (result.url().contains("cla=department&fun=home")) {
            try {
                List<Depart> departList = JsonParser.parseJSONArray(Depart.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("department"));
                mMineCallbacks.getDepartListData(departList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
