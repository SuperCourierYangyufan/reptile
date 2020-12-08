package com.my.demo.content;

/**
 * @author 杨宇帆
 * @create 2020-12-08
 */
public enum StatusEnum {
    unknown("0","未知"),
    ;
    StatusEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    private String code;
    private String name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }}
