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
     * 获取客户附件数据
     */
    void getAttachmentData();

    /**
     * 通过客户id查询客户附件
     * @param customId 客户id
     */
    void requestCustomAttachData(String customId);
}
