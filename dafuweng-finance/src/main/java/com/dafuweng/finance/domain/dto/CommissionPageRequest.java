package com.dafuweng.finance.domain.dto;

import lombok.Data;

@Data
public class CommissionPageRequest {

    private Long salesRepId;
    private Long contractId;
    private Integer status;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}
