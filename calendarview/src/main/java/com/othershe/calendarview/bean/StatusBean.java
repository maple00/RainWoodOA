package com.othershe.calendarview.bean;

import java.io.Serializable;

/**
 * create by a797s in 2020/5/11 17:45
 * 
 * @Description : 状态描述
 * @Usage：
 **/
public final class StatusBean implements Serializable {

    private String status;
    private int color;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
