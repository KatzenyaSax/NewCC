package com.dafuweng.finance.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class LoanAuditRecordVO {

    private Long id;
    private Long loanAuditId;
    private Long operatorId;
    private String operatorName;
    private String operatorRole;
    private String action;
    private String actionDesc;
    private String content;
    private List<String> attachmentUrls;
    private LocalDateTime createdAt;
}
