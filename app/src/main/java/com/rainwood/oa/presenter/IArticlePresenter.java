package com.rainwood.oa.presenter;

import com.rainwood.oa.base.IBasePresenter;
import com.rainwood.oa.view.IArticleCallbacks;

/**
 * @Author: a797s
 * @Date: 2020/6/5 18:12
 * @Desc: 文章管理 ---沟通技巧、管理制度、开发文档、帮助中心
 */
public interface IArticlePresenter extends IBasePresenter<IArticleCallbacks> {

    /**
     * 沟通技巧
     */
    void requestCommunicationData();

    /**
     * 管理制度
     */
    void requestManagerSystemData();

    /**
     * 开发文档
     */
    void requestDevDocumentData();

    /**
     * 帮助中心
     */
    void requestHelperData();

    /**
     * 查看文章详情
     */
    void requestArticleDetailById(String id);
}
