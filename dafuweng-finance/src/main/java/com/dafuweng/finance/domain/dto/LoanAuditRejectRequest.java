package com.dafuweng.finance.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoanAuditRejectRequest {

    @NotNull(message = "审核ID不能为空")
    private Long auditId;

    @NotBlank(message = "拒绝原因不能为空")
    @Size(max = 500, message = "拒绝原因最多500字符")
    private String rejectReason;
}
