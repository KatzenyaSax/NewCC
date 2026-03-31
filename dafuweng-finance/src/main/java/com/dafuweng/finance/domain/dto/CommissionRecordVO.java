package com.dafuweng.finance.domain.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CommissionRecordVO {

    private Long id;
    private Long performanceId;
    private Long salesRepId;
    private String salesRepName;
    private Long contractId;
    private String contractNo;
    private BigDecimal commissionAmount;
    private BigDecimal commissionRate;
    private Integer status;
    private String statusDesc;
    private LocalDateTime confirmTime;
    private LocalDateTime grantTime;
    private String grantAccount;
    private String remark;
    private LocalDateTime createdAt;
}
