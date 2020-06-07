package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: sxs
 * @Time: 2020/6/7 14:55
 * @Desc: 办公文件
 */
public final class OfficeFile implements Serializable {

    /**
     * 文件分类
     */
    private String type;

    /**
     * 文件名称
     */
    private String name;

    /**
     * 文件地址
     */
    private String src;

    /**
     * 是否保密
     */
    private String secret;

    /**
     * 文件格式
     */
    private String format;

    /**
     * 文件大小
     */
    private String size;

    /**
     * 文件更新时间
     */
    private String updateTime;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
