package com.dafuweng.finance.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BankVO {

    private Long id;
    private String bankCode;
    private String bankName;
    private String bankBranch;
    private String contactPerson;
    private String contactPhone;
    private Integer status;
    private String statusDesc;
    private Integer sortOrder;
    private LocalDateTime createdAt;
}
