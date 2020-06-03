package com.rainwood.oa.presenter.impl;

import android.text.TextUtils;

import com.rainwood.oa.model.domain.CustomOrder;
import com.rainwood.oa.model.domain.CustomOrderValues;
import com.rainwood.oa.model.domain.Examination;
import com.rainwood.oa.model.domain.FontAndFont;
import com.rainwood.oa.model.domain.Order;
import com.rainwood.oa.model.domain.OrderValues;
import com.rainwood.oa.model.domain.PrimaryKey;
import com.rainwood.oa.network.json.JsonParser;
import com.rainwood.oa.network.okhttp.HttpResponse;
import com.rainwood.oa.network.okhttp.OkHttp;
import com.rainwood.oa.network.okhttp.OnHttpListener;
import com.rainwood.oa.network.okhttp.RequestParams;
import com.rainwood.oa.presenter.IOrderPresenter;
import com.rainwood.oa.utils.Constants;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.oa.utils.RandomUtil;
import com.rainwood.oa.view.IOrderCallbacks;
import com.rainwood.tools.toast.ToastUtils;

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

  /*  @Override
    public void requestOrderData() {
        Map<String, List> orderMap = new HashMap<>();
        // 模拟所有订单的统计信息
        List<OrderStatics> staticsList = new ArrayList<>();
        String[] staticsTitles = {"合同金额", "费用计提", "合同净值", "已回款", "已付费用", "净回款", "合同应收", "剩余净值"};
        for (int i = 0; i < staticsTitles.length; i++) {
            OrderStatics statics = new OrderStatics();
            statics.setTitle(staticsTitles[i]);
            statics.setValues("￥7669646.86" + i);
            staticsList.add(statics);
        }
        // 模拟所有的订单
        // 订单中所有为空得字段，都不会显示出来
        List<Order> orderList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Order order = new Order();
            order.setNo("6345001937");
            order.setStatus("开发中");
            order.setOrderName("正南齐北科技制造业ERP");
            order.setMoney("7800.00");
            order.setChargeName("圣枪游侠");
            order.setTimeLimit("12");
            order.setStartTime("2020.04.10");
            order.setEndTime("2020.04.22");
            List<OrderStatics> natureList = new ArrayList<>();
            for (String staticsTitle : staticsTitles) {
                OrderStatics statics = new OrderStatics();
                statics.setTitle(staticsTitle);
                statics.setValues("￥7669646.86");
                if ("已回款".equals(staticsTitle)) {
                    statics.setValues(null);
                }
                natureList.add(statics);
            }
            order.setNatureList(natureList);
            orderList.add(order);
        }
        orderMap.put("orderStatics", staticsList);
        orderMap.put("order", orderList);

        mOrderEditCallbacks.getAllOrderPage(orderMap);
    }
*/

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
     * 请求订单列表
     */
    @Override
    public void requestOrderList() {
        RequestParams params = new RequestParams();
        OkHttp.post(Constants.BASE_URL + "cla=order&fun=home", params, this);
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
    }
}
