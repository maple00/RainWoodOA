package com.rainwood.oa.model.domain;

/**
 * @Author: a797s
 * @Date: 2020/5/28 9:48
 * @Desc: 系统日志
 */
public final class SystemLogcat {

    /**
     * 姓名
     */
    private String name;

    /**
     * 内容
     */
    private String logcat;

    /**
     * 来源
     */
    private String origin;

    /**
     * 时间
     */
    private String time;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogcat() {
        return logcat;
    }

    public void setLogcat(String logcat) {
        this.logcat = logcat;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
