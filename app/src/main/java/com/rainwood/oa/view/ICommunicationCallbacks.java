package com.rainwood.oa.view;

import com.rainwood.oa.base.IBaseCallback;
import com.rainwood.oa.model.domain.Article;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/21 11:48
 * @Desc: 沟通技巧模块
 */
public interface ICommunicationCallbacks extends IBaseCallback {

    /**
     * 获取沟通技巧所有得内容
     * @param dataList
     */
    void getAllData(List<Article> dataList);
}
