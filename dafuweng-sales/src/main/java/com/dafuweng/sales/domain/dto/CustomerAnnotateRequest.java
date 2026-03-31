package com.dafuweng.sales.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CustomerAnnotateRequest {

    @NotNull(message = "客户ID不能为空")
    private Long customerId;

    @NotBlank(message = "批注内容不能为空")
    @Size(max = 500, message = "批注内容最多500字符")
    private String content;
}
