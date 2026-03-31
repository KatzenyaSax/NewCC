package com.dafuweng.system.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ZoneVO {
    private Long id;
    private String zoneCode;
    private String zoneName;
    private Long directorId;
    private String directorName;
    private Integer sortOrder;
    private Integer status;
    private String statusDesc;
    private LocalDateTime createdAt;
}
