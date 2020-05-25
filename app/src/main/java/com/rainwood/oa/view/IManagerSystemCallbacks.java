package com.rainwood.oa.view;

import com.rainwood.oa.base.IBaseCallback;
import com.rainwood.oa.model.domain.ManagerSystem;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/25 19:02
 * @Desc: 管理制度
 */
public interface IManagerSystemCallbacks extends IBaseCallback {

    /**
     * 获取管理制度的所有内容
     */
    void getAllData(List<ManagerSystem> systemList);
}
