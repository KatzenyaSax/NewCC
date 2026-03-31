package com.dafuweng.finance.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("bank")
public class Bank implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private String bankCode;

    private String bankName;

    private String bankBranch;

    private String contactPerson;

    private String contactPhone;

    private Integer status;

    private Integer sortOrder;

    private Long createdBy;

    private LocalDateTime createdAt;

    private Long updatedBy;

    private LocalDateTime updatedAt;

    @TableLogic
    private Integer deleted;
}
