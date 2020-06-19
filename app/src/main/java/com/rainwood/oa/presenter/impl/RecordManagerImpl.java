package com.rainwood.oa.presenter.impl;

import com.rainwood.oa.model.domain.AdminLeaveOut;
import com.rainwood.oa.model.domain.AdminOverTime;
import com.rainwood.oa.model.domain.CardRecord;
import com.rainwood.oa.model.domain.CustomFollowRecord;
import com.rainwood.oa.model.domain.FinancialInvoiceRecord;
import com.rainwood.oa.model.domain.InvoiceRecord;
import com.rainwood.oa.model.domain.KnowledgeFollowRecord;
import com.rainwood.oa.model.domain.LeaveOutRecord;
import com.rainwood.oa.model.domain.LeaveRecord;
import com.rainwood.oa.model.domain.OvertimeRecord;
import com.rainwood.oa.model.domain.ReceivableRecord;
import com.rainwood.oa.network.json.JsonParser;
import com.rainwood.oa.network.okhttp.HttpResponse;
import com.rainwood.oa.network.okhttp.OkHttp;
import com.rainwood.oa.network.okhttp.OnHttpListener;
import com.rainwood.oa.network.okhttp.RequestParams;
import com.rainwood.oa.presenter.IRecordManagerPresenter;
import com.rainwood.oa.utils.Constants;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.oa.utils.RandomUtil;
import com.rainwood.oa.view.IRecordCallbacks;
import com.rainwood.tools.toast.ToastUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/5/25 19:59
 * @Desc: 记录管理逻辑类
 */
public final class RecordManagerImpl implements IRecordManagerPresenter, OnHttpListener {

    private IRecordCallbacks mRecordCallbacks;

    /**
     * 客户-加班记录
     */
    @Override
    public void requestOvertimeRecord(String customId) {
        RequestParams params = new RequestParams();
        params.add("khid", customId);
        OkHttp.post(Constants.BASE_URL + "cla=client&fun=workAdd", params, this);
    }

    /**
     * 行政人事--- 加班记录
     */
    @Override
    public void requestOvertimeRecord() {
        RequestParams params = new RequestParams();
        OkHttp.post(Constants.BASE_URL + "cla=workAdd&fun=home", params, this);
    }

    /**
     * 请假记录
     */
    @Override
    public void requestLeaveRecord() {
        RequestParams params = new RequestParams();
        OkHttp.post(Constants.BASE_URL + "cla=work&fun=home", params, this);
    }

    /**
     * 外出记录
     *
     * @param customId 客户id
     */
    @Override
    public void requestGoOutRecord(String customId) {
        RequestParams params = new RequestParams();
        params.add("khid", customId);
        OkHttp.post(Constants.BASE_URL + "cla=client&fun=out", params, this);
    }

    /**
     * 行政人事--外出记录
     */
    @Override
    public void requestGoOutRecord() {
        RequestParams params = new RequestParams();
        OkHttp.post(Constants.BASE_URL + "cla=workOut&fun=home", params, this);
    }

    /**
     * 行政人事---补卡记录
     */
    @Override
    public void requestReissueRecord() {
        RequestParams params = new RequestParams();
        OkHttp.post(Constants.BASE_URL + "cla=workSignAdd&fun=home", params, this);
    }

    /**
     * 请求客户的跟进记录
     *
     * @param customId 客户id
     */
    @Override
    public void requestCustomFollowRecords(String customId) {
        RequestParams params = new RequestParams();
        params.add("khid", customId);
        OkHttp.post(Constants.BASE_URL + "cla=client&fun=follow", params, this);
    }

    /**
     * 客户回款记录
     *
     * @param customId 客户id
     */
    @Override
    public void requestCustomReceivableRecords(String customId) {
        RequestParams params = new RequestParams();
        params.add("khid", customId);
        OkHttp.post(Constants.BASE_URL + "cla=client&fun=collection", params, this);
    }

    /**
     * 客户回款记录详情
     *
     * @param receivableId 汇款记录id
     */
    @Override
    public void requestCustomReceivableRecordDetail(String receivableId) {
        RequestParams params = new RequestParams();
        params.add("id ", receivableId);
        OkHttp.post(Constants.BASE_URL + "cla=client&fun=collectionDetail", params, this);
    }

