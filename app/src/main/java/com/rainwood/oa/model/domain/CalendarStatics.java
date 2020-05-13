package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * create by a797s in 2020/5/13 14:44
 *
 * @Description : 日历统计信息
 * @Usage : 用于将统计信息对象化
 **/
public final class CalendarStatics implements Serializable {

    /**
     * 统计数据
     */
    private String data;

    /**
     * 统计数据的背景
     * default: 无背景
     */
    private int dataBackground = 0;

    /**
     * 统计数据的字体大小
     * default: 18dp
     */
    private int dataFontSize = 18;

    /**
     * 描述统计信息
     */
    private String descData;

    /**
     * 统计信息备注
     */
    private String note;

    /**
     * 备注的背景
     */
    private int noteBackground;

    /**
     * 备注字体的daxiao
     */
    private int noteFontSize;

    /**
     * 统计信息是否可点击
     */
    private boolean enableClickData;

    /**
     * 统计描述是否可点击
     */
    private boolean enableClickDesc;

    /**
     * 统计描述的图片
     */
    private int descIcon = 0;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getDataBackground() {
        return dataBackground;
    }

    public void setDataBackground(int dataBackground) {
        this.dataBackground = dataBackground;
    }

    public int getDataFontSize() {
        return dataFontSize;
    }

    public void setDataFontSize(int dataFontSize) {
        this.dataFontSize = dataFontSize;
    }

    public String getDescData() {
        return descData;
    }

    public void setDescData(String descData) {
        this.descData = descData;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getNoteBackground() {
        return noteBackground;
    }

    public void setNoteBackground(int noteBackground) {
        this.noteBackground = noteBackground;
    }

    public int getNoteFontSize() {
        return noteFontSize;
    }

    public void setNoteFontSize(int noteFontSize) {
        this.noteFontSize = noteFontSize;
    }

    public boolean isEnableClickData() {
        return enableClickData;
    }

    public void setEnableClickData(boolean enableClickData) {
        this.enableClickData = enableClickData;
    }

    public boolean isEnableClickDesc() {
        return enableClickDesc;
    }

    public void setEnableClickDesc(boolean enableClickDesc) {
        this.enableClickDesc = enableClickDesc;
    }

    public int getDescIcon() {
        return descIcon;
    }

    public void setDescIcon(int descIcon) {
        this.descIcon = descIcon;
    }
}

