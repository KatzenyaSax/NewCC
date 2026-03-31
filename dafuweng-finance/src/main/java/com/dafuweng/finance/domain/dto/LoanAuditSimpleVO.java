package com.dafuweng.finance.domain.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class LoanAuditSimpleVO {

    private Long id;
    private String contractNo;
    private String customerName;
    private String customerPhone;
    private BigDecimal contractAmount;
    private Integer auditStatus;
    private String auditStatusDesc;
    private LocalDateTime createdAt;
}
