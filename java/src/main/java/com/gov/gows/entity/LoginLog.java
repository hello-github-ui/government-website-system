package com.gov.gows.entity;

import lombok.Data;
import java.time.LocalDateTime;

/** 登录日志实体（对应表 gov_login_log）。 */
@Data
public class LoginLog {
    private Long id;
    private Long userId;
    private Integer userType;
    private String username;
    private Integer loginType;
    /** 1成功 0失败 */
    private Integer loginStatus;
    private String failReason;
    private String ipAddress;
    private String userAgent;
    private LocalDateTime createTime;
}
