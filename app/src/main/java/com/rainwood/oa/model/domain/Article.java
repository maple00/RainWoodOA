package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/5/21 11:40
 * @Desc: 文章----沟通技巧、管理制度、开发文档、帮助中心
 */
public final class Article implements Serializable {

    /**
     * id
     */
    private String id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String word;

    /**
     * 详情内容
     */
    private String article;

    /**
     * 浏览人数
     */
    private String clickRate;

    /**
     * 排名
     */
    private String sort;

    /**
     * 浏览时间
     */
    private String updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getClickRate() {
        return clickRate;
    }

    public void setClickRate(String clickRate) {
        this.clickRate = clickRate;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "CommunicationSkills{" +
                "title='" + title + '\'' +
                ", content='" + word + '\'' +
                ", screenNum='" + clickRate + '\'' +
                ", sort='" + sort + '\'' +
                ", time='" + updateTime + '\'' +
                '}';
    }
}
