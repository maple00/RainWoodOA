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
    private String attachmentName;

    /**
     * 客户名称
     */
    private String customName;

    /**
     * 附件时间
     */
    private String time;

    /**
     * 附件选中
     */
    private boolean selected;

    public String getAttachmentName() {
        return attachmentName;
    }

    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return "Attachment{" +
                "attachmentName='" + attachmentName + '\'' +
                ", customName='" + customName + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
