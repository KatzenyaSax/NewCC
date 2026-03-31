package com.dafuweng.finance.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FinanceProductPageRequest {

    private String productName;
    private Long bankId;
    private BigDecimal minLoanAmount;
    private BigDecimal maxLoanAmount;
    private Integer term;
    private Integer status;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}
