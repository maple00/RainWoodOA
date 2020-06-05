package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/6/5 10:49
 * @Desc: 团队基金
 */
public final class TeamFunds implements Serializable {

    /**
     * 事由内容
     */
    private String content;

    /**
     * 金额
     */
    private String money;

    /**
     * 发生时间
     */
    private String time;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
