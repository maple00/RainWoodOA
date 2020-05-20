package com.rainwood.oa.model.domain;

/**
 * @Author: a797s
 * @Date: 2020/5/19 14:30
 * @Desc: 跟进记录
 */
public final class FollowRecord {

    /**
     * 跟进记录
     */
    private String record;

    /**
     * 跟进时间
     */
    private String time;

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
