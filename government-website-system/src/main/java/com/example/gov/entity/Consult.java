package com.example.gov.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 咨询投诉实体（对应表 gov_consult）。
 */
@Data
public class Consult {
    private Long id;
    private Long userId;
    /**
     * 1咨询 2投诉 3建议
     */
    private Integer type;
    private String title;
    private String content;
    private String contactName;
    private String contactPhone;
    private String contactEmail;
    private String attachment;
    private String replyContent;
    private LocalDateTime replyTime;
    private Long replyUserId;
    /**
     * 0待处理 1处理中 2已回复 3已关闭
     */
    private Integer status;
    private Integer isPublic;
    private String ipAddress;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
