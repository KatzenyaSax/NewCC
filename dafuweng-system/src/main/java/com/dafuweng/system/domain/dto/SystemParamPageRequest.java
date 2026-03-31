package com.dafuweng.system.domain.dto;

import lombok.Data;

@Data
public class SystemParamPageRequest {
    private String paramGroup;
    private Integer status;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}
