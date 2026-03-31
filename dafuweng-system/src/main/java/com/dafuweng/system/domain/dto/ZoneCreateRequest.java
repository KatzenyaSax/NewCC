package com.dafuweng.system.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ZoneCreateRequest {

    @NotBlank(message = "战区编码不能为空")
    @Size(max = 50, message = "战区编码最多50字符")
    private String zoneCode;

    @NotBlank(message = "战区名称不能为空")
    @Size(max = 100, message = "战区名称最多100字符")
    private String zoneName;

    private Long directorId;

    private Integer sortOrder;

    private Integer status;
}
