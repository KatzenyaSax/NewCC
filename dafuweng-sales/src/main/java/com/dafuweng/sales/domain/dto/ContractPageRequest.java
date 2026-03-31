package com.dafuweng.sales.domain.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ContractPageRequest {
    private String contractNo;
    private Long customerId;
    private String customerName;
    private Long salesRepId;
    private Long deptId;
    private Long productId;
    private Integer status;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}
