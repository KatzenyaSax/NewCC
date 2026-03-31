package com.dafuweng.system.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SystemParamCreateRequest {

    @NotBlank(message = "参数键不能为空")
    @Size(max = 100, message = "参数键最多100字符")
    private String paramKey;

    @NotBlank(message = "参数值不能为空")
    private String paramValue;

    @Size(max = 50, message = "参数类型最多50字符")
    private String paramType;

    @Size(max = 50, message = "参数分组最多50字符")
    private String paramGroup;

    @Size(max = 200, message = "参数说明最多200字符")
    private String remark;

    private Integer sortOrder;

    private Integer status;
}
