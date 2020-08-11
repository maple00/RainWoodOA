package com.rainwood.oa.view;

import com.rainwood.oa.base.IBaseCallback;
import com.rainwood.oa.model.domain.Logcat;
import com.rainwood.oa.model.domain.ManagerMain;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/28 9:43
 * @Desc: 系统设置
 */
public interface ILogcatCallbacks extends IBaseCallback {

    /**
     * 获取系统日志
     */
    default void getSystemLogcat(List<Logcat> logcatList){}

    /**
     * 日志类型
     * @param menuList
     */
    default void getMainManagerData(List<ManagerMain> menuList){};

    /**
     * 是否展示部门员工
     * @param stid
     */
    default void getRes4ShowDepart(String stid){}
}
