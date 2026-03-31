package com.dafuweng.sales.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CustomerDuplicateCheckResponse {
    /** 是否存在 */
    private boolean duplicate;

    /** 已存在的客户ID */
    private Long existCustomerId;

    /** 已存在的客户姓名 */
    private String existCustomerName;

    /** 已录入的销售姓名 */
    private String existSalesRepName;

    /** 录入时间 */
    private LocalDateTime existCreatedAt;
}
