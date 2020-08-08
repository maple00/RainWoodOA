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
     * @param searchText
     * @param pageCount
     */
    void requestCommunicationData(String searchText, int pageCount);

    /**
     * 管理制度
     * @param title
     */
    void requestManagerSystemData(String title);

    /**
     * 开发文档
     */
    void requestDevDocumentData();

    /**
     * 帮助中心
     * @param searchText
     * @param pageCount
     */
    void requestHelperData(String searchText, int pageCount);

    /**
     * 查看文章详情
     */
    void requestArticleDetailById(String id);
}
