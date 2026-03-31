package com.dafuweng.sales.domain.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CustomerPageRequest {
    private String name;
    private String phone;
    private Integer customerType;
    private Integer status;
    private Integer intentionLevel;
    private Long salesRepId;
    private Long deptId;
    private Long zoneId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}
