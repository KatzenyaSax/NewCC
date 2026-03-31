package com.dafuweng.sales.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ContractVO {
    private Long id;
    private String contractNo;
    private Long customerId;
    private String customerName;
    private String customerPhone;
    private Long salesRepId;
    private String salesRepName;
    private Long deptId;
    private String deptName;
    private Long productId;
    private String productName;
    private BigDecimal contractAmount;
    private BigDecimal actualLoanAmount;
    private BigDecimal serviceFeeRate;
    private BigDecimal serviceFee1;
    private BigDecimal serviceFee2;
    private Boolean serviceFee1Paid;
    private LocalDate serviceFee1PayDate;
    private Boolean serviceFee2Paid;
    private LocalDate serviceFee2PayDate;
    private Integer status;
    private String statusDesc;
    private LocalDate signDate;
    private String paperContractNo;
    private LocalDateTime financeSendTime;
    private LocalDateTime financeReceiveTime;
    private String loanUse;
    private String guaranteeInfo;
    private String rejectReason;
    private String remark;
    private List<ContractAttachmentVO> attachments;
    private LocalDateTime createdAt;
    private String createdByName;
    private LocalDateTime updatedAt;
}
