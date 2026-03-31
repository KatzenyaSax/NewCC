package com.dafuweng.finance.domain.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class FinanceProductUpdateRequest {

    @NotNull(message = "产品ID不能为空")
    private Long id;

    @Size(max = 100, message = "产品名称最多100字符")
    private String productName;

    private Long bankId;

    @DecimalMin(value = "0.01", message = "最小贷款金额必须大于0")
    private BigDecimal minAmount;

    @DecimalMin(value = "0.01", message = "最大贷款金额必须大于0")
    private BigDecimal maxAmount;

    @DecimalMin(value = "0", message = "利率不能为负")
    @DecimalMax(value = "1", message = "利率不能大于1")
    private BigDecimal interestRate;

    @Min(value = 1, message = "最小期限最少1个月")
    private Integer minTerm;

    @Min(value = 1, message = "最大期限最少1个月")
    private Integer maxTerm;

    private List<String> requirements;
    private List<String> documents;
    private String productFeatures;

    @DecimalMin(value = "0", message = "渠道佣金比例不能为负")
    @DecimalMax(value = "1", message = "渠道佣金比例不能大于1")
    private BigDecimal commissionRate;

    private Integer status;
}
