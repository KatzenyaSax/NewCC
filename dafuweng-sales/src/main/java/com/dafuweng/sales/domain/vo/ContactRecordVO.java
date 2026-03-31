package com.dafuweng.sales.domain.vo;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ContactRecordVO {
    private Long id;
    private Long customerId;
    private String customerName;
    private Long salesRepId;
    private String salesRepName;
    private Integer contactType;
    private String contactTypeDesc;
    private LocalDateTime contactDate;
    private String content;
    private Integer intentionBefore;
    private String intentionBeforeDesc;
    private Integer intentionAfter;
    private String intentionAfterDesc;
    private LocalDate followUpDate;
    private List<String> attachmentUrls;
    private LocalDateTime createdAt;
}
