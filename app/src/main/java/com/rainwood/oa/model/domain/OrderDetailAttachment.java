package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/6/4 10:01
 * @Desc: 订单详情附件
 */
public final class OrderDetailAttachment implements Serializable {

    /**
     * 上传人员
     */
    private String staffName;

    /**
     * 附件名称
     */
    private String name;

    /**
     * 附件路径
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
     * 大小
     */
    private String size;

    /**
     * 上传时间
     */
    private String time;

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