    /**
     * 请求客户下的开票记录
     *
     * @param customId 客户id
     */
    @Override
    public void requestCustomInvoiceRecords(String customId) {
        RequestParams params = new RequestParams();
        params.add("id ", customId);
        OkHttp.post(Constants.BASE_URL + "cla=client&fun=invoiceLi", params, this);
    }

    /**
     * 财务管理---开票记录
     * @param type 为空queryAll， 已拨付：是，未拨付：否
     */
    @Override
    public void requestInvoiceRecords(String type) {
        RequestParams params = new RequestParams();
        params.add("open", type);
        OkHttp.post(Constants.BASE_URL + "cla=kehuInvoice&fun=home", params, this);
    }

    /**
     * 请求客户下的开票记录中新建开票中的页面参数
     */
    @Override
    public void requestCustomInvoiceParams() {
        RequestParams params = new RequestParams();
        OkHttp.post(Constants.BASE_URL + "cla=client&fun=invoicePara", params, this);
    }

    /**
     * 新建开票记录
     *
     * @param seller   销售方
     * @param type     发票类型
     * @param money    价税合计
     * @param note     备注
     * @param customId 客户id
     */
    @Override
    public void CreateInvoiceRecord(String seller, String type, String money, String note, String customId) {
        RequestParams params = new RequestParams();
        params.add("id", RandomUtil.getItemID(20));
        params.add("company", seller);
        params.add("type", type);
        params.add("money", money);
        params.add("text", note);
        params.add("clientId", customId);
        OkHttp.post(Constants.BASE_URL + "cla=client&fun=invoiceAdd", params, this);
    }

    /**
     * 知识管理--跟进记录
     */
    @Override
    public void requestKnowledgeFollowRecords() {
        // 模拟知识管理中的跟进记录
        List<KnowledgeFollowRecord> recordList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            KnowledgeFollowRecord record = new KnowledgeFollowRecord();
            record.setTarget((i % 2 == 0) ? " 客户" : "订单");
            record.setTargetName("重庆市永川区兴永建设发展有限公司项目管理");
            record.setText("在全球，随着Flutter被越来越多的知名公司应用在自己的商业APP中，" +
                    "Flutter这门新技术也逐渐进入了移动开发者的视野，尤其是当Google在2018年IO大会上发布了第一个" +
                    "Preview版本后，国内刮起来一股学习Flutter的热潮。\n\n为了更好的方便帮助中国开发者了解这门新技术" +
                    "，我们，Flutter中文网，前后发起了Flutter翻译计划、Flutter开源计划，前者主要的任务是翻译" +
                    "Flutter官方文档，后者则主要是开发一些常用的包来丰富Flutter生态，帮助开发者提高开发效率。而时" +
                    "至今日，这两件事取得的效果还都不错！");
            record.setStaffName("Android攻城狮");
            record.setTime("  2020.04.10 13:37");
            recordList.add(record);
        }

