package com.rainwood.oa.view;

import com.rainwood.oa.base.IBaseCallback;
import com.rainwood.oa.model.domain.ManagerMain;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/4/28 11:47
 * @Desc: 管理界面callback
 */
public interface IManagerCallbacks  extends IBaseCallback {

    /**
     * 管理界面数据
     * @param mainList
     */
    void getMainManagerData(List<ManagerMain> mainList);
}
