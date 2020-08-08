package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/7/27 10:32
 * @Desc: 版本信息
 */
public final class VersionData implements Serializable {

    @Override
    public String toString() {
        return "VersionData{" +
                "url='" + url + '\'' +
                ", version='" + version + '\'' +
                ", time='" + time + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    /**
     * 链接
     */
    private String url;

    /**
     * 版本号
     */
    private String version;

    /**
     * 更新时间
     */
    private String time;

    /**
     * 更新内容
     */
    private String content;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
