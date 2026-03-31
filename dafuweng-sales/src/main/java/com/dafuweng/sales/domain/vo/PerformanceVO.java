package com.dafuweng.sales.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PerformanceVO {
    private Long id;
    private Long contractId;
    private String contractNo;
    private Long salesRepId;
    private String salesRepName;
    private Long deptId;
    private String deptName;
    private Long zoneId;
    private String zoneName;
    private BigDecimal contractAmount;
    private BigDecimal commissionRate;
    private BigDecimal commissionAmount;
    private Integer status;
    private String statusDesc;
    private LocalDateTime calculateTime;
    private LocalDateTime confirmTime;
    private LocalDateTime grantTime;
    private String remark;
    private LocalDateTime createdAt;
}
