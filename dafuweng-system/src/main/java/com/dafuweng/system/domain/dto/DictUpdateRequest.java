package com.dafuweng.system.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DictUpdateRequest {

    @NotNull(message = "字典ID不能为空")
    private Long id;

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
