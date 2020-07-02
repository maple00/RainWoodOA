package com.rainwood.oa.presenter;

import com.rainwood.oa.base.IBasePresenter;
import com.rainwood.oa.view.IAttachmentCallbacks;

/**
 * @Author: sxs
 * @Time: 2020/5/16 19:11
 * @Desc: 附件
 */
public interface IAttachmentPresenter extends IBasePresenter<IAttachmentCallbacks> {

    /**
     * 通过客户id查询客户附件
     *
     * @param customId 客户id
     */
    void requestCustomAttachData(String customId);

    /**
     * 知识管理 ---  办公文件
     * @param  searchText 搜索内容
     * @param classify 分类
     * @param format   格式
     * @param secret   保密状态
     * @param sorting  默认排序
     */
    void requestOfficeFileData(String searchText, String classify, String format, String secret, String sorting, int page);

    /**
     * 办公文件 -- condition
     */
    void requestOfficeCondition();

    /**
     * 知识管理 --- 附件管理
     * @param attachName 附件名称
     * @param staffId 上传者
     * @param secret 保密状态
     * @param target 对象类型
     * @param sorting 排序方式
     * @param page 页码
     */
    void requestKnowledgeAttach(String attachName, String staffId, String secret, String target, String sorting, int page);

    /**
     * 附件管理 -- condition
     */
    void requestAttachCondition();
}
