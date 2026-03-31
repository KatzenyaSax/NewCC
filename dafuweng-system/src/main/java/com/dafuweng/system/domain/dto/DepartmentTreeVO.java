package com.dafuweng.system.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class DepartmentTreeVO {
    private Long id;
    private String deptName;
    private Long parentId;
    private Long zoneId;
    private String zoneName;
    private List<DepartmentTreeVO> children;
}
