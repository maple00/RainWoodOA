package com.rainwood.oa.presenter.impl;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.Contact;
import com.rainwood.oa.model.domain.Custom;
import com.rainwood.oa.model.domain.CustomDetail;
import com.rainwood.oa.model.domain.CustomInvoice;
import com.rainwood.oa.model.domain.CustomStaff;
import com.rainwood.oa.model.domain.IconAndFont;
import com.rainwood.oa.model.domain.SelectedItem;
import com.rainwood.oa.network.json.JsonParser;
import com.rainwood.oa.network.okhttp.HttpResponse;
import com.rainwood.oa.network.okhttp.OkHttp;
import com.rainwood.oa.network.okhttp.OnHttpListener;
import com.rainwood.oa.network.okhttp.RequestParams;
import com.rainwood.oa.presenter.ICustomPresenter;
import com.rainwood.oa.utils.Constants;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.oa.utils.RandomUtil;
import com.rainwood.oa.view.ICustomCallbacks;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/5/18 14:49
 * @Desc: 客户逻辑类
 */
public class CustomImpl implements ICustomPresenter, OnHttpListener {

    private ICustomCallbacks mCustomCallback;

    /**
     * 请求跟进状态
     */
    @Override
    public void requestFollowStatus() {
        RequestParams params = new RequestParams();
        params.add("deviceID", Constants.IMEI);
        OkHttp.post(Constants.BASE_URL + "cla=client&fun=clientWorkFlow", params, this);
    }

    /**
     * 请求客户来源
     */
    @Override
    public void requestCustomOrigin() {
        RequestParams params = new RequestParams();
        params.add("deviceID", Constants.IMEI);
        OkHttp.post(Constants.BASE_URL + "cla=client&fun=clientSource", params, this);
    }

    /**
     * 创建客户
     *
     * @param userName
     * @param tel
     * @param wxNum
     * @param qqNum
     * @param post
     * @param followStatus
     * @param origin
     * @param note
     * @param preMoney
     * @param industry
     * @param demand
     * @param company
     * @param taxNum
     * @param emailAddress
     * @param invoiceAddress
     * @param landLine
     * @param bankName
     * @param bankNo
     */
    @Override
    public void createCustomData(String userName, String tel, String wxNum, String qqNum, String post,
                                 String followStatus, String origin, String note, String preMoney,
                                 String industry, String demand, String company, String taxNum,
                                 String emailAddress, String invoiceAddress,
                                 String landLine, String bankName, String bankNo) {
        String randomId = RandomUtil.getItemID(20);
        //LogUtils.d("sxs", "创建客户id-----" + randomId);
        RequestParams params = new RequestParams();
        params.add("id", randomId);
        params.add("deviceID", Constants.IMEI);
        params.add("contactName", userName);
        params.add("contactTel", tel);
        params.add("contactWx", wxNum);
        params.add("contactQQ", qqNum);
        params.add("contactIdentity", post);
        params.add("workFlow", followStatus);
        params.add("source", origin);
        params.add("sourceText", note);
        params.add("budget", preMoney);
        params.add("industry", industry);
        params.add("text", demand);
        params.add("companyName", company);
        params.add("companyNum", taxNum);
        params.add("companyBank", bankName);
        params.add("companyBankNum", bankNo);
        params.add("landline", landLine);
        params.add("companyAddress", invoiceAddress);
        OkHttp.post(Constants.BASE_URL + "cla=client&fun=edit", params, this);
    }

    /**
     * 温馨提示
     */
    @Override
    public void requestWarmPrompt() {
        RequestParams params = new RequestParams();
        OkHttp.post(Constants.BASE_URL + "cla=client&fun=shareWarn", params, this);
    }

