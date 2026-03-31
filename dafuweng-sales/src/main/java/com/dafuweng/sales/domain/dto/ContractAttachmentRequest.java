package com.dafuweng.sales.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ContractAttachmentRequest {

    @NotBlank(message = "附件类型不能为空")
    @Pattern(regexp = "^(business_license|id_card|other)$", message = "附件类型不合法")
    private String attachmentType;

    @NotBlank(message = "文件URL不能为空")
    @Size(max = 500, message = "文件URL最多500字符")
    private String fileUrl;

    @NotBlank(message = "文件名不能为空")
    @Size(max = 200, message = "文件名最多200字符")
    private String fileName;

    private Long fileSize;

    @Size(max = 32, message = "文件MD5最多32字符")
    private String fileMd5;
}
