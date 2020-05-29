package com.rainwood.oa.presenter;

import com.rainwood.oa.base.IBasePresenter;
import com.rainwood.oa.view.ICommonCallbacks;

import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/5/26 14:21
 * @Desc: 公共数据
 */
public interface ICommonPresenter extends IBasePresenter<ICommonCallbacks> {

    /**
     * 客户跟进状态
     */
    void AuditRecords();
}
