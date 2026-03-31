package com.dafuweng.system.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DictVO {
    private Long id;
    private String dictType;
    private String dictCode;
    private String dictLabel;
    private String dictValue;
    private Integer sortOrder;
    private Integer status;
    private String remark;
    private LocalDateTime createdAt;
}
