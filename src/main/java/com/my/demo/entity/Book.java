package com.my.demo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author yufan.yang
 * @since 2020-12-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Book implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    private Long id;

    @TableField("createTime")
    private Date createtime;

    @TableField("updateTime")
    private Date updatetime;

    private Long deleted;

    private String name;

    private String url;

    private String author;

    private String size;

    private String lntroduction;

    @TableField("lastUpdateTime")
    private String lastupdatetime;

    @TableField("latestChapter")
    private String latestchapter;

    private String status;

    private String type;

    private Integer source;


}
