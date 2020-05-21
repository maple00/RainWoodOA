package com.rainwood.oa.view;

import com.rainwood.oa.base.IBaseCallback;
import com.rainwood.oa.model.domain.Depart;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/21 17:38
 * @Desc:
 */
public interface IDepartCallbacks extends IBaseCallback {

    /**
     * 所有的部门数据
     * @param departList
     */
    void getAllDepartData(List<Depart> departList);
}
