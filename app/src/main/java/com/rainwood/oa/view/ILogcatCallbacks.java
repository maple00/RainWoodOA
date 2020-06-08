package com.rainwood.oa.view;

import com.rainwood.oa.base.IBaseCallback;
import com.rainwood.oa.model.domain.Logcat;

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
}
