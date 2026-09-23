package com.example.gov.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 网站设置实体（对应表 gov_settings）。
 */
@Data
public class Setting {
    private Long id;
    private String settingKey;
    private String settingValue;
    private String settingGroup;
    private String settingDesc;
    private Integer isSystem;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
