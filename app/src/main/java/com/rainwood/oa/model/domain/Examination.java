package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/5/19 16:27
 * @Desc: 审批
 */
public final class Examination implements Serializable {

    /**
     * 头像
     */
    private String headPhoto;

    /**
     * 姓名
     */
    private String name;

    /**
     * 部门
     */
    private String depart;

    /**
     * 职位
     */
    private String post;

    public String getHeadPhoto() {
        return headPhoto;
    }

    public void setHeadPhoto(String headPhoto) {
        this.headPhoto = headPhoto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepart() {
        return depart;
    }

    public void setDepart(String depart) {
        this.depart = depart;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    @Override
    public String toString() {
        return "Examination{" +
                "headPhoto='" + headPhoto + '\'' +
                ", name='" + name + '\'' +
                ", depart='" + depart + '\'' +
                ", post='" + post + '\'' +
                '}';
    }
}
