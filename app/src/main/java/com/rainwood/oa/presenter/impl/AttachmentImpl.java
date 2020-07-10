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
import java.util.List;

/**
 * @Author: sxs
 * @Time: 2020/5/16 19:12
 * @Desc: 客户附件
 */
public final class AttachmentImpl implements IAttachmentPresenter, OnHttpListener {

    private IAttachmentCallbacks mAttachmentCallback;

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
     * 知识管理 -- 办公文件
     *
     * @param searchText 搜索内容
     * @param classify   分类
     * @param format     格式
     * @param secret     保密状态
     * @param sorting    默认排序
     */
    @Override
    public void requestOfficeFileData(String searchText, String classify, String format, String secret, String sorting, int page) {
        RequestParams params = new RequestParams();
        params.add("name", searchText);
        params.add("type", classify);
        params.add("format", format);
        params.add("secret", secret);
        params.add("orderBy", sorting);
        OkHttp.post(Constants.BASE_URL + "cla=fileWork&fun=home&page=" + page, params, this);
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
     * 知识管理 --- 附件列表
     *
     * @param attachName 附件名称
     * @param staffId    上传者
     * @param secret     保密状态
     * @param target     对象类型
     * @param sorting    排序方式
     * @param page       页码
     */
    @Override
    public void requestKnowledgeAttach(String attachName, String staffId, String secret, String target, String sorting, int page) {
        RequestParams params = new RequestParams();
        params.add("name", attachName);
        params.add("stid", staffId);
        params.add("secret", secret);
        params.add("target", target);
        params.add("orderBy", sorting);
        OkHttp.post(Constants.BASE_URL + "cla=file&fun=home&page=" + page, params, this);
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
                List<String> typeArray = JsonParser.parseJSONList(JsonParser.parseJSONObjectString(
                        JsonParser.parseJSONObjectString(result.body())
                                .getString("search")).getString("type"));
                List<String> formatArray = JsonParser.parseJSONList(JsonParser.parseJSONObjectString(
                        JsonParser.parseJSONObjectString(result.body())
                                .getString("search")).getString("format"));
                List<String> secretArray = JsonParser.parseJSONList(JsonParser.parseJSONObjectString(
                        JsonParser.parseJSONObjectString(result.body())
                                .getString("search")).getString("secret"));
                List<String> sortArray = JsonParser.parseJSONList(JsonParser.parseJSONObjectString(
                        JsonParser.parseJSONObjectString(result.body())
                                .getString("search")).getString("orderBy"));
                List<SelectedItem> typeList = new ArrayList<>();
                List<SelectedItem> formatList = new ArrayList<>();
                List<SelectedItem> secretList = new ArrayList<>();
                List<SelectedItem> sortList = new ArrayList<>();
                for (int i = 0; i < ListUtils.getSize(typeArray); i++) {
                    SelectedItem item = new SelectedItem();
                    item.setName(typeArray.get(i));
                    typeList.add(item);
                }
                for (int i = 0; i < ListUtils.getSize(formatArray); i++) {
                    SelectedItem item = new SelectedItem();
                    item.setName(formatArray.get(i));
                    formatList.add(item);
                }
                for (int i = 0; i < ListUtils.getSize(secretArray); i++) {
                    SelectedItem item = new SelectedItem();
                    item.setName(secretArray.get(i));
                    secretList.add(item);
                }
                for (int i = 0; i < ListUtils.getSize(sortArray); i++) {
                    SelectedItem item = new SelectedItem();
                    item.setName(sortArray.get(i));
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
                List<String> targetArray = JsonParser.parseJSONList(JsonParser.parseJSONObjectString(
                        JsonParser.parseJSONObjectString(result.body())
                                .getString("search")).getString("target"));
                List<String> secretArray = JsonParser.parseJSONList(JsonParser.parseJSONObjectString(
                        JsonParser.parseJSONObjectString(result.body())
                                .getString("search")).getString("secret"));
                List<String> sortArray = JsonParser.parseJSONList(JsonParser.parseJSONObjectString(
                        JsonParser.parseJSONObjectString(result.body())
                                .getString("search")).getString("orderBy"));
                List<SelectedItem> targetList = new ArrayList<>();
                List<SelectedItem> secretList = new ArrayList<>();
                List<SelectedItem> sortList = new ArrayList<>();
                for (int i = 0; i < ListUtils.getSize(targetArray); i++) {
                    SelectedItem item = new SelectedItem();
                    item.setName(targetArray.get(i));
                    targetList.add(item);
                }
                for (int i = 0; i < ListUtils.getSize(secretArray); i++) {
                    SelectedItem item = new SelectedItem();
                    item.setName(secretArray.get(i));
                    secretList.add(item);
                }
                for (int i = 0; i < ListUtils.getSize(sortArray); i++) {
                    SelectedItem item = new SelectedItem();
                    item.setName(sortArray.get(i));
                    sortList.add(item);
                }
                mAttachmentCallback.getAttachCondition(targetList, secretList, sortList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
