package com.gov.gows.entity;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/** 政策法规实体（对应表 gov_policy）。 */
@Data
public class Policy {
    private Long id;
    private String title;
    private String content;
    private String summary;
    private String docNo;
    private String publishOrg;
    private LocalDate publishDate;
    private LocalDate effectiveDate;
    private Long categoryId;
    private String attachment;
    private Integer views;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
