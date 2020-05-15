package com.rainwood.oa.model.domain;

/**
 * create by a797s in 2020/5/15 10:58
 *
 * @Description : 协作人
 * @Usage :
 **/
public final class Associates {

    /**
     * 头像
     */
    private String headSrc;

    /**
     * 名字
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

    public String getHeadSrc() {
        return headSrc;
    }

    public void setHeadSrc(String headSrc) {
        this.headSrc = headSrc;
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
}
