package com.gov.gows.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 导航菜单实体（对应表 gov_nav）。
 */
@Data
public class Nav {
    private Long id;
    private Long parentId;
    private String navName;
    private String navUrl;
    private Integer navType;
    private String target;
    private String icon;
    private Integer sort;
    private Integer isShow;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
