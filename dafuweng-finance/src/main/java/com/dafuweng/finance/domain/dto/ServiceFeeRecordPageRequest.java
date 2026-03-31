package com.dafuweng.finance.domain.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ServiceFeeRecordPageRequest {

    private Long contractId;
    private Integer feeType;
    private Integer paymentStatus;
    private Long accountantId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}
