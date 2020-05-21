package com.rainwood.oa.presenter;

import com.rainwood.oa.base.IBasePresenter;
import com.rainwood.oa.view.IDepartCallbacks;

/**
 * @Author: a797s
 * @Date: 2020/5/21 17:39
 * @Desc:
 */
public interface IDepartPresenter extends IBasePresenter<IDepartCallbacks> {

    /**
     * 请求所有的部门数据
     */
    void getAllDepartData();
}
