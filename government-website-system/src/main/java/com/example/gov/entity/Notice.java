package com.example.gov.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 公告实体（对应表 gov_notice）。
 */
@Data
public class Notice {
    private Long id;
    private String title;
    private String content;
    private String summary;
    private String cover;
    private String author;
    private String source;
    private Integer views;
    private Integer sort;
    private Integer isTop;
    private Integer isImportant;
    /**
     * 0草稿 1已发布 2已下架
     */
    private Integer status;
    private LocalDateTime publishTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
