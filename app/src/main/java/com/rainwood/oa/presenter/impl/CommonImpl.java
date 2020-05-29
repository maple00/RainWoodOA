package com.rainwood.oa.presenter.impl;

import com.rainwood.oa.model.domain.AuditRecords;
import com.rainwood.oa.presenter.ICommonPresenter;
import com.rainwood.oa.view.ICommonCallbacks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/5/26 14:22
 * @Desc: 公共数据请求---下拉选择框数据、popWindow数据，dialog数据
 */
public final class CommonImpl implements ICommonPresenter {

    private ICommonCallbacks mCommonCallbacks;

    /**
     * 审核记录列表
     */
    @Override
    public void AuditRecords() {
        // 模拟审核记录
        List<AuditRecords> recordsList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            AuditRecords auditRecords = new AuditRecords();
            auditRecords.setContent("韩梅梅驳回了蔡川的加班申请");
            auditRecords.setTime("2020.03.20 16:48");
            recordsList.add(auditRecords);
        }

        Map<String, List<AuditRecords>> recordMap = new HashMap<>();
        recordMap.put("overtime", recordsList);

        mCommonCallbacks.getAuditRecords(recordMap);
    }

    @Override
    public void registerViewCallback(ICommonCallbacks callback) {
        mCommonCallbacks = callback;
    }

    @Override
    public void unregisterViewCallback(ICommonCallbacks callback) {
        mCommonCallbacks = null;
    }
}
