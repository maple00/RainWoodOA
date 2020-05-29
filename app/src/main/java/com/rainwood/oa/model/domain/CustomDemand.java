package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/5/28 10:42
 * @Desc: 客户需求
 */
public final class CustomDemand implements Serializable {

    /**
     * id
     */
    private String id;

    /**
     * 意向状态
     */
    private String workFlow;

    /**
     * 客户来源
     */
    private String source;

    /**
     * 预算
     */
    private String budget;

    /**
     * 所在行业
     */
    private String industry;

    /**
     * 需求描述
     */
    private String text;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWorkFlow() {
        return workFlow;
    }

    public void setWorkFlow(String workFlow) {
        this.workFlow = workFlow;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "CustomDemand{" +
                "id='" + id + '\'' +
                ", workFlow='" + workFlow + '\'' +
                ", source='" + source + '\'' +
                ", budget='" + budget + '\'' +
                ", industry='" + industry + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
