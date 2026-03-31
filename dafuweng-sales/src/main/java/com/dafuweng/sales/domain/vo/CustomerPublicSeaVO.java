package com.dafuweng.sales.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CustomerPublicSeaVO {
    private Long id;
    private String name;
    private String phone;
    private Integer customerType;
    private String customerTypeDesc;
    private Integer intentionLevel;
    private String intentionLevelDesc;
    private LocalDateTime publicSeaTime;
    private String publicSeaReason;
    private String source;
    private BigDecimal loanIntentionAmount;
    private String loanIntentionProduct;
    private LocalDateTime createdAt;
    private String salesRepName;
    private String deptName;
}
