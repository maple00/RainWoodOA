package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/6/17 16:58
 * @Desc: 待办事项
 */
public final class BlockLog implements Serializable {

    /**
     * 事件id
     */
    private String id;

    /**
     * 事件内容
     */
    private String content;

    /**
     * 事件时间
     */
    private String time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
