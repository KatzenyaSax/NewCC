package com.dafuweng.finance.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("service_fee_record")
public class ServiceFeeRecord implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long contractId;

    private Integer feeType;

    private BigDecimal amount;

    private BigDecimal shouldAmount;

    private String paymentMethod;

    private Integer paymentStatus;

    private LocalDate paymentDate;

    private String paymentAccount;

    private String receiptNo;

    private Long accountantId;

    private String remark;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @TableLogic
    private Integer deleted;
}
