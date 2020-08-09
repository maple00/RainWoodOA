package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: sxs
 * @Time: 2020/8/8 16:11
 * @Desc: 登录数据
 */
public final class LoginData implements Serializable {
    @Override
    public String toString() {
        return "LoginData{" +
                "userName='" + userName + '\'' +
                ", pwd='" + pwd + '\'' +
                ", life='" + life + '\'' +
                '}';
    }

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String pwd;

    /**
     * 会话时长
     */
    private String life;

  /*  public LoginData(String userName, String pwd, String life) {
        this.userName = userName;
        this.pwd = pwd;
        this.life = life;
    }*/

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getLife() {
        return life;
    }

    public void setLife(String life) {
        this.life = life;
    }
}
