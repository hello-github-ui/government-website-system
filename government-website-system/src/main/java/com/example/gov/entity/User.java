package com.example.gov.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 前台用户实体（对应表 gov_user）。
 */
@Data
public class User {
    private Long id;
    private String username;
    private String password;
    private String realName;
    private String phone;
    private String email;
    private String idCard;
    private String userIntro;
    private String avatar;
    private Integer isAdmin;
    private Integer authStatus;
    private LocalDateTime authTime;
    private LocalDateTime authExpireTime;
    private LocalDateTime lastLoginTime;
    private String lastLoginIp;
    private Integer loginFailCount;
    private LocalDateTime lockTime;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
