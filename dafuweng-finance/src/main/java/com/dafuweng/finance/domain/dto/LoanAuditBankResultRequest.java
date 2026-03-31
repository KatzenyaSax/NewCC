package com.dafuweng.finance.domain.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class LoanAuditBankResultRequest {

    @NotNull(message = "审核ID不能为空")
    private Long auditId;

    @NotBlank(message = "银行审核状态不能为空")
    @Pattern(regexp = "^(approved|rejected)$", message = "银行审核状态不合法")
    private String bankAuditStatus;

    private String bankFeedbackContent;

    @DecimalMin(value = "0", message = "放款金额不能为负")
    private BigDecimal actualLoanAmount;

    @DecimalMin(value = "0", message = "利率不能为负")
    @DecimalMax(value = "1", message = "利率不能大于1")
    private BigDecimal actualInterestRate;

    private LocalDate loanGrantedDate;
}
