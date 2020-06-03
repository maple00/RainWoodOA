package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/6/3 16:46
 * @Desc: 组织结构中得员工信息
 */
public final class StaffStructure implements Serializable {

    /**
     * 姓名
     */
    private String name;

    /**
     * 头像
     */
    private String headPhoto;

    /**
     * 职位
     */
    private String post;

    /**
     * 电话
     */
    private String tel;

    /**
     * qq
     */
    private String qq;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadPhoto() {
        return headPhoto;
    }

    public void setHeadPhoto(String headPhoto) {
        this.headPhoto = headPhoto;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }
}
