package com.dafuweng.finance.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoanAuditReceiveRequest {

    @NotNull(message = "合同ID不能为空")
    private Long contractId;

    private String remark;
}
