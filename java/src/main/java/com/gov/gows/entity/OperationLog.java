package com.gov.gows.entity;

import lombok.Data;
import java.time.LocalDateTime;

/** 操作日志实体（对应表 gov_operation_log）。 */
@Data
public class OperationLog {
    private Long id;
    private Long userId;
    /** 1管理员 2前台用户 */
    private Integer userType;
    private String username;
    private String module;
    private String action;
    private String content;
    private String ipAddress;
    private String userAgent;
    private LocalDateTime createTime;
}
