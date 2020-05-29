package com.rainwood.oa.model.domain;

/**
 * @Author: sxs
 * @Time: 2020/5/16 19:13
 * @Desc: 附件
 */
public final class Attachment {
    /**
     * 附件名称
     */
    private String name;

    /**
     * 客户名称
     */
    private String staffName;

    /**
     * 附件时间
     */
    private String time;

    /**
     * 是否保密
     */
    private String secret;

    /**
     * 文件大小
     */
    private String size;
    /**
     * 文件格式
     */
    private String format;

    /**
     * 附件地址
     */
    private String src;

    /**
     * 附件选中
     */
    private boolean selected;

    @Override
    public String toString() {
        return "Attachment{" +
                "name='" + name + '\'' +
                ", staffName='" + staffName + '\'' +
                ", time='" + time + '\'' +
                ", secret='" + secret + '\'' +
                ", size='" + size + '\'' +
                ", format='" + format + '\'' +
                ", src='" + src + '\'' +
                ", selected=" + selected +
                '}';
    }

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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
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

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
