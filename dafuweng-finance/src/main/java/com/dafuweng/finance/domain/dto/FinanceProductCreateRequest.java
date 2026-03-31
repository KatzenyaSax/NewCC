package com.dafuweng.finance.domain.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class FinanceProductCreateRequest {

    @NotBlank(message = "产品名称不能为空")
    @Size(max = 100, message = "产品名称最多100字符")
    private String productName;

    @NotNull(message = "所属银行ID不能为空")
    private Long bankId;

    @NotNull(message = "最小贷款金额不能为空")
    @DecimalMin(value = "0.01", message = "最小贷款金额必须大于0")
    private BigDecimal minAmount;

    @NotNull(message = "最大贷款金额不能为空")
    @DecimalMin(value = "0.01", message = "最大贷款金额必须大于0")
    private BigDecimal maxAmount;

    @NotNull(message = "年利率不能为空")
    @DecimalMin(value = "0", message = "利率不能为负")
    @DecimalMax(value = "1", message = "利率不能大于1")
    private BigDecimal interestRate;

    @NotNull(message = "最小期限不能为空")
    @Min(value = 1, message = "最小期限最少1个月")
    private Integer minTerm;

    @NotNull(message = "最大期限不能为空")
    @Min(value = 1, message = "最大期限最少1个月")
    private Integer maxTerm;

    private List<String> requirements;

    private List<String> documents;

    private String productFeatures;

    @DecimalMin(value = "0", message = "渠道佣金比例不能为负")
    @DecimalMax(value = "1", message = "渠道佣金比例不能大于1")
    private BigDecimal commissionRate;
}
