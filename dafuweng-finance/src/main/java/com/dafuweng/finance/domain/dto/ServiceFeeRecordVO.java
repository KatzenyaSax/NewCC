package com.dafuweng.finance.domain.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ServiceFeeRecordVO {

    private Long id;
    private Long contractId;
    private String contractNo;
    private String customerName;
    private Integer feeType;
    private String feeTypeDesc;
    private BigDecimal amount;
    private BigDecimal shouldAmount;
    private String paymentMethod;
    private String paymentMethodDesc;
    private Integer paymentStatus;
    private String paymentStatusDesc;
    private LocalDate paymentDate;
    private String paymentAccount;
    private String receiptNo;
    private Long accountantId;
    private String accountantName;
    private String remark;
    private LocalDateTime createdAt;
}
