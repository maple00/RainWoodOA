package com.rainwood.oa.presenter.impl;

import android.text.TextUtils;

import com.rainwood.oa.model.domain.CustomOrder;
import com.rainwood.oa.model.domain.CustomOrderValues;
import com.rainwood.oa.model.domain.Examination;
import com.rainwood.oa.model.domain.FontAndFont;
import com.rainwood.oa.model.domain.Order;
import com.rainwood.oa.model.domain.OrderCost;
import com.rainwood.oa.model.domain.OrderDetailAttachment;
import com.rainwood.oa.model.domain.OrderDetailBaseValues;
import com.rainwood.oa.model.domain.OrderFollow;
import com.rainwood.oa.model.domain.OrderPayed;
import com.rainwood.oa.model.domain.OrderReceivable;
import com.rainwood.oa.model.domain.OrderTask;
import com.rainwood.oa.model.domain.OrderValues;
import com.rainwood.oa.model.domain.PrimaryKey;
import com.rainwood.oa.model.domain.SelectedItem;
import com.rainwood.oa.network.json.JsonParser;
import com.rainwood.oa.network.okhttp.HttpResponse;
import com.rainwood.oa.network.okhttp.OkHttp;
import com.rainwood.oa.network.okhttp.OnHttpListener;
import com.rainwood.oa.network.okhttp.RequestParams;
import com.rainwood.oa.presenter.IOrderPresenter;
import com.rainwood.oa.utils.Constants;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.oa.utils.RandomUtil;
import com.rainwood.oa.view.IOrderCallbacks;
import com.rainwood.tools.toast.ToastUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/5/19 16:52
 * @Desc: 订单逻辑类
 */
public final class OrderImpl implements IOrderPresenter, OnHttpListener {

    private IOrderCallbacks mOrderEditCallbacks;

