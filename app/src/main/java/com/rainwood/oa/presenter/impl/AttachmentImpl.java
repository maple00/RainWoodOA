package com.rainwood.oa.presenter.impl;

import com.rainwood.oa.model.domain.Attachment;
import com.rainwood.oa.presenter.IAttachmentPresenter;
import com.rainwood.oa.view.IAttachmentCallbacks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: sxs
 * @Time: 2020/5/16 19:12
 * @Desc: 客户附件
 */
public final class AttachmentImpl implements IAttachmentPresenter {

    private IAttachmentCallbacks mAttachmentCallback;

    @Override
    public void getAttachmentData() {

        Map<String, List<Attachment>> attachmentMap = new HashMap<>();
        List<Attachment> attachmentList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Attachment attachment = new Attachment();
            if (i == 0) {
                attachment.setAttachmentName("客户需求整理.doc");
            } else if (i == 1) {
                attachment.setAttachmentName("客户需求整理客户需求整理客户需求整理客户需求整理客户需求.pdf");
            } else if (i == 2) {
                attachment.setAttachmentName("客户需求整理客户需求整理.zip");
            } else {
                attachment.setAttachmentName("客户公司logo文件.jpg");
            }
            attachment.setCustomName("韩梅梅");
            attachment.setTime("2020.04.10 13:37");
            attachmentList.add(attachment);
        }
        attachmentMap.put("attachment", attachmentList);
        mAttachmentCallback.getAllAttachment(attachmentMap);
    }

    @Override
    public void registerViewCallback(IAttachmentCallbacks callback) {
        this.mAttachmentCallback = callback;
    }

    @Override
    public void unregisterViewCallback(IAttachmentCallbacks callback) {
        mAttachmentCallback = null;
    }
}
