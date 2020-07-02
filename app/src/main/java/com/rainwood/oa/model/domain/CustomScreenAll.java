package com.rainwood.oa.model.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/7/1 17:51
 * @Desc: 客户列表 全部筛选
 */
public final class CustomScreenAll implements Serializable {

    /**
     * 标题
     */
    private String title;

    /**
     * 关键字
     */
    private String key;

    private List<InnerCustomScreen> array;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<InnerCustomScreen> getArray() {
        return array;
    }

    public void setArray(List<InnerCustomScreen> array) {
        this.array = array;
    }

    private static final class InnerCustomScreen implements Serializable {
        /**
         * 员工id
         */
        private String stid;

        /**
         * value
         */
        private String name;

        public String getStid() {
            return stid;
        }

        public void setStid(String stid) {
            this.stid = stid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
