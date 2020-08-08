package com.rainwood.oa.model.domain;

/**
 * @Author: a797s
 * @Date: 2020/7/27 9:36
 * @Desc:
 */
public final class PieBean {
    private float Numner;
    private String Name;

    public PieBean() {
    }

    public PieBean(float Numner, String Name) {
        this.Numner = Numner;
        this.Name = Name;
    }

    public float getNumner() {
        return Numner;
    }

    public void setNumner(float numner) {
        Numner = numner;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
