package com.rainwood.oa.model.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/29 14:52
 * @Desc: 员工数曲线
 */
public final class StaffCurve implements Serializable {

    /**
     * 曲线名称
     */
    private String name;

    private List<StaffCurveItem> data;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<StaffCurveItem> getData() {
        return data;
    }

    public void setData(List<StaffCurveItem> data) {
        this.data = data;
    }
}
