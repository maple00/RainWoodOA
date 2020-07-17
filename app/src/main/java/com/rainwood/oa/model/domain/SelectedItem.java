package com.rainwood.oa.model.domain;

import java.io.Serializable;

/**
 * create by a797s in 2020/5/14 17:09
 *
 * @Description : 选中的item
 * @Usage :
 **/
public final class SelectedItem implements Serializable {

    private String id;
    private String name;
    private boolean hasSelected;

    public SelectedItem() {
    }

    public SelectedItem(String name) {
        this.name = name;
    }

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

    public boolean isHasSelected() {
        return hasSelected;
    }

    public void setHasSelected(boolean hasSelected) {
        this.hasSelected = hasSelected;
    }

    @Override
    public String toString() {
        return "SelectedItem{" +
                "name='" + name + '\'' +
                ", hasSelected=" + hasSelected +
                '}';
    }
}
