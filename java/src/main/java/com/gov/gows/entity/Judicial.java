package com.gov.gows.entity;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/** 裁判文书实体（对应表 gov_judicial）。 */
@Data
public class Judicial {
    private Long id;
    private String caseNo;
    private String caseName;
    private String court;
    private String caseType;
    private String caseCause;
    private LocalDate judgeDate;
    private LocalDate publishDate;
    private String content;
    private String contentDesensitized;
    private String parties;
    private String judge;
    private String clerk;
    private String attachment;
    private Integer views;
    private Integer downloadCount;
    /** 0待审核 1已通过 2已驳回 */
    private Integer checkStatus;
    private String checkRemark;
    private Integer isPublic;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
