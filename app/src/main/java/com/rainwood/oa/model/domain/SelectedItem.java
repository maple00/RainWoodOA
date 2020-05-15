package com.rainwood.oa.model.domain;

/**
 * create by a797s in 2020/5/14 17:09
 *
 * @Description : 选中的item
 * @Usage :
 **/
public final class SelectedItem {

    private String data;
    private boolean hasSelected;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isHasSelected() {
        return hasSelected;
    }

    public void setHasSelected(boolean hasSelected) {
        this.hasSelected = hasSelected;
    }
}
