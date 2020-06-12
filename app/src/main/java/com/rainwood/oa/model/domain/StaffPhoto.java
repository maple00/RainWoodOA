package com.rainwood.oa.model.domain;

/**
 * @Author: a797s
 * @Date: 2020/5/25 10:40
 * @Desc: 员工照片信息
 */
public final class StaffPhoto {

    /**
     * 照片来源
     */
    private String origin;

    /**
     * 照片描述
     */
    private String desc;

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
