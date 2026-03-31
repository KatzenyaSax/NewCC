package com.dafuweng.finance.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("loan_audit_record")
public class LoanAuditRecord implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long loanAuditId;

    private Long operatorId;

    private String operatorName;

    private String operatorRole;

    private String action;

    private String content;

    private String attachmentUrls;

    private LocalDateTime createdAt;
}
