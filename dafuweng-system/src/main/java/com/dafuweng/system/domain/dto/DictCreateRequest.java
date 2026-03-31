package com.dafuweng.system.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DictCreateRequest {

    @NotBlank(message = "字典类型不能为空")
    @Size(max = 50, message = "字典类型最多50字符")
    private String dictType;

    @NotBlank(message = "字典编码不能为空")
    @Size(max = 50, message = "字典编码最多50字符")
    private String dictCode;

    @NotBlank(message = "字典标签不能为空")
    @Size(max = 100, message = "字典标签最多100字符")
    private String dictLabel;

    @NotBlank(message = "字典值不能为空")
    @Size(max = 200, message = "字典值最多200字符")
    private String dictValue;

    private Integer sortOrder;

    private Integer status;

    @Size(max = 200, message = "备注最多200字符")
    private String remark;
}