    @Override
    public void requestAllExaminationData() {
        // 模拟所有的审批人
        List<Examination> examinationList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Examination examination = new Examination();
            examination.setName("德玛西亚");
            examination.setDepart("技术部");
            examination.setPost("开发攻城狮");
            examination.setHeadPhoto("https://www.baidu.com/img/bd_logo.png");
            examinationList.add(examination);
        }
        Map<String, List<Examination>> dataMap = new HashMap<>();
        dataMap.put("examination", examinationList);
        mOrderEditCallbacks.getAllExaminationData(dataMap);
    }

    /**
     * 请求客户下的订单列表
     *
     * @param customId
     */
    @Override
    public void requestCustomOrderList(String customId) {
        RequestParams params = new RequestParams();
        params.add("khid", customId);
        OkHttp.post(Constants.BASE_URL + "cla=client&fun=orderLi", params, this);
    }

    /**
     * 创建订单
     */
    @Override
    public void CreateNewOrder(String customId, String orderNameStr, String moneyStr, String noteStr) {
        RequestParams params = new RequestParams();
        params.add("khid", customId);
        params.add("id", RandomUtil.getNumberId(20));
        params.add("name", orderNameStr);
        params.add("money", moneyStr);
        params.add("text", noteStr);
        OkHttp.post(Constants.BASE_URL + "cla=order&fun=edit", params, this);
    }

    /**
     * 通过关键字查询客户名称
     *
     * @param key
     */
    @Override
    public void requestCustomName(String key) {
        RequestParams params = new RequestParams();
        params.add("key", key);
        OkHttp.post(Constants.BASE_URL + "cla=order&fun=clientKey", params, this);
    }

    /**
     * 订单列表
     *
     * @param orderName 搜索关键字
     * @param state     状态
     * @param staffId   部门员工
     * @param sorting   排序
     */
    @Override
    public void requestOrderList(String orderName, String state, String staffId, String sorting, int page) {
        RequestParams params = new RequestParams();
        params.add("name", orderName);
        params.add("workFlow", state);
        params.add("stid", staffId);
        params.add("orderBy", sorting);
        OkHttp.post(Constants.BASE_URL + "cla=order&fun=home&page=" + page, params, this);
    }

    /**
     * 订单列表 -- condition
     */
    @Override
    public void requestCondition() {
        RequestParams params = new RequestParams();
        OkHttp.post(Constants.BASE_URL + "cla=order&fun=search", params, this);
    }

    /**
     * 请求订单详情
     *
     * @param orderId
     */
    @Override
    public void requestOrderDetailById(String orderId) {
        RequestParams params = new RequestParams();
        params.add("id", orderId);
        OkHttp.post(Constants.BASE_URL + "cla=order&fun=detail", params, this);
    }

    @Override
    public void registerViewCallback(IOrderCallbacks callback) {
        mOrderEditCallbacks = callback;
    }

    @Override
    public void unregisterViewCallback(IOrderCallbacks callback) {
        mOrderEditCallbacks = null;
    }

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    /**
     * response
     *
     * @param result response succeed information
     *               success : response flag
     */
    @Override
    public void onHttpSucceed(HttpResponse result) {
        LogUtils.d("sxs", "result ---- " + result.body());
        if (!(result.code() == 200)) {
            mOrderEditCallbacks.onError();
            return;
        }
        try {
            String warn = JsonParser.parseJSONObjectString(result.body()).getString("warn");
            if (!"success".equals(warn)) {
                ToastUtils.show(warn);
                mOrderEditCallbacks.onError(warn);
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (result.url().contains("cla=client&fun=orderLi")) {
            // 客户订单列表
            try {
                List<CustomOrderValues> customOrderValuesList = JsonParser.parseJSONArray(CustomOrderValues.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("order"));
                // 处理数据 --- 对订单中的空置进行判断，为空则不显示
                // CustomOrderValues -- 与CustomOrder 一一对应
                List<CustomOrder> customOrderList = new ArrayList<>();
                for (CustomOrderValues values : customOrderValuesList) {
                    CustomOrder customOrder = new CustomOrder();
                    customOrder.setId(values.getId());
                    customOrder.setWorkFlow(values.getWorkFlow());
                    customOrder.setName(values.getName());
                    customOrder.setMoney(values.getMoney());
                    customOrder.setSignDay(values.getSignDay());
                    customOrder.setEndDay(values.getEndDay());
                    customOrder.setCycle(values.getCycle());
                    // 处理空数据
                    List<FontAndFont> valuesList = new ArrayList<>();
                    if (!TextUtils.isEmpty(values.getCost())) {
                        FontAndFont value = new FontAndFont();
                        value.setTitle("费用计提");
                        value.setDesc("￥" + values.getCost());
                        valuesList.add(value);
                    }
                    if (!TextUtils.isEmpty(values.getNetWorth())) {
                        FontAndFont value = new FontAndFont();
                        value.setTitle("合同净值");
                        value.setDesc("￥" + values.getNetWorth());
                        valuesList.add(value);
                    }
                    if (!TextUtils.isEmpty(values.getReceivable())) {
                        FontAndFont value = new FontAndFont();
                        value.setTitle("合同应收");
                        value.setDesc("￥" + values.getReceivable());
                        valuesList.add(value);
                    }
                    if (!TextUtils.isEmpty(values.getNetWorthWait())) {
                        FontAndFont value = new FontAndFont();
                        value.setTitle("剩余净值");
                        value.setDesc("￥" + values.getNetWorthWait());
                        valuesList.add(value);
                    }
                    if (!TextUtils.isEmpty(values.getRatio())) {
                        FontAndFont value = new FontAndFont();
                        value.setTitle("拨付比例");
                        value.setDesc(values.getRatio() + "%");
                        valuesList.add(value);
                    }
                    if (!TextUtils.isEmpty(values.getBuyCarAllotMoney())) {
                        FontAndFont value = new FontAndFont();
                        value.setTitle("预计奖金");
                        value.setDesc("￥" + values.getBuyCarAllotMoney());
                        valuesList.add(value);
                    }
                    if (!TextUtils.isEmpty(values.getBuyCarAllotPay())) {
                        FontAndFont value = new FontAndFont();
                        value.setTitle("已拨付");
                        value.setDesc("￥" + values.getBuyCarAllotPay());
                        valuesList.add(value);
                    }
                    if (!TextUtils.isEmpty(values.getBuyCarAllotSurplus())) {
                        FontAndFont value = new FontAndFont();
                        value.setTitle("奖金余额");
                        value.setDesc("￥" + values.getNetWorthWait());
                        valuesList.add(value);
                    }
                    if (!TextUtils.isEmpty(values.getMoneyIn())) {
                        FontAndFont value = new FontAndFont();
                        value.setTitle("已回款");
                        value.setDesc("￥" + values.getMoneyIn());
                        valuesList.add(value);
                    }
                    if (!TextUtils.isEmpty(values.getMoneyOut())) {
                        FontAndFont value = new FontAndFont();
                        value.setTitle("已付费用");
                        value.setDesc("￥" + values.getMoneyOut());
                        valuesList.add(value);
                    }
                    if (!TextUtils.isEmpty(values.getNetWorthIn())) {
                        FontAndFont value = new FontAndFont();
                        value.setTitle("净回款");
                        value.setDesc("￥" + values.getNetWorthIn());
                        valuesList.add(value);
                    }
                    customOrder.setValueList(valuesList);
                    customOrderList.add(customOrder);
                }
                mOrderEditCallbacks.getCustomOrderList(customOrderList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 通过关键字查询客户名称
        else if (result.url().contains("cla=order&fun=clientKey")) {
            try {
                List<PrimaryKey> orderCustomData = JsonParser.parseJSONArray(PrimaryKey.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("kehu"));

                mOrderEditCallbacks.getCustomDataByKey(orderCustomData);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 创建订单
        else if (result.url().contains("cla=order&fun=edit")) {
            try {
                String warn = JsonParser.parseJSONObjectString(result.body()).getString("warn");
                mOrderEditCallbacks.getCreateResult("success".equals(warn));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 总的订单列表
        else if (result.url().contains("cla=order&fun=home")) {
            try {
                List<OrderValues> orderList = JsonParser.parseJSONArray(OrderValues.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("order"));
                // 数据处理
                List<Order> orderValuesList = new ArrayList<>();
                for (OrderValues order : orderList) {
                    Order values = new Order();
                    values.setId(order.getId());
                    values.setName(order.getName());
                    values.setWorkFlow(order.getWorkFlow());
                    values.setMoney(order.getMoney());
                    values.setStaffName(order.getStaffName());

                    List<FontAndFont> valuesList = new ArrayList<>();
                    if (!TextUtils.isEmpty(order.getCost())) {
                        FontAndFont value = new FontAndFont();
                        value.setTitle("费用计提");
                        value.setDesc("￥" + order.getCost());
                        valuesList.add(value);
                    }
                    if (!TextUtils.isEmpty(order.getNetWorthOrder())) {
                        FontAndFont value = new FontAndFont();
                        value.setTitle("合同净值");
                        value.setDesc("￥" + order.getNetWorthOrder());
                        valuesList.add(value);
                    }
                    if (!TextUtils.isEmpty(order.getMoneyWait())) {
                        FontAndFont value = new FontAndFont();
                        value.setTitle("合同应收");
                        value.setDesc("￥" + order.getMoneyWait());
                        valuesList.add(value);
                    }
                    if (!TextUtils.isEmpty(order.getNetWorthIn())) {
                        FontAndFont value = new FontAndFont();
                        value.setTitle("净回款");
                        value.setDesc("￥" + order.getNetWorthIn());
                        valuesList.add(value);
                    }
                    if (!TextUtils.isEmpty(order.getNetWorthWait())) {
                        FontAndFont value = new FontAndFont();
                        value.setTitle("剩余净值");
                        value.setDesc("￥" + order.getNetWorthWait());
                        valuesList.add(value);
                    }
                    if (!TextUtils.isEmpty(order.getRatio())) {
                        FontAndFont value = new FontAndFont();
                        value.setTitle("拨付比例");
                        value.setDesc("￥" + order.getRatio());
                        valuesList.add(value);
                    }
                    if (!TextUtils.isEmpty(order.getBuyCarAllotMoney())) {
                        FontAndFont value = new FontAndFont();
                        value.setTitle("预计奖金");
                        value.setDesc("￥" + order.getBuyCarAllotMoney());
                        valuesList.add(value);
                    }
                    if (!TextUtils.isEmpty(order.getBuyCarAllotPay())) {
                        FontAndFont value = new FontAndFont();
                        value.setTitle("已拨付");
                        value.setDesc("￥" + order.getBuyCarAllotPay());
                        valuesList.add(value);
                    }
                    if (!TextUtils.isEmpty(order.getBuyCarAllotSurplus())) {
                        FontAndFont value = new FontAndFont();
                        value.setTitle("奖金余额");
                        value.setDesc("￥" + order.getBuyCarAllotSurplus());
                        valuesList.add(value);
                    }
                    values.setNatureList(valuesList);
                    orderValuesList.add(values);
                }

                mOrderEditCallbacks.getOrderList(orderValuesList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 订单详情
        else if (result.url().contains("cla=order&fun=detail")) {
            // 订单基本信息、附件列表、费用计提、跟进记录、任务分配、回款记录、已付费用
            try {
                // 附件
                List<OrderDetailAttachment> attachmentList = JsonParser.parseJSONArray(OrderDetailAttachment.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("file"));
                // 费用计提
                List<OrderCost> costList = JsonParser.parseJSONArray(OrderCost.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("cost"));
                // 跟进记录
                List<OrderFollow> followList = JsonParser.parseJSONArray(OrderFollow.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("follow"));
                // 任务分配
                List<OrderTask> taskList = JsonParser.parseJSONArray(OrderTask.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("allot"));
                // 回款记录
                List<OrderReceivable> receivableList = JsonParser.parseJSONArray(OrderReceivable.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("pay"));
                //已付费用
                List<OrderPayed> payedList = JsonParser.parseJSONArray(OrderPayed.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("out"));
                Map<String, Object> orderMap = new HashMap<>();
                orderMap.put("attachment", attachmentList);
                orderMap.put("cost", costList);
                orderMap.put("follow", followList);
                orderMap.put("task", taskList);
                orderMap.put("receivable", receivableList);
                orderMap.put("payed", payedList);
                // 处理基本数据
                OrderDetailBaseValues orderBaseValues = JsonParser.parseJSONObject(OrderDetailBaseValues.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("order"));
                // 显示数据
                List<FontAndFont> showDataList = new ArrayList<>();
                if (orderBaseValues.getId() != null) {
                    FontAndFont font = new FontAndFont();
                    font.setTitle("订单号");
                    font.setDesc(orderBaseValues.getId());
                    showDataList.add(font);
                }
                if (orderBaseValues.getName() != null) {
                    FontAndFont font = new FontAndFont();
                    font.setTitle("订单名称");
                    font.setDesc(orderBaseValues.getName());
                    showDataList.add(font);
                }
                if (orderBaseValues.getName() != null) {
                    FontAndFont font = new FontAndFont();
                    font.setTitle("立项日期");
                    font.setDesc(orderBaseValues.getSignDay());
                    showDataList.add(font);
                }
                if (orderBaseValues.getName() != null) {
                    FontAndFont font = new FontAndFont();
                    font.setTitle("交付日期");
                    font.setDesc(orderBaseValues.getEndDay());
                    showDataList.add(font);
                }
                if (orderBaseValues.getMoney() != null) {
                    FontAndFont font = new FontAndFont();
                    font.setTitle("合同金额");
                    font.setDesc("￥ " + orderBaseValues.getId());
                    showDataList.add(font);
                }
                if (orderBaseValues.getMoney() != null) {
                    FontAndFont font = new FontAndFont();
                    font.setTitle("工期");
                    font.setDesc(orderBaseValues.getCycle() + "天");
                    showDataList.add(font);
                }

                // 点击隐藏/显示内容
                List<FontAndFont> hideDataList = new ArrayList<>();
                if (orderBaseValues.getCost() != null) {
                    FontAndFont font = new FontAndFont();
                    font.setTitle("费用计提");
                    font.setDesc("￥ " + orderBaseValues.getCost());
                    hideDataList.add(font);
                }
                if (orderBaseValues.getNetWorthOrder() != null) {
                    FontAndFont font = new FontAndFont();
                    font.setTitle("合同净值");
                    font.setDesc("￥ " + orderBaseValues.getNetWorthOrder());
                    hideDataList.add(font);
                }
                if (orderBaseValues.getMoneyIn() != null) {
                    FontAndFont font = new FontAndFont();
                    font.setTitle("已回款");
                    font.setDesc("￥ " + orderBaseValues.getMoneyIn());
                    hideDataList.add(font);
                }
                if (orderBaseValues.getMoneyOut() != null) {
                    FontAndFont font = new FontAndFont();
                    font.setTitle("已付费用");
                    font.setDesc("￥ " + orderBaseValues.getMoneyOut());
                    hideDataList.add(font);
                }
                if (orderBaseValues.getNetWorthIn() != null) {
                    FontAndFont font = new FontAndFont();
                    font.setTitle("净回款");
                    font.setDesc("￥ " + orderBaseValues.getNetWorthIn());
                    hideDataList.add(font);
                }
                if (orderBaseValues.getMoneyWait() != null) {
                    FontAndFont font = new FontAndFont();
                    font.setTitle("合同应收");
                    font.setDesc("￥ " + orderBaseValues.getMoneyWait());
                    hideDataList.add(font);
                }
                if (orderBaseValues.getNetWorthWait() != null) {
                    FontAndFont font = new FontAndFont();
                    font.setTitle("剩余净值");
                    font.setDesc("￥ " + orderBaseValues.getNetWorthWait());
                    hideDataList.add(font);
                }
                orderMap.put("showData", showDataList);
                orderMap.put("hideData", hideDataList);
                if (!TextUtils.isEmpty(orderBaseValues.getWorkFlow())) {
                    orderMap.put("orderStatus", orderBaseValues.getWorkFlow());
                }
                mOrderEditCallbacks.getOrderDetail(orderMap);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 订单 --condition
        else if (result.url().contains("cla=order&fun=search")) {
            try {
                List<String> typeArray = JsonParser.parseJSONList(JsonParser.parseJSONObjectString(
                        JsonParser.parseJSONObjectString(result.body()).getString("search")).getString("workFlow"));
                List<String> payerArray = JsonParser.parseJSONList(JsonParser.parseJSONObjectString(
                        JsonParser.parseJSONObjectString(result.body()).getString("search")).getString("orderBy"));
                List<SelectedItem> stateList = new ArrayList<>();
                List<SelectedItem> sortList = new ArrayList<>();
                for (int i = 0; i < ListUtils.getSize(typeArray); i++) {
                    SelectedItem item = new SelectedItem();
                    item.setName(typeArray.get(i));
                    stateList.add(item);
                }
                for (int i = 0; i < ListUtils.getSize(payerArray); i++) {
                    SelectedItem item = new SelectedItem();
                    item.setName(payerArray.get(i));
                    sortList.add(item);
                }
                mOrderEditCallbacks.getOrderCondition(stateList, sortList);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}