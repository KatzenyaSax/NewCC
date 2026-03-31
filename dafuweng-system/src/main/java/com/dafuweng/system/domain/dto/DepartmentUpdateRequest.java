package com.dafuweng.system.domain.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DepartmentUpdateRequest {

    @NotNull(message = "部门ID不能为空")
    private Long id;

    @Size(max = 100, message = "部门名称最多100字符")
    private String deptName;

    @NotNull(message = "状态不能为空")
    private Integer status;

    private Long managerId;

    private Integer sortOrder;
}
