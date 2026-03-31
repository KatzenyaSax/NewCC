package com.dafuweng.system.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class DepartmentVO {
    private Long id;
    private String deptCode;
    private String deptName;
    private Long parentId;
    private String parentName;
    private Long zoneId;
    private String zoneName;
    private Long managerId;
    private String managerName;
    private Integer sortOrder;
    private Integer status;
    private String statusDesc;
    private List<DepartmentVO> children;
    private LocalDateTime createdAt;
}
