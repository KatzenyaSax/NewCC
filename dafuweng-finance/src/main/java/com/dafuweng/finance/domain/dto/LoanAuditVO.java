package com.dafuweng.finance.domain.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class LoanAuditVO {

    private Long id;
    private Long contractId;
    private String contractNo;
    private Long customerId;
    private String customerName;
    private String customerPhone;
    private BigDecimal contractAmount;
    private Long financeSpecialistId;
    private String financeSpecialistName;
    private Long recommendedProductId;
    private String recommendedProductName;
    private BigDecimal approvedAmount;
    private Integer approvedTerm;
    private BigDecimal approvedInterestRate;
    private Integer auditStatus;
    private String auditStatusDesc;
    private Long bankId;
    private String bankName;
    private String bankAuditStatus;
    private String bankAuditStatusDesc;
    private LocalDateTime bankApplyTime;
    private LocalDateTime bankFeedbackTime;
    private String bankFeedbackContent;
    private String rejectReason;
    private String auditOpinion;
    private LocalDateTime auditDate;
    private LocalDateTime loanGrantedDate;
    private BigDecimal actualLoanAmount;
    private BigDecimal actualInterestRate;
    private LocalDateTime createdAt;
    private List<LoanAuditRecordVO> auditRecords;
}
