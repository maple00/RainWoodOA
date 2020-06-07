package com.rainwood.oa.view;

import com.rainwood.oa.base.IBaseCallback;
import com.rainwood.oa.model.domain.Article;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/5 18:09
 * @Desc: 文章管理
 */
public interface IArticleCallbacks extends IBaseCallback {

    /**
     * 沟通技巧
     */
    default void getCommunicationData(List<Article> skillsList) {
    }

    /**
     * 管理制度
     */
    default void getManagerSystemData(List<Article> managerSysList) {
    }

    /**
     * 开发文档
     */
    default void getDevDocumentData() {
    }

    /**
     * 帮助中心
     */
    default void getHelperData(List<Article> helperList) {
    }

    /**
     * 文章详情
     */
    default void getArticleDetail(Article article){}

}
