package com.dafuweng.finance.domain.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class FinanceProductVO {

    private Long id;
    private String productCode;
    private String productName;
    private Long bankId;
    private String bankName;
    private BigDecimal minAmount;
    private BigDecimal maxAmount;
    private String amountRange;
    private BigDecimal interestRate;
    private String interestRateDesc;
    private Integer minTerm;
    private Integer maxTerm;
    private String termRange;
    private List<String> requirements;
    private List<String> documents;
    private String productFeatures;
    private BigDecimal commissionRate;
    private Integer status;
    private String statusDesc;
    private LocalDateTime onlineTime;
    private LocalDateTime offlineTime;
    private LocalDateTime createdAt;
}