        RequestParams params = new RequestParams();
        OkHttp.post(Constants.BASE_URL + "cla=follow&fun=home", params, this);

    }

    @Override
    public void registerViewCallback(IRecordCallbacks callback) {
        mRecordCallbacks = callback;
    }

    @Override
    public void unregisterViewCallback(IRecordCallbacks callback) {
        mRecordCallbacks = null;
    }

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        LogUtils.d("sxs", "result ---- " + result.body());
        if (!(result.code() == 200)) {
            mRecordCallbacks.onError();
            return;
        }
        try {
            String warn = JsonParser.parseJSONObjectString(result.body()).getString("warn");
            if (!"success".equals(warn)) {
                ToastUtils.show(warn);
                mRecordCallbacks.onError(warn);
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 客户管理-----跟进记录
        if (result.url().contains("cla=client&fun=follow")) {
            try {
                List<CustomFollowRecord> customFollowRecordList = JsonParser.parseJSONArray(CustomFollowRecord.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("follow"));
                mRecordCallbacks.getCustomFollowRecords(customFollowRecordList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 客户管理----外出记录
        else if (result.url().contains("cla=client&fun=out")) {
            try {
                List<LeaveOutRecord> customFollowRecordList = JsonParser.parseJSONArray(LeaveOutRecord.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("out"));
                mRecordCallbacks.getGoOutRecords(customFollowRecordList);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        // 客户管理---加班记录
        else if (result.url().contains("cla=client&fun=workAdd")) {
            try {
                List<OvertimeRecord> overtimeRecordList = JsonParser.parseJSONArray(OvertimeRecord.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("add"));
                mRecordCallbacks.getOvertimeRecords(overtimeRecordList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 客户管理----回款记录
        else if (result.url().equals(Constants.BASE_URL + "cla=client&fun=collection")) {
            try {
                List<ReceivableRecord> receivableList = JsonParser.parseJSONArray(ReceivableRecord.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("collection"));
                mRecordCallbacks.getCustomReceivableRecords(receivableList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //客户管理---- 回款记录详情
        else if (result.url().equals(Constants.BASE_URL + "cla=client&fun=collectionDetail")) {
            try {
                ReceivableRecord receivableRecord = JsonParser.parseJSONObject(ReceivableRecord.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("collection"));
                mRecordCallbacks.getCustomReceivableRecordDetail(receivableRecord);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 客户管理--开票记录
        else if (result.url().contains("cla=client&fun=invoiceLi")) {
            try {
                List<InvoiceRecord> invoiceList = JsonParser.parseJSONArray(InvoiceRecord.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("invoice"));
                mRecordCallbacks.getCustomInvoiceRecords(invoiceList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 客户下开票记录中新建开票记录页面数据
        else if (result.url().contains("cla=client&fun=invoicePara")) {
            try {
                JSONArray sellerArray = JsonParser.parseJSONArrayString(
                        JsonParser.parseJSONObjectString(
                                JsonParser.parseJSONObjectString(
                                        result.body()).getString("para")).getString("company"));
                String taxRate = JsonParser.parseJSONObjectString(
                        JsonParser.parseJSONObjectString(
                                result.body()).getString("para")).getString("taxRate");
                List<String> sellerList = new ArrayList<>();
                for (int i = 0; i < sellerArray.length(); i++) {
                    sellerList.add(sellerArray.getString(i));
                }
                Map<String, Object> invoiceNewPageParams = new HashMap<>();
                invoiceNewPageParams.put("sellers", sellerList);
                invoiceNewPageParams.put("rate", taxRate);
                mRecordCallbacks.getCustomNewInvoiceRecordsPageParams(invoiceNewPageParams);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 客户管理---新增开票记录
        else if (result.url().contains("cla=client&fun=invoiceAdd")) {
            JSONObject jsonObject = JsonParser.parseJSONObjectString(result.body());
            try {
                mRecordCallbacks.createInvoiceRecord(jsonObject.get("warn").equals("success"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 行政人事--- 加班记录
        else if (result.url().contains("cla=workAdd&fun=home")) {
            try {
                List<AdminOverTime> adminOvertimeRecordList = JsonParser.parseJSONArray(AdminOverTime.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("add"));
                mRecordCallbacks.getAdminOverTimeRecords(adminOvertimeRecordList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 请假记录
        else if (result.url().contains("cla=work&fun=home")){
            try {
                List<LeaveRecord> leaveRecordList = JsonParser.parseJSONArray(LeaveRecord.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("work"));
                mRecordCallbacks.getLeaveRecords(leaveRecordList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 行政人事--外出记录
        else if (result.url().contains("cla=workOut&fun=home")){
            try {
                List<AdminLeaveOut> adminLeaveOutList = JsonParser.parseJSONArray(AdminLeaveOut.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("out"));
                mRecordCallbacks.getAdminLeaveOutRecords(adminLeaveOutList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 行政人事--- 补卡记录
        else if (result.url().contains("cla=workSignAdd&fun=home")){
            try {
                List<CardRecord> cardRecords = JsonParser.parseJSONArray(CardRecord.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("add"));
                mRecordCallbacks.getReissueRecords(cardRecords);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 财务管理--开票记录
        else if (result.url().contains("cla=kehuInvoice&fun=home")){
            try {
                List<FinancialInvoiceRecord> financialInvoiceRecordList = JsonParser.parseJSONArray(FinancialInvoiceRecord.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("invoice"));
                mRecordCallbacks.getFinancialInvoiceRecords(financialInvoiceRecordList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 知识管理-- 跟进记录 KnowledgeFollowRecord
        else if (result.url().contains("cla=follow&fun=home")){
            try {
                List<KnowledgeFollowRecord> knowledgeFollowRecordList = JsonParser.parseJSONArray(KnowledgeFollowRecord.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("follow"));
                mRecordCallbacks.getKnowledgeFollowRecords(knowledgeFollowRecordList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
