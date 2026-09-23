package com.example.gov.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 管理员角色实体（对应表 gov_admin_role）。
 */
@Data
public class AdminRole {
    private Long id;
    private String roleName;
    private String roleDesc;
    /**
     * 权限列表 JSON 字符串
     */
    private String permissions;
    private Integer sort;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
