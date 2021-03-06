package com.rainwood.oa.view;

import com.rainwood.oa.base.IBaseCallback;
import com.rainwood.oa.model.domain.Attachment;
import com.rainwood.oa.model.domain.KnowledgeAttach;
import com.rainwood.oa.model.domain.OfficeFile;
import com.rainwood.oa.model.domain.SelectedItem;

import java.util.List;
import java.util.Map;

/**
 * @Author: sxs
 * @Time: 2020/5/16 19:12
 * @Desc: 客户附件
 */
public interface IAttachmentCallbacks extends IBaseCallback {

    /**
     * 获取所有的附件数据
     * @param attachMap
     */
    default void getAllAttachment(Map attachMap){};

    /**
     * 客户下的附件
     */
    default void getCustomAttachments(List<Attachment> attachmentList){};

    /**
     * 办公文件
     * @param fileList
     */
    default void getOfficeFileData(List<OfficeFile> fileList){}

    /**
     * 办公文件 -- condition
     */
    default void getOfficeCondition(List<SelectedItem> typeList, List<SelectedItem> formatList,
                                    List<SelectedItem> secretList, List<SelectedItem> sortList){}

    /**
     * 附件管理（知识管理）
     */
    default void getKnowledgeAttach(List<KnowledgeAttach> attachList){}

    /**
     * 附件管理 -- condition
     */
    default void getAttachCondition(List<SelectedItem> targetList,
                                    List<SelectedItem> secretList, List<SelectedItem> sortList){}
}
