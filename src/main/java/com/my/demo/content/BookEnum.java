package com.my.demo.content;

/**
 * @author 杨宇帆
 * @create 2020-12-07
 */
public enum BookEnum {
    qishu(1,"http://www.qishus.com/txt/77746.html","http://www.qishus.com/txt/[0-9]{4,7}.html"),
    EightZero(2,"https://www.txt80.com/all/","https://www.txt80.com/all/index_[0-9]{1,4}.html")
    ;
    private Integer source;
    private String baseUrl;
    private String likeUrl;

    BookEnum(Integer source, String baseUrl, String likeUrl) {
        this.source = source;
        this.baseUrl = baseUrl;
        this.likeUrl = likeUrl;
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
    }

    public String getLikeUrl() {
        return likeUrl;
    }

    public void setLikeUrl(String likeUrl) {
        this.likeUrl = likeUrl;
    }}
