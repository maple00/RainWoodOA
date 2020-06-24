package com.rainwood.oa.presenter.impl;

import com.rainwood.oa.model.domain.Attachment;
import com.rainwood.oa.model.domain.KnowledgeAttach;
import com.rainwood.oa.model.domain.OfficeFile;
import com.rainwood.oa.model.domain.SelectedItem;
import com.rainwood.oa.network.json.JsonParser;
import com.rainwood.oa.network.okhttp.HttpResponse;
import com.rainwood.oa.network.okhttp.OkHttp;
import com.rainwood.oa.network.okhttp.OnHttpListener;
import com.rainwood.oa.network.okhttp.RequestParams;
import com.rainwood.oa.presenter.IAttachmentPresenter;
import com.rainwood.oa.utils.Constants;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.oa.view.IAttachmentCallbacks;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: sxs
 * @Time: 2020/5/16 19:12
 * @Desc: 客户附件
 */
public final class AttachmentImpl implements IAttachmentPresenter, OnHttpListener {

    private IAttachmentCallbacks mAttachmentCallback;

    @Override
    public void getAttachmentData() {

        Map<String, List<Attachment>> attachmentMap = new HashMap<>();
        List<Attachment> attachmentList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Attachment attachment = new Attachment();
            if (i == 0) {
                attachment.setName("客户需求整理.doc");
            } else if (i == 1) {
                attachment.setName("客户需求整理客户需求整理客户需求整理客户需求整理客户需求.pdf");
            } else if (i == 2) {
                attachment.setName("客户需求整理客户需求整理.zip");
            } else {
                attachment.setName("客户公司logo文件.jpg");
            }
            attachment.setStaffName("韩梅梅");
            attachment.setTime("2020.04.10 13:37");
            attachmentList.add(attachment);
        }
        attachmentMap.put("attachment", attachmentList);
        mAttachmentCallback.getAllAttachment(attachmentMap);
    }

    /**
     * 通过客户id查询建客户附件
     *
     * @param customId 客户id
     */
    @Override
    public void requestCustomAttachData(String customId) {
        // 查询客户附件
        RequestParams params = new RequestParams();
        params.add("khid", customId);
        OkHttp.post(Constants.BASE_URL + "cla=client&fun=fileLi", params, this);
    }

    /**
     * 请求办公文件(知识管理)
     */
    @Override
    public void requestOfficeFileData() {
        RequestParams params = new RequestParams();
        OkHttp.post(Constants.BASE_URL + "cla=fileWork&fun=home", params, this);
    }

    /**
     * 办公文件 -- condition
     */
    @Override
    public void requestOfficeCondition() {
        RequestParams params = new RequestParams();
        OkHttp.post(Constants.BASE_URL + "cla=fileWork&fun=search", params, this);
    }

    /**
     * 请求知识管理中的附件列表
     */
    @Override
    public void requestKnowledgeAttach() {
        RequestParams params = new RequestParams();
        OkHttp.post(Constants.BASE_URL + "cla=file&fun=home", params, this);
    }

    /**
     * 知识管理 --附件管理condition
     */
    @Override
    public void requestAttachCondition() {
        RequestParams params = new RequestParams();
        OkHttp.post(Constants.BASE_URL + "cla=file&fun=search", params, this);
    }

    @Override
    public void registerViewCallback(IAttachmentCallbacks callback) {
        this.mAttachmentCallback = callback;
    }

    @Override
    public void unregisterViewCallback(IAttachmentCallbacks callback) {
        mAttachmentCallback = null;
    }

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        LogUtils.d("sxs", "result ---- " + result.body());
        if (!(result.code() == 200)) {
            mAttachmentCallback.onError();
            return;
        }
        try {
            String warn = JsonParser.parseJSONObjectString(result.body()).getString("warn");
            if (!"success".equals(warn)) {
                mAttachmentCallback.onError(warn);
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 客户附件管理
        if (result.url().contains("cla=client&fun=fileLi")) {
            try {
                List<Attachment> attachList = JsonParser.parseJSONArray(Attachment.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("file"));
                if (ListUtils.getSize(attachList) == 0) {
                    mAttachmentCallback.onEmpty();
                    return;
                }
                mAttachmentCallback.getCustomAttachments(attachList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 办公文件
        else if (result.url().contains("cla=fileWork&fun=home")) {
            try {
                List<OfficeFile> officeFileList = JsonParser.parseJSONArray(OfficeFile.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("file"));
                mAttachmentCallback.getOfficeFileData(officeFileList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 办公文件 -- condition
        else if (result.url().contains("cla=fileWork&fun=search")) {
            try {
                JSONArray typeArray = JsonParser.parseJSONArrayString(JsonParser.parseJSONObjectString(
                        JsonParser.parseJSONObjectString(result.body())
                                .getString("search")).getString("type"));
                JSONArray formatArray = JsonParser.parseJSONArrayString(JsonParser.parseJSONObjectString(
                        JsonParser.parseJSONObjectString(result.body())
                                .getString("search")).getString("format"));
                JSONArray secretArray = JsonParser.parseJSONArrayString(JsonParser.parseJSONObjectString(
                        JsonParser.parseJSONObjectString(result.body())
                                .getString("search")).getString("secret"));
                JSONArray sortArray = JsonParser.parseJSONArrayString(JsonParser.parseJSONObjectString(
                        JsonParser.parseJSONObjectString(result.body())
                                .getString("search")).getString("orderBy"));
                List<SelectedItem> typeList = new ArrayList<>();
                List<SelectedItem> formatList = new ArrayList<>();
                List<SelectedItem> secretList = new ArrayList<>();
                List<SelectedItem> sortList = new ArrayList<>();
                for (int i = 0; i < typeArray.length(); i++) {
                    SelectedItem item = new SelectedItem();
                    item.setName(typeArray.getString(i));
                    typeList.add(item);
                }
                for (int i = 0; i < formatArray.length(); i++) {
                    SelectedItem item = new SelectedItem();
                    item.setName(formatArray.getString(i));
                    formatList.add(item);
                }
                for (int i = 0; i < secretArray.length(); i++) {
                    SelectedItem item = new SelectedItem();
                    item.setName(secretArray.getString(i));
                    secretList.add(item);
                }
                for (int i = 0; i < sortArray.length(); i++) {
                    SelectedItem item = new SelectedItem();
                    item.setName(sortArray.getString(i));
                    sortList.add(item);
                }
                mAttachmentCallback.getOfficeCondition(typeList, formatList, secretList, sortList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 知识管理---附件管理
        else if (result.url().contains("cla=file&fun=home")) {
            try {
                List<KnowledgeAttach> attachList = JsonParser.parseJSONArray(KnowledgeAttach.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("file"));
                mAttachmentCallback.getKnowledgeAttach(attachList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 知识管理 -- 附件管理condition
        else if (result.url().contains("cla=file&fun=search")) {
            try {
                JSONArray targetArray = JsonParser.parseJSONArrayString(JsonParser.parseJSONObjectString(
                        JsonParser.parseJSONObjectString(result.body())
                                .getString("search")).getString("target"));
                JSONArray secretArray = JsonParser.parseJSONArrayString(JsonParser.parseJSONObjectString(
                        JsonParser.parseJSONObjectString(result.body())
                                .getString("search")).getString("secret"));
                JSONArray sortArray = JsonParser.parseJSONArrayString(JsonParser.parseJSONObjectString(
                        JsonParser.parseJSONObjectString(result.body())
                                .getString("search")).getString("orderBy"));
                List<SelectedItem> targetList = new ArrayList<>();
                List<SelectedItem> secretList = new ArrayList<>();
                List<SelectedItem> sortList = new ArrayList<>();
                for (int i = 0; i < targetArray.length(); i++) {
                    SelectedItem item = new SelectedItem();
                    item.setName(targetArray.getString(i));
                    targetList.add(item);
                }
                for (int i = 0; i < secretArray.length(); i++) {
                    SelectedItem item = new SelectedItem();
                    item.setName(secretArray.getString(i));
                    secretList.add(item);
                }
                for (int i = 0; i < sortArray.length(); i++) {
                    SelectedItem item = new SelectedItem();
                    item.setName(sortArray.getString(i));
                    sortList.add(item);
                }
                mAttachmentCallback.getAttachCondition(targetList, secretList, sortList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
