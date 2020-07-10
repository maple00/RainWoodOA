package com.rainwood.oa.model.domain;

import java.util.List;

/**
 * @author Relin
 * @describe
 * @date 2020/7/7
 **/
public class CalendarBody {

    private String moon;
    private List<String> day;
    private String  text;
    private String warn;

    public String getMoon() {
        return moon;
    }

    public void setMoon(String moon) {
        this.moon = moon;
    }

    public List<String> getDay() {
        return day;
    }

    public void setDay(List<String> day) {
        this.day = day;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getWarn() {
        return warn;
    }

    public void setWarn(String warn) {
        this.warn = warn;
    }
}
