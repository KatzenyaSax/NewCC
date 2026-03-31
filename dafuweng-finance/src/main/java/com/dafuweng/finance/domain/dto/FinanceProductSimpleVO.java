package com.dafuweng.finance.domain.dto;

import lombok.Data;

@Data
public class FinanceProductSimpleVO {

    private Long id;
    private String productName;
    private String bankName;
    private String amountRange;
    private String interestRateDesc;
    private String termRange;
}
