package com.rainwood.oa.presenter.impl;

import com.rainwood.oa.model.domain.Article;
import com.rainwood.oa.model.domain.Style;
import com.rainwood.oa.network.json.JsonParser;
import com.rainwood.oa.network.okhttp.HttpResponse;
import com.rainwood.oa.network.okhttp.OkHttp;
import com.rainwood.oa.network.okhttp.OnHttpListener;
import com.rainwood.oa.network.okhttp.RequestParams;
import com.rainwood.oa.presenter.IArticlePresenter;
import com.rainwood.oa.utils.Constants;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.oa.view.IArticleCallbacks;

import org.json.JSONException;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/5 18:13
 * @Desc: 文章管理逻辑类
 */
public final class ArticleImpl implements IArticlePresenter, OnHttpListener {

    private IArticleCallbacks mArticleCallbacks;

    /**
     * 沟通技巧
     *
     * @param searchText
     * @param pageCount
     */
    @Override
    public void requestCommunicationData(String searchText, int pageCount) {
        RequestParams params = new RequestParams();
        params.add("life", Constants.life);
        params.add("title", searchText);
        OkHttp.post(Constants.BASE_URL + "cla=article&fun=clientTalk&page=" + pageCount, params, this);
    }

    /**
     * 管理制度
     *
     * @param title
     */
    @Override
    public void requestManagerSystemData(String title) {
        RequestParams params = new RequestParams();
        params.add("life", Constants.life);
        params.add("title", title);
        OkHttp.post(Constants.BASE_URL + "cla=article&fun=adSystem", params, this);
    }

    /**
     * 开发文档
     */
    @Override
    public void requestDevDocumentData() {

    }

    /**
     * 帮助中心
     *
     * @param searchText
     * @param pageCount
     */
    @Override
    public void requestHelperData(String searchText, int pageCount) {
        RequestParams params = new RequestParams();
        params.add("life", Constants.life);
        params.add("title", searchText);
        OkHttp.post(Constants.BASE_URL + "cla=article&fun=adHelp&page=" + pageCount, params, this);
    }

    /**
     * 查看文章详情
     *
     * @param id
     */
    @Override
    public void requestArticleDetailById(String id) {
        RequestParams params = new RequestParams();
        params.add("life", Constants.life);
        params.add("id", id);
        OkHttp.post(Constants.BASE_URL + "cla=article&fun=detail", params, this);
    }

    @Override
    public void registerViewCallback(IArticleCallbacks callback) {
        mArticleCallbacks = callback;
    }

    @Override
    public void unregisterViewCallback(IArticleCallbacks callback) {
        mArticleCallbacks = null;
    }

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        LogUtils.d("sxs", "result ---- " + result.body());
        if (!(result.code() == 200)) {
            mArticleCallbacks.onError();
            return;
        }
        try {
            String warn = JsonParser.parseJSONObjectString(result.body()).getString("warn");
            if (!"success".equals(warn)) {
                mArticleCallbacks.onError(warn);
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 沟通技巧
        if (result.url().contains("cla=article&fun=clientTalk")) {
            try {
                List<Article> skillList = JsonParser.parseJSONArray(Article.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("article"));
                mArticleCallbacks.getCommunicationData(skillList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 管理制度
        else if (result.url().contains("cla=article&fun=adSystem")) {
            try {
                List<Article> skillList = JsonParser.parseJSONArray(Article.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("article"));
                mArticleCallbacks.getManagerSystemData(skillList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 帮助中心
        else if (result.url().contains("cla=article&fun=adHelp")) {
            try {
                List<Article> skillList = JsonParser.parseJSONArray(Article.class,
                        JsonParser.parseJSONObjectString(result.body()).getString("article"));
                mArticleCallbacks.getHelperData(skillList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // 文章详情
        else if (result.url().contains("cla=article&fun=detail")) {
            try {
                Article article = JsonParser.parseJSONObject(Article.class, JsonParser.parseJSONObjectString(result.body()).getString("article"));
                Style style = JsonParser.parseJSONObject(Style.class, JsonParser.parseJSONObjectString(result.body()).getString("style"));
                mArticleCallbacks.getArticleDetail(article, style);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }
}