    /**
     * 创建介绍客户
     *
     * @param companyName  公司名称
     * @param contact      联系人
     * @param tel          联系电话
     * @param demand       需求描述
     * @param origin       客户来源
     * @param followState  跟进状态
     * @param introduceObj 被介绍对象
     */
    @Override
    public void createIntroduceCustom(String companyName, String contact, String tel, String demand,
                                      String origin, String followState, String introduceObj) {
        RequestParams params = new RequestParams();
        params.add("companyName", companyName);
        params.add("contactName", contact);
        params.add("contactTel", tel);
        params.add("text", demand);
        params.add("workFlow", followState);
        params.add("source", origin);
        params.add("staffId", introduceObj);
        OkHttp.post(Constants.BASE_URL + "cla=client&fun=share", params, this);
    }

    /**
     * 请求客户列表
     *
     * @param pageCount 分页页码
     */
    @Override
    public void requestALlCustomData(int pageCount) {
        RequestParams params = new RequestParams();
        OkHttp.post(Constants.BASE_URL + "cla=client&fun=home&page=" + pageCount, params, this);
    }

    /**
     * 客户列表 -- 状态 condition
     */
    @Override
    public void requestStateCondition() {
        RequestParams params = new RequestParams();
        OkHttp.post(Constants.BASE_URL + "cla=client&fun=clientWorkFlow", params, this);
    }

