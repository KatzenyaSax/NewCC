package com.dafuweng.system.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DepartmentCreateRequest {

    @NotBlank(message = "部门名称不能为空")
    @Size(max = 100, message = "部门名称最多100字符")
    private String deptName;

    @NotBlank(message = "部门编码不能为空")
    @Size(max = 50, message = "部门编码最多50字符")
    private String deptCode;

    @NotNull(message = "父部门ID不能为空")
    private Long parentId;

    @NotNull(message = "战区ID不能为空")
    private Long zoneId;

    private Long managerId;

    private Integer sortOrder;
}
