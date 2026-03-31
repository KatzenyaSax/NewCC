package com.dafuweng.system.domain.dto;

import lombok.Data;

@Data
public class DictPageRequest {
    private String dictType;
    private Integer status;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}
