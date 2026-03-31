package com.dafuweng.sales.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CustomerTransferRequest {

    @NotNull(message = "客户ID不能为空")
    private Long customerId;

    @NotNull(message = "目标销售ID不能为空")
    private Long toSalesRepId;

    @NotBlank(message = "转移原因不能为空")
    @Size(max = 200, message = "转移原因最多200字符")
    private String reason;
}
