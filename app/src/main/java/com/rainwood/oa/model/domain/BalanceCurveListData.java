package com.rainwood.oa.model.domain;

/**
 * @Author: a797s
 * @Date: 2020/6/29 11:15
 * @Desc: 收支曲线统计列表
 */
public final class BalanceCurveListData {

    /**
     * 月份
     */
    private String month;

    /**
     * 收入
     */
    private String income;

    /**
     * 支出
     */
    private String outcome;

    /**
     * 工资
     */
    private String salary;

    /**
     * 结算
     */
    private String settlement;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getSettlement() {
        return settlement;
    }

    public void setSettlement(String settlement) {
        this.settlement = settlement;
    }

    @Override
    public String toString() {
        return "BalanceCurveListData{" +
                "month='" + month + '\'' +
                ", income='" + income + '\'' +
                ", outcome='" + outcome + '\'' +
                ", salary='" + salary + '\'' +
                ", settlement='" + settlement + '\'' +
                '}';
    }
}
