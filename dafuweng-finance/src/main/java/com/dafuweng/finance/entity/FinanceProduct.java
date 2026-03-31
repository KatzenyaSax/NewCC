package com.dafuweng.finance.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("finance_product")
public class FinanceProduct implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private String productCode;

    private String productName;

    private Long bankId;

    private String bankName;

    private BigDecimal minAmount;

    private BigDecimal maxAmount;

    private BigDecimal interestRate;

    private Integer minTerm;

    private Integer maxTerm;

    private String requirements;

    private String documents;

    private String productFeatures;

    private BigDecimal commissionRate;

    private Integer status;

    private Integer sortOrder;

    private LocalDateTime onlineTime;

    private LocalDateTime offlineTime;

    private Long createdBy;

    private LocalDateTime createdAt;

    private Long updatedBy;

    private LocalDateTime updatedAt;

    @TableLogic
    private Integer deleted;
}
