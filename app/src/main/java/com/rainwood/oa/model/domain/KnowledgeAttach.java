package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: sxs
 * @Time: 2020/6/7 16:38
 * @Desc: 知识管理--附件管理
 */
public final class KnowledgeAttach implements Serializable {

    /**
     * 附件名称
     */
    private String name;

    /**
     * 上传人
     */
    private String staffName;

    /**
     * 上传对象
     */
    private String target;

    /**
     * 对象id
     */
    private String targetId;

    /**
     * 对象名称
     */
    private String targetName;

    /**
     * 文件大小
     */
    private String size;

    /**
     * 文件格式
     */
    private String format;

    /**
     * 是否保密
     */
    private String secret;

    /**
     * 附件地址
     */
    private String src;

    /**
     * 上传时间
     */
    private String time;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

