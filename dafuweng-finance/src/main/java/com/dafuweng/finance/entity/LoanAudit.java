package com.dafuweng.finance.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("loan_audit")
public class LoanAudit implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long contractId;

    private Long financeSpecialistId;

    private Long recommendedProductId;

    private BigDecimal approvedAmount;

    private Integer approvedTerm;

    private BigDecimal approvedInterestRate;

    private Integer auditStatus;

    private Long bankId;

    private String bankAuditStatus;

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

    private LocalDateTime updatedAt;

    @TableLogic
    private Integer deleted;
}
