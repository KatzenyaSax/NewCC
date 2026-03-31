package com.dafuweng.system.domain.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ZoneUpdateRequest {

    @NotNull(message = "战区ID不能为空")
    private Long id;

    @Size(max = 100, message = "战区名称最多100字符")
    private String zoneName;

    private Long directorId;

    private Integer sortOrder;

    @NotNull(message = "状态不能为空")
    private Integer status;
}
