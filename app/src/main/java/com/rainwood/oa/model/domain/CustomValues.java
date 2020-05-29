package com.rainwood.oa.model.domain;

/**
 * @Author: a797s
 * @Date: 2020/5/28 10:39
 * @Desc: 客户详情中的属性
 */
public final class CustomValues {

    /**
     * id
     */
    private String id;

    /**
     * 头像
     */
    private String ico;

    /**
     * 姓名
     */
    private String name;

    /**
     * 工作职位
     */
    private String job;

    /**
     * 直属上级
     */
    private String manager;

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIco() {
        return ico;
    }

    public void setIco(String ico) {
        this.ico = ico;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }
}
