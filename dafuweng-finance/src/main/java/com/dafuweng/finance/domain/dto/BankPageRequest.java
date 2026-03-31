package com.dafuweng.finance.domain.dto;

import lombok.Data;

@Data
public class BankPageRequest {

    private String bankName;
    private Integer status;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}
