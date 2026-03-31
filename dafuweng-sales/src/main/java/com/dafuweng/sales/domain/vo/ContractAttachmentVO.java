package com.dafuweng.sales.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ContractAttachmentVO {
    private Long id;
    private Long contractId;
    private String attachmentType;
    private String attachmentTypeDesc;
    private String fileUrl;
    private String fileName;
    private Long fileSize;
    private LocalDateTime uploadTime;
    private String uploadByName;
}
