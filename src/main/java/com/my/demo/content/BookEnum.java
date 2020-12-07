package com.my.demo.content;

/**
 * @author 杨宇帆
 * @create 2020-12-07
 */
public enum BookEnum {
    qishu(1,"http://www.uidzhx.com/Shtml10376.html"),
    unknown(0,"");
    private Integer source;
    private String baseUrl;

    BookEnum() {
    }

    BookEnum(Integer source, String baseUrl) {
        this.source = source;
        this.baseUrl = baseUrl;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }}
