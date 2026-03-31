package com.dafuweng.sales.domain.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ContractCreateRequest {

    @NotNull(message = "客户ID不能为空")
    private Long customerId;

    @NotNull(message = "合同金额不能为空")
    @DecimalMin(value = "0.01", message = "合同金额必须大于0")
    private BigDecimal contractAmount;

    @NotNull(message = "服务费率不能为空")
    @DecimalMin(value = "0", message = "服务费率不能为负")
    @DecimalMax(value = "1", message = "服务费率不能大于1")
    private BigDecimal serviceFeeRate;

    /** 贷款用途 */
    @Size(max = 200, message = "贷款用途最多200字符")
    private String loanUse;

    /** 担保信息（JSON） */
    private String guaranteeInfo;

    @Size(max = 500, message = "备注最多500字符")
    private String remark;
}
