package com.dafuweng.sales.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ContractPayFirstRequest {

    @NotNull(message = "合同ID不能为空")
    private Long contractId;

    /** 首期支付日期 */
    @NotNull(message = "支付日期不能为空")
    private LocalDate payDate;
}
