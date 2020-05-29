package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * create by a797s in 2020/5/15 11:22
 *
 * @Description : 联系人
 * @Usage :
 * <p>
 * POST：id联系人主键，khid新建时需要客户ID。
 * position    职位
 * name      姓名
 * tel        手机
 * phone     座机
 * qq        QQ号码
 * WeChat    微信号
 * list        排序号
 * text       备注
 **/
public final class Contact implements Serializable {

    /**
     * 联系人id
     */
    private String id;
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
    private String position;

    /**
     * 手机号码
     */
    private String tel;

    /**
     * 座机号码
     */
    private String phone;

    /**
     * 微信号
     */
    private String WeChat;

    /**
     * QQ号
     */
    private String qq;

    /**
     * 备注
     */
    private String text;

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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWeChat() {
        return WeChat;
    }

    public void setWeChat(String weChat) {
        this.WeChat = weChat;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id='" + id + '\'' +
                ", selected=" + selected +
                ", name='" + name + '\'' +
                ", position='" + position + '\'' +
                ", tel='" + tel + '\'' +
                ", phone='" + phone + '\'' +
                ", WeChat='" + WeChat + '\'' +
                ", qq='" + qq + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
