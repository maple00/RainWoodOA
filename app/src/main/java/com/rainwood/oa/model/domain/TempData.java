package com.rainwood.oa.model.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/6/3 18:26
 * @Desc: 临时对象
 */
public final class TempData implements Serializable {

    private Map<String,String> tempMap;

    public Map<String, String> getTempMap() {
        return tempMap;
    }

    public void setTempMap(Map<String, String> tempMap) {
        this.tempMap = tempMap;
    }
}
