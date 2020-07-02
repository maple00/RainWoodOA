package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/6/29 14:52
 * @Desc: 员工数量曲线
 */
public final class StaffCurveItem implements Serializable {

    /**
     * 数量
     */
    private float num;

    public float getNum() {
        return num;
    }

    public void setNum(float num) {
        this.num = num;
    }
}
