package com.gov.gows.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 前端文字配置实体（对应表 gov_language）。
 */
@Data
public class Language {
    private Long id;
    private String langKey;
    private String langValue;
    private String langGroup;
    private String module;
    private Integer isDefault;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
