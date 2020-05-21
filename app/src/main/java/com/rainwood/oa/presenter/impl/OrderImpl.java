package com.rainwood.oa.presenter.impl;

import com.rainwood.oa.model.domain.Examination;
import com.rainwood.oa.model.domain.Order;
import com.rainwood.oa.model.domain.OrderStatics;
import com.rainwood.oa.presenter.IOrderPresenter;
import com.rainwood.oa.view.IOrderCallbacks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/5/19 16:52
 * @Desc: 订单逻辑类
 */
public final class OrderImpl implements IOrderPresenter {

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

    @Override
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
                if ("已回款".equals(staticsTitle)){
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

    @Override
    public void registerViewCallback(IOrderCallbacks callback) {
        mOrderEditCallbacks = callback;
    }

    @Override
    public void unregisterViewCallback(IOrderCallbacks callback) {
        mOrderEditCallbacks = null;
    }
}
