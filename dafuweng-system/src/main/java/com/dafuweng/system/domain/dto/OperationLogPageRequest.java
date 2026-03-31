package com.dafuweng.system.domain.dto;

import lombok.Data;

@Data
public class OperationLogPageRequest {
    private Long userId;
    private String module;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}
