package com.dafuweng.finance.domain.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class LoanAuditReviewRequest {

    @NotNull(message = "审核ID不能为空")
    private Long auditId;

    @NotNull(message = "推荐产品ID不能为空")
    private Long recommendedProductId;

    @NotNull(message = "审批金额不能为空")
    @DecimalMin(value = "0.01", message = "审批金额必须大于0")
    private BigDecimal approvedAmount;

    @NotNull(message = "审批期限不能为空")
    @Min(value = 1, message = "审批期限最少1个月")
    private Integer approvedTerm;

    @NotNull(message = "审批利率不能为空")
    @DecimalMin(value = "0", message = "利率不能为负")
    @DecimalMax(value = "1", message = "利率不能大于1")
    private BigDecimal approvedInterestRate;

    @Size(max = 500, message = "审核意见最多500字符")
    private String auditOpinion;

    private List<String> attachmentUrls;
}
