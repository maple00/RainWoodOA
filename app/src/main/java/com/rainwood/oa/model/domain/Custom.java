package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * create by a797s in 2020/5/15 13:06
 *
 * @Description : 客户
 * @Usage :
 **/
public final class Custom implements Serializable {

    /**
     * 客户id
     */
    private String id;

    /**
     * 客户负责人名字
     */
    private String name;

    /**
     * 客户公司名称
     */
    private String company;

    /**
     * 签约状态
     */
    private String status;

    /**
     * 客户来源
     */
    private String origin;

    /**
     * 客户所属行业
     */
    private String trade;

    /**
     * 客户预算
     */
    private int budget;

    /**
     * 客户需求大概描述
     */
    private String demand;

    /**
     * 选中
     */
    private boolean selected;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getTrade() {
        return trade;
    }

    public void setTrade(String trade) {
        this.trade = trade;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public String getDemand() {
        return demand;
    }

    public void setDemand(String demand) {
        this.demand = demand;
    }

    @Override
    public String toString() {
        return "Custom{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", company='" + company + '\'' +
                ", status='" + status + '\'' +
                ", origin='" + origin + '\'' +
                ", trade='" + trade + '\'' +
                ", budget=" + budget +
                ", demand='" + demand + '\'' +
                ", selected=" + selected +
                '}';
    }
}
