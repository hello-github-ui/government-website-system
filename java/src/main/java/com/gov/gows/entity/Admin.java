package com.gov.gows.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 管理员实体（对应表 gov_admin）。
 */
@Data
public class Admin {
    private Long id;
    private String username;
    /** 密码（bcrypt 加密存储） */
    private String password;
    private String name;
    private String phone;
    private String email;
    private Long roleId;
    private String avatar;
    /** 是否超级管理员 0否 1是 */
    private Integer isSuper;
    private Integer isAdmin;
    /** 授权状态 0待授权 1已授权 2已拒绝 */
    private Integer authStatus;
    private LocalDateTime authTime;
    private LocalDateTime authExpireTime;
    private LocalDateTime lastLoginTime;
    private String lastLoginIp;
    private Integer loginFailCount;
    private LocalDateTime lockTime;
    /** 状态 0禁用 1启用 */
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
