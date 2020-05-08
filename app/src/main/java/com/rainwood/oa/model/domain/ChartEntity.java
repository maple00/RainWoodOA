package com.rainwood.oa.model.domain;

/**
 * 图表
 */
public class ChartEntity {

    /**
     * X轴上的值
     */
    private String xLabel;
    /**
     * Y轴上的值
     */
    private Float yValue;

    public ChartEntity(String xLabel, Float yValue) {
        this.xLabel = xLabel;
        this.yValue = yValue;
    }

    @Override
    public String toString() {
        return "ChartEntity{" +
                "xLabel='" + xLabel + '\'' +
                ", yValue=" + yValue +
                '}';
    }

    public String getxLabel() {
        return xLabel;
    }

    public void setxLabel(String xLabel) {
        this.xLabel = xLabel;
    }

    public ChartEntity(Float yValue) {
        this.yValue = yValue;
    }

    public Float getyValue() {
        return yValue;
    }

    public void setyValue(Float yValue) {
        this.yValue = yValue;
    }
}