    /**
     * 客户列表 -- 区域condition
     */
    @Override
    public void requestAreaCondition() {

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
                    item.setName("全部");
                    break;
                case 1:
                    item.setName("已签约");
                    break;
                case 2:
                    item.setName("优质");
                    break;
                case 3:
                    item.setName("一般");
                    break;
                case 4:
                    item.setName("很差");
                    break;
                case 5:
                    item.setName("待联系");
                    break;
                case 6:
                    item.setName("已放弃");
                    break;
            }
            selectedList.add(item);
        }

        Map<String, List<SelectedItem>> selectedMap = new HashMap<>();
        selectedMap.put("selectedItem", selectedList);
        mCustomCallback.getALlStatus(selectedMap);
    }


    /**
     * 客户模块，暂时先固定
     */
    private String[] customModules = {"客户附件", "订单列表", "跟进记录", "外出记录", "加班记录", "回款记录",
            "开票信息", "开票记录"};
    private int[] customDetailModuleIcons = {R.drawable.ic_custom_attach, R.drawable.ic_custom_order_list,
            R.drawable.ic_custom_follow_record, R.drawable.ic_custom_out_record, R.drawable.ic_custom_overtime_record,
            R.drawable.ic_custom_receivable_record, R.drawable.ic_custom_invoice_data, R.drawable.ic_custom_invoice_record};

    @Override
    public void getDetailData() {
        if (mCustomCallback != null) {
            mCustomCallback.onLoading();
        }
        Map<String, List> contentMap = new HashMap<>();
        List<IconAndFont> moduleItemList = new ArrayList<>();
        for (int i = 0; i < customModules.length; i++) {
            IconAndFont moduleCustom = new IconAndFont();
            moduleCustom.setName(customModules[i]);
            moduleCustom.setLocalMipmap(customDetailModuleIcons[i]);
            moduleItemList.add(moduleCustom);
        }

        contentMap.put("module", moduleItemList);
        mCustomCallback.getCustomDetailData(contentMap);
    }

    /**
     * 客户详情
     *
     * @param khid
     */
    @Override
    public void requestCustomDetailById(String khid) {
        RequestParams params = new RequestParams();
        params.add("khid", khid);
        OkHttp.post(Constants.BASE_URL + "cla=client&fun=detail", params, this);
    }

    /**
     * 客户的联系人列表
     *
     * @param khid 客户id
     */
    @Override
    public void requestContactListByCustomId(String khid) {
        RequestParams params = new RequestParams();
        params.add("khid", khid);
        OkHttp.post(Constants.BASE_URL + "cla=client&fun=kehuStaff", params, this);
    }

    /**
     * 创建客户的联系人
     *
     * @param customId
     * @param contactId
     * @param post
     * @param name
     * @param tel
     * @param lineTel
     * @param qqNum
     * @param wxNum
     * @param note
     */
    @Override
    public void requestCreateContact(String customId, String contactId, String post, String name, String tel, String lineTel,
                                     String qqNum, String wxNum, String note) {
        RequestParams params = new RequestParams();
        params.add("khid", customId);
        params.add("id", contactId);
        params.add("position", post);
        params.add("name", name);
        params.add("tel", tel);
        params.add("phone", lineTel);
        params.add("qq", qqNum);
        params.add("WeChat", wxNum);
        params.add("text", note);
        OkHttp.post(Constants.BASE_URL + "cla=client&fun=kehuStaffEdit", params, this);
    }

    /**
     * 请求客户下的员工列表
     */
    @Override
    public void requestCustomStaff() {
        RequestParams params = new RequestParams();
        OkHttp.post(Constants.BASE_URL + "cla=client&fun=staffClientEdit", params, this);
    }

    /**
     * 添加协作人
     *
     * @param customId 客户id
     * @param staffId  员工id
     */
    @Override
    public void requestPlusAssociates(String customId, String staffId) {
        RequestParams params = new RequestParams();
        LogUtils.d("sxs", "customId ---- " + customId);
        LogUtils.d("sxs", "staffId ---- " + staffId);
        params.add("khid", customId);
        params.add("staffId", staffId);
        OkHttp.post(Constants.BASE_URL + "cla=client&fun=cooperate", params, this);
    }

    /**
     * 删除协作人
     *
     * @param customId 客户id
     * @param staffId  员工id
     */
    @Override
    public void requestDeleteAssociates(String customId, String staffId) {
        RequestParams params = new RequestParams();
        params.add("khid", customId);
        params.add("stid", staffId);
        OkHttp.post(Constants.BASE_URL + "cla=client&fun=cooperateDel", params, this);
    }

    /**
     * 客户转让
     *
     * @param customId 客户id
     * @param staffId  被转让的员工ID
     */
    @Override
    public void requestRevCustom(String customId, String staffId) {
        RequestParams params = new RequestParams();
        params.add("khid", customId);
        params.add("stid", staffId);
        OkHttp.post(Constants.BASE_URL + "cla=client&fun=move", params, this);
    }

    /**
     * 查询客户下的开票信息
     *
     * @param customId
     */
    @Override
    public void requestCustomInvoice(String customId) {
        RequestParams params = new RequestParams();
        params.add("khid", customId);
        OkHttp.post(Constants.BASE_URL + "cla=client&fun=invoice", params, this);
    }

    @Override
    public void registerViewCallback(ICustomCallbacks callback) {
        this.mCustomCallback = callback;
    }

    @Override
    public void unregisterViewCallback(ICustomCallbacks callback) {
        mCustomCallback = null;
    }

    @Override
    public void onHttpFailure(HttpResponse result) {
        mCustomCallback.onError();
    }

    /**
     * response
     *
     * @param result response succeed information
     *               success: response 成功flag
     */
    @Override
    public void onHttpSucceed(HttpResponse result) {
        LogUtils.d("sxs", "result ---- " + result.body());
        if (!(result.code() == 200)) {
            mCustomCallback.onError();
            return;
        }
        try {
            String warn = JsonParser.parseJSONObjectString(result.body()).getString("warn");
            if (!"success".equals(warn)) {
                mCustomCallback.onError(warn);
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //   跟进状态
        if (result.url().contains("cla=client&fun=clientWorkFlow")) {
            try {
                JSONArray jsonArray = JsonParser.parseJSONArrayString(
                        JsonParser.parseJSONObjectString(result.body()).getString("para"));
                List<SelectedItem> stateList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    SelectedItem item = new SelectedItem();
                    item.setName(jsonArray.getString(i));
                    stateList.add(item);
                }
                mCustomCallback.getStateCondition(stateList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (result.url().contains("cla=client&fun=clientSource")) {
            // 客户来源
            try {
                JSONArray jsonArray = JsonParser.parseJSONArrayString(
                        JsonParser.parseJSONObjectString(result.body()).getString("para"));
                List<String> originList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    originList.add(jsonArray.getString(i));
                }
                mCustomCallback.getCustomOrigin(originList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (result.url().contains("cla=client&fun=edit")) {
            // 创建/编辑客户
            JSONObject jsonObject = JsonParser.parseJSONObjectString(result.body());
            try {
                mCustomCallback.createCustomData(jsonObject.get("warn").equals("success"),
                        jsonObject.getString("warn"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 温馨提示
        else if (result.url().contains("cla=client&fun=shareWarn")) {
            try {
                String warnPrompt = JsonParser.parseJSONObjectString(result.body()).getString("share");
                mCustomCallback.getWarnPrompt(warnPrompt);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 新建介绍客户
        else if (result.url().equals(Constants.BASE_URL + "cla=client&fun=share")) {
            try {
                // 介绍人
                String introduce = JsonParser.parseJSONObjectString(result.body()).getString("share");
                // 被介绍人
                String introduced = JsonParser.parseJSONObjectString(result.body()).getString("staffName");
                // 客户名称
                String customName = JsonParser.parseJSONObjectString(result.body()).getString("companyName");
                Map<String, String> map = new HashMap<>();
                map.put("introduce", introduce);
                map.put("introduced", introduced);
                map.put("customName", customName);
                mCustomCallback.getIntroduceCreateData(map);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        // 客户列表
        else if (result.url().contains("cla=client&fun=home&page")) {
            try {
                List<Custom> customList = JsonParser.parseJSONArray(Custom.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("kehu"));
                if (ListUtils.getSize(customList) == 0) {
                    mCustomCallback.onEmpty();
                    return;
                }
                mCustomCallback.getAllCustomList(customList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 客户列表 ---- 状态condition
      /*  else if (result.url().contains("cla=client&fun=clientWorkFlow")) {
            try {
                JSONArray stateArray = JsonParser.parseJSONArrayString(
                        JsonParser.parseJSONObjectString(result.body()).getString("para"));
                List<SelectedItem> stateList = new ArrayList<>();
                for (int i = 0; i < stateArray.length(); i++) {
                    SelectedItem item = new SelectedItem();
                    item.setName(stateArray.getString(i));
                    stateList.add(item);
                }
                mCustomCallback.getStateCondition(stateList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }*/
        else if (result.url().contains("cla=client&fun=detail")) {
            // 客户详情
            CustomDetail customDetail = JsonParser.parseJSONObject(CustomDetail.class, result.body());
            HashMap<String, CustomDetail> dataDetailMap = new HashMap<String, CustomDetail>();
            dataDetailMap.put("custom", customDetail);
            mCustomCallback.getCustomDetailValues(dataDetailMap);
        } else if (result.url().contains("cla=client&fun=kehuStaff")) {
            // 客户下的联系人列表
            try {
                List<Contact> contactList = JsonParser.parseJSONArray(Contact.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("kehuStaff"));
                mCustomCallback.getCustomContactList(contactList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (result.url().contains("cla=client&fun=kehuStaffEdit")) {
            // 创建/新建联系人
            JSONObject jsonObject = JsonParser.parseJSONObjectString(result.body());
            try {
                mCustomCallback.createContactData(jsonObject.get("warn").equals("success"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (result.url().contains("cla=client&fun=staffClientEdit")) {
            // 客户下的联系人列表
            try {
                List<CustomStaff> customStaffList = JsonParser.parseJSONArray(CustomStaff.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("staff"));
                mCustomCallback.getCustomOfStaff(customStaffList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (result.url().contains("cla=client&fun=cooperate")) {
            // 添加协作人

        } else if (result.url().contains("cla=client&fun=cooperateDel")) {
            // 删除协作人

        } else if (result.url().contains("cla=client&fun=move")) {
            // 客户转让
        } else if (result.url().contains("cla=client&fun=invoice")) {
            // 开票信息获取
            try {
                CustomInvoice customInvoice = JsonParser.parseJSONObject(CustomInvoice.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("invoice"));
                mCustomCallback.getCustomInvoice(customInvoice);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
