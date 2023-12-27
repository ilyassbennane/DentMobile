package com.bennane.jaii.entities;

public class Group {
    private  long id;
    private  String code;

    public Group() {
    }

    public Group(long id, String code) {
        this.id = id;
        this.code = code;
    }

    public long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }
    @Override
    public String toString() {
        return code;
    }

    public void setId(long id) {
        this.id = id;
    }
}
