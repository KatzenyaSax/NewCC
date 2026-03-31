package com.dafuweng.system.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SystemParamVO {
    private Long id;
    private String paramKey;
    private String paramValue;
    private String paramType;
    private String paramGroup;
    private String remark;
    private Integer sortOrder;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
