package com.dafuweng.sales.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CustomerVO {
    private Long id;
    private String name;
    private String phone;
    private String idCard;
    private Integer customerType;
    private String customerTypeDesc;
    private String companyName;
    private String companyLegalPerson;
    private BigDecimal companyRegCapital;
    private Long salesRepId;
    private String salesRepName;
    private Long deptId;
    private String deptName;
    private Long zoneId;
    private String zoneName;
    private Integer intentionLevel;
    private String intentionLevelDesc;
    private Integer status;
    private String statusDesc;
    private LocalDateTime lastContactDate;
    private LocalDate nextFollowUpDate;
    private LocalDateTime publicSeaTime;
    private String publicSeaReason;
    private List<AnnotationVO> annotations;
    private String source;
    private BigDecimal loanIntentionAmount;
    private String loanIntentionProduct;
    private LocalDateTime createdAt;
    private String createdByName;
    private LocalDateTime updatedAt;
}
