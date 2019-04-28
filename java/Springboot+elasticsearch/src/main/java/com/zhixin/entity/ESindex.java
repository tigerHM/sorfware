package com.zhixin.entity;


import java.io.Serializable;

public class ESindex implements Serializable {

    private String index;

    private String type;

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ESindex(String index, String type) {
        this.index = index;
        this.type = type;
    }
}
