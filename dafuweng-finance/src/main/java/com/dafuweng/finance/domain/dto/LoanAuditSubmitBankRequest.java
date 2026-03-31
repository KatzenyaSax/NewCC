package com.dafuweng.finance.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoanAuditSubmitBankRequest {

    @NotNull(message = "审核ID不能为空")
    private Long auditId;

    @NotNull(message = "银行ID不能为空")
    private Long bankId;

    private String remark;
}
