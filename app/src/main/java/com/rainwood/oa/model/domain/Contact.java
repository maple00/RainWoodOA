package com.rainwood.oa.model.domain;

/**
 * create by a797s in 2020/5/15 11:22
 *
 * @Description : 联系人
 * @Usage :
 **/
public final class Contact {

    /**
     * 该联系人是否被选择
     */
    private boolean selected;
    /**
     * 姓名
     */
    private String name;

    /**
     * 职位
     */
    private String post;

    /**
     * 手机号码
     */
    private String telNum;

    /**
     * 座机号码
     */
    private String specialPlane;

    /**
     * 微信号
     */
    private String wxNum;

    /**
     * QQ号
     */
    private String qqNum;

    /**
     * 备注
     */
    private String note;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getTelNum() {
        return telNum;
    }

    public void setTelNum(String telNum) {
        this.telNum = telNum;
    }

    public String getSpecialPlane() {
        return specialPlane;
    }

    public void setSpecialPlane(String specialPlane) {
        this.specialPlane = specialPlane;
    }

    public String getWxNum() {
        return wxNum;
    }

    public void setWxNum(String wxNum) {
        this.wxNum = wxNum;
    }

    public String getQqNum() {
        return qqNum;
    }

    public void setQqNum(String qqNum) {
        this.qqNum = qqNum;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "name='" + name + '\'' +
                ", post='" + post + '\'' +
                ", telNum='" + telNum + '\'' +
                ", specialPlane='" + specialPlane + '\'' +
                ", wxNum='" + wxNum + '\'' +
                ", qqNum='" + qqNum + '\'' +
                ", note='" + note + '\'' +
                '}';
    }
}
