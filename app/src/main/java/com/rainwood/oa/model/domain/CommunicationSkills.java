package com.rainwood.oa.model.domain;

/**
 * @Author: a797s
 * @Date: 2020/5/21 11:40
 * @Desc: 沟通技巧
 */
public final class CommunicationSkills {

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 浏览人数
     */
    private String screenNum;

    /**
     * 排名
     */
    private String sort;

    /**
     * 浏览时间
     */
    private String time;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getScreenNum() {
        return screenNum;
    }

    public void setScreenNum(String screenNum) {
        this.screenNum = screenNum;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "CommunicationSkills{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", screenNum='" + screenNum + '\'' +
                ", sort='" + sort + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
