package com.dafuweng.finance.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("commission_record")
public class CommissionRecord implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long performanceId;

    private Long salesRepId;

    private Long contractId;

    private BigDecimal commissionAmount;

    private BigDecimal commissionRate;

    private Integer status;

    private LocalDateTime confirmTime;

    private LocalDateTime grantTime;

    private String grantAccount;

    private String remark;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @TableLogic
    private Integer deleted;
}
